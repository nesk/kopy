@file:Suppress("ReturnCount")

package com.javiersc.kotlin.kopy.compiler.ir.transformers

import com.javiersc.kotlin.compiler.extensions.common.fqName
import com.javiersc.kotlin.compiler.extensions.common.toCallableId
import com.javiersc.kotlin.compiler.extensions.common.toName
import com.javiersc.kotlin.compiler.extensions.ir.asIr
import com.javiersc.kotlin.compiler.extensions.ir.asIrOrNull
import com.javiersc.kotlin.compiler.extensions.ir.createIrFunctionExpression
import com.javiersc.kotlin.compiler.extensions.ir.declarationIrBuilder
import com.javiersc.kotlin.compiler.extensions.ir.filterIrIsInstance
import com.javiersc.kotlin.compiler.extensions.ir.firstIrSimpleFunction
import com.javiersc.kotlin.compiler.extensions.ir.hasAnnotation
import com.javiersc.kotlin.compiler.extensions.ir.name
import com.javiersc.kotlin.compiler.extensions.ir.toIrTreeNode
import com.javiersc.kotlin.kopy.KopyFunctionCopy
import com.javiersc.kotlin.kopy.compiler.ir.utils.findDeclarationParent
import com.javiersc.kotlin.kopy.compiler.ir.utils.isKopySet
import com.javiersc.kotlin.kopy.compiler.ir.utils.isKopySetOrUpdate
import com.javiersc.kotlin.kopy.compiler.ir.utils.isKopyUpdate
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.builtins.StandardNames
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.backend.js.utils.valueArguments
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrDeclarationParent
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrBlockBody
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrDeclarationReference
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrFunctionExpression
import org.jetbrains.kotlin.ir.expressions.IrGetValue
import org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression
import org.jetbrains.kotlin.ir.expressions.IrReturn
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.symbols.IrSymbol
import org.jetbrains.kotlin.ir.transformStatement
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.classOrFail
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.deepCopyWithSymbols
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.getArguments
import org.jetbrains.kotlin.ir.util.getSimpleFunction
import org.jetbrains.kotlin.ir.util.indexOrMinusOne
import org.jetbrains.kotlin.ir.util.patchDeclarationParents
import org.jetbrains.kotlin.ir.visitors.IrElementTransformerVoid
import org.jetbrains.kotlin.name.SpecialNames

