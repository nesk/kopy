package com.javiersc.kotlin.kopy.compiler.ir

import com.javiersc.kotlin.kopy.compiler.ir.transformers.IrAtomicPropertyTransformer
import com.javiersc.kotlin.kopy.compiler.ir.transformers.IrInitKopyableFunctionTransformer
import com.javiersc.kotlin.kopy.compiler.ir.transformers.IrSetOrUpdateCallTransformer
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.visitors.IrElementTransformer

internal class IrKopyGenerationExtension(
    private val configuration: CompilerConfiguration,
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        moduleFragment.generate(pluginContext)
    }

    @JvmName("generate2")
    private fun IrModuleFragment.generate(pluginContext: IrPluginContext) {
        transform(IrAtomicPropertyTransformer(this, pluginContext))
        transform(IrInitKopyableFunctionTransformer(this, pluginContext))
        transform(IrSetOrUpdateCallTransformer(this, pluginContext))

        println("FINISHED")
    }

    private fun <T> IrModuleFragment.transform(transformer: IrElementTransformer<T?>) {
        this.transform(transformer, null)
    }
}