internal class IrSetOrUpdateCallTransformer(
    private val moduleFragment: IrModuleFragment,
    private val pluginContext: IrPluginContext,
) : IrElementTransformerVoidWithContext() {

    override fun visitCall(expression: IrCall): IrExpression {
        val expressionCall: IrCall = expression
        fun originalCall(): IrExpression = super.visitCall(expression)
        if (!expression.isKopySetOrUpdate) return originalCall()

        val originalCallCallWithAlsoCall: IrCall =
            when {
                expression.isKopySet -> {
                    val setValueParameterTransformer: IrElementTransformerVoid =
                        SetValueTransformer(expressionCall)
                    expression.also { it.transform(setValueParameterTransformer, null) }
                }
                expression.isKopyUpdate -> {
                    val updateReturnTransformer: IrElementTransformerVoid =
                        UpdateReturnTransformer(expressionCall)
                    expression.also { it.transformStatement(updateReturnTransformer) }
                }
                else -> return originalCall()
            }
        return originalCallCallWithAlsoCall
    }

    private inner class SetValueTransformer(
        private val expressionCall: IrCall,
    ) : IrElementTransformerVoid() {
        override fun visitExpression(expression: IrExpression): IrExpression {
            fun original(): IrExpression = super.visitExpression(expression)
            if (expression !in expressionCall.valueArguments) return original()
            val parent: IrDeclarationParent =
                expression.findDeclarationParent() ?: return original()
            if (parent != expressionCall.findDeclarationParent()) return original()
            val alsoCall: IrCall =
                pluginContext.createAlsoCall(expressionCall, parent) ?: return original()
            val expressionWithAlsoCall: IrCall = alsoCall.also { it.extensionReceiver = expression }
            return expressionWithAlsoCall
        }
    }

    private inner class UpdateReturnTransformer(
        private val expressionCall: IrCall,
    ) : IrElementTransformerVoid() {
        override fun visitReturn(expression: IrReturn): IrExpression {
            fun original(): IrExpression = super.visitReturn(expression)
            val parent: IrDeclarationParent =
                expression.findDeclarationParent() ?: return original()
            val updateLambda: IrSimpleFunction =
                expressionCall.valueArguments
                    .firstOrNull()
                    ?.asIrOrNull<IrFunctionExpression>()
                    ?.function ?: return original()
            if (parent != updateLambda) return original()
            val alsoCall: IrCall =
                pluginContext.createAlsoCall(expressionCall, parent) ?: return original()
            val value: IrExpression = expression.value
            val expressionWithAlsoCall: IrReturn =
                expression.also { irReturn ->
                    irReturn.value = alsoCall.also { it.extensionReceiver = value }
                }
            return expressionWithAlsoCall
        }
    }

    private fun IrPluginContext.createAlsoCall(
        expression: IrCall,
        expressionParent: IrDeclarationParent
    ): IrCall? {
        val setOrUpdateType: IrType = expression.extensionReceiver?.type ?: return null

        val alsoBlockFunction: IrSimpleFunction =
            createAlsoBlockFunction(
                expressionParent = expressionParent,
                setOrUpdateType = setOrUpdateType,
                expression = expression
            ) ?: return null

        val alsoFunction: IrSimpleFunction = firstIrSimpleFunction("kotlin.also".toCallableId())

        val alsoBlockFunctionExpression: IrFunctionExpression =
            createIrFunctionExpression(
                type = irBuiltIns.run { functionN(1).typeWith(setOrUpdateType, unitType) },
                function = alsoBlockFunction,
                origin = IrStatementOrigin.LAMBDA,
            )

        val alsoCall: IrCall =
            declarationIrBuilder(alsoFunction.symbol).run {
                irCall(alsoFunction.symbol).also {
                    it.patchDeclarationParents(expressionParent)
                    it.type = setOrUpdateType
                    it.putTypeArgument(0, setOrUpdateType)
                    it.putValueArgument(index = 0, valueArgument = alsoBlockFunctionExpression)
                }
            }

        return alsoCall
    }

    private fun IrElement.findDeclarationParent(): IrDeclarationParent? =
        findDeclarationParent(moduleFragment)?.asIrOrNull<IrDeclarationParent>()

    private fun createAlsoBlockFunction(
        expressionParent: IrDeclarationParent,
        setOrUpdateType: IrType,
        expression: IrCall
    ): IrSimpleFunction? {
        val alsoBlockFunction: IrSimpleFunction =
            pluginContext.irFactory
                .buildFun {
                    name = SpecialNames.ANONYMOUS
                    visibility = DescriptorVisibilities.LOCAL
                    returnType = pluginContext.irBuiltIns.unitType
                    origin = IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA
                }
                .apply {
                    parent = expressionParent
                    addValueParameter {
                        this.name = StandardNames.IMPLICIT_LAMBDA_PARAMETER_NAME
                        this.type = setOrUpdateType
                    }

                    val alsoBodyBlock: IrBlockBody =
                        pluginContext.declarationIrBuilder(this.symbol).irBlockBody {
                            val itValueParameter: IrValueParameter = valueParameters.first()
                            val alsoItValueParameterGetValue: IrGetValue = irGet(itValueParameter)
                            val copyChainCall: IrCall =
                                expression.createCopyChainCall(alsoItValueParameterGetValue)
                                    ?: return null
                            val setKopyableReferenceCall: IrCall? =
                                expression.createSetKopyableReferenceCall(
                                    copyChainCall = copyChainCall,
                                )
                            if (setKopyableReferenceCall != null) +setKopyableReferenceCall
                        }

                    body = alsoBodyBlock
                }
        return alsoBlockFunction
    }

    private fun IrCall.createCopyChainCall(alsoItValueParameterGetValue: IrGetValue): IrCall? {
        val getKopyableRefCall: IrCall = createGetKopyableReferenceCall()
        val getKopyableRefType: IrSimpleType = getKopyableRefCall.type.asIr()

        val chain: List<IrMemberAccessExpression<*>> = dispatchersChain().reversed()
        if (chain.isEmpty()) return null

        val dispatchers: List<IrMemberAccessExpression<*>> = listOf(getKopyableRefCall) + chain

        val calls: List<IrCall> =
            dispatchers
                .zipWithNext { current, next ->
                    val dataClass: IrClassSymbol =
                        if (current.type == getKopyableRefType) getKopyableRefType.classOrFail
                        else current.type.classOrFail

                    val getFun: IrSimpleFunction = next.asIr<IrCall>().symbol.owner
                    val isLast: Boolean = next == dispatchers.last()
                    val argumentValue: IrDeclarationReference =
                        if (isLast) alsoItValueParameterGetValue
                        else next.apply { this.dispatchReceiver = current }

                    val copyCall: IrCall? =
                        current.symbol.createCopyCall(
                            dataClass = dataClass,
                            dispatchReceiver = current,
                            propertyGetFunction = getFun,
                            argumentValue = argumentValue,
                        )
                    copyCall ?: return null
                }
                .reversed()

        val copyChainCall: IrCall =
            calls.reduce { acc, irCall ->
                val argumentIndex: Int =
                    irCall.getArguments().firstNotNullOfOrNull {
                        val index = it.first.indexOrMinusOne
                        if (index != -1) index else null
                    } ?: return null
                irCall.putValueArgument(argumentIndex, acc)
                irCall
            }

        copyChainCall
            .toIrTreeNode()
            .asSequence()
            .filterIrIsInstance<IrCall>()
            .filter { call ->
                val dispatcherName = call.dispatchReceiver.asIrOrNull<IrCall>()?.name
                dispatcherName in dispatchers.mapNotNull { it.asIrOrNull<IrCall>()?.name }
            }
            .forEach { it.dispatchReceiver = it.dispatchReceiver?.deepCopyWithSymbols() }

        return copyChainCall
    }

    private fun IrCall.createGetKopyableReferenceCall(): IrCall {
        val kopyableGetValue: IrGetValue =
            dispatchReceiver?.asIrOrNull<IrGetValue>() ?: error("No value found")

        val kopyableClass: IrClassSymbol = kopyableGetValue.type.classOrFail
        val getKopyableReferenceFunction: IrSimpleFunctionSymbol =
            kopyableClass.getSimpleFunction("getKopyableReference") ?: error("No function found")

        val getKopyableReferenceCall: IrCall =
            pluginContext.declarationIrBuilder(kopyableGetValue.symbol).run {
                irCall(getKopyableReferenceFunction).apply {
                    dispatchReceiver = kopyableGetValue
                    type = kopyableGetValue.type
                }
            }
        return getKopyableReferenceCall
    }

    private fun IrMemberAccessExpression<*>.dispatchersChain(): List<IrMemberAccessExpression<*>> =
        buildList {
            val extensionReceiver: IrMemberAccessExpression<*> =
                runCatching {
                        extensionReceiver
                            ?.asIrOrNull<IrMemberAccessExpression<*>>()
                            ?.deepCopyWithSymbols()
                    }
                    .getOrNull() ?: return@buildList

            add(extensionReceiver)
            fun extract(expression: IrMemberAccessExpression<*>) {
                val expressionCopy: IrMemberAccessExpression<*> = expression.deepCopyWithSymbols()
                val dispatcher: IrMemberAccessExpression<*>? =
                    expressionCopy.dispatchReceiver.asIrOrNull<IrMemberAccessExpression<*>>()
                if (dispatcher == null) {
                    onEach { it.dispatchReceiver = null }
                    return
                }
                add(dispatcher.deepCopyWithSymbols())
                extract(dispatcher)
            }
            extract(extensionReceiver)
        }

    private fun IrSymbol.createCopyCall(
        dataClass: IrClassSymbol,
        dispatchReceiver: IrExpression,
        propertyGetFunction: IrSimpleFunction,
        argumentValue: IrExpression,
    ): IrCall? {
        val copyCall: IrCall? =
            pluginContext.declarationIrBuilder(this).run {
                val kotlinCopyFunctionSymbol: IrSimpleFunctionSymbol =
                    dataClass.owner.functions
                        .firstOrNull {
                            it.name == "copy".toName() &&
                                !it.hasAnnotation(fqName<KopyFunctionCopy>())
                        }
                        ?.symbol ?: return null
                irCall(kotlinCopyFunctionSymbol).apply {
                    this.dispatchReceiver = dispatchReceiver

                    val copyParamIndex: Int =
                        kotlinCopyFunctionSymbol.owner.valueParameters.indexOfFirst {
                            it.name == propertyGetFunction.correspondingPropertySymbol?.owner?.name
                        }
                    if (copyParamIndex == -1) return@run null
                    putValueArgument(copyParamIndex, argumentValue)
                }
            }
        return copyCall
    }

    private fun IrCall.createSetKopyableReferenceCall(copyChainCall: IrCall): IrCall? {
        val kopyableGetValue: IrGetValue = dispatchReceiver?.asIrOrNull<IrGetValue>() ?: return null

        val kopyableClass: IrClassSymbol = kopyableGetValue.type.classOrFail

        val setKopyableReferenceFunction: IrSimpleFunctionSymbol =
            kopyableClass.getSimpleFunction("setKopyableReference")!!

        val setKopyableReferenceCall: IrCall =
            pluginContext.declarationIrBuilder(kopyableGetValue.symbol).run {
                irCall(setKopyableReferenceFunction).apply {
                    dispatchReceiver = kopyableGetValue.deepCopyWithSymbols()
                    putValueArgument(0, copyChainCall)
                }
            }

        return setKopyableReferenceCall
    }
}