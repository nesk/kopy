package com.javiersc.kotlin.kopy.gradle.plugin

import com.javiersc.gradle.logging.extensions.warnColored
import com.javiersc.kotlin.kopy.args.KopyFunctions
import com.javiersc.kotlin.kopy.args.KopyVisibility
import com.javiersc.kotlin.kopy.compiler.KopyCompilerProjectData
import com.javiersc.kotlin.stdlib.remove
import javax.inject.Inject
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.api.provider.ProviderFactory
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption
import org.jetbrains.kotlin.gradle.plugin.getKotlinPluginVersion

public class KopyGradlePlugin
@Inject
constructor(
    private val providers: ProviderFactory,
) : KotlinCompilerPluginSupportPlugin {

    private val Project.kopy: KopyExtension
        get() = the<KopyExtension>()


    override fun apply(target: Project) {
        target.createExtension()
        target.withKotlin {
            suppressKopyOptInt()
            reportKotlinVersion()
        }
        target.withKotlinAndroid { dependencies { "api"(kopyRuntime) } }
        target.withKotlinJvm { dependencies { "api"(kopyRuntime) } }
        target.withKotlinMultiplatform { dependencies { "commonMainApi"(kopyRuntime) } }
    }

    override fun applyToCompilation(
        kotlinCompilation: KotlinCompilation<*>
    ): Provider<List<SubpluginOption>> {
        val kopy: KopyExtension = kotlinCompilation.project.kopy
        return providers.provider {
            listOf(
                SubpluginOption(
                    key = KopyFunctions.NAME,
                    value = kopy.functions.get().value,
                ),
                SubpluginOption(
                    key = KopyVisibility.NAME,
                    value = kopy.visibility.get().value,
                ),
            )
        }
    }

    override fun getCompilerPluginId(): String =
        "${KopyCompilerProjectData.Group}.${KopyCompilerProjectData.Name}"

    override fun getPluginArtifact(): SubpluginArtifact =
        SubpluginArtifact(
            groupId = KopyCompilerProjectData.Group,
            artifactId = KopyCompilerProjectData.Name,
            version = KopyCompilerProjectData.Version,
        )

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean = true

    private fun Project.createExtension() {
        extensions.create<KopyExtension>(KopyExtension.NAME)
    }

    private fun Project.reportKotlinVersion() {
        val kopyVersion: String =
            KopyCompilerProjectData.Version.replaceBeforeLast("+", "").remove("+")
        val kotlinVersion: String = getKotlinPluginVersion()
        if (kopyVersion == kotlinVersion) return
        logger.warnColored {
            "Kopy uses a different Kotlin version, $kopyVersion, than the Kotlin Gradle plugin " +
                "applied to this project, $kotlinVersion. It may not work as expected."
        }
    }

    private fun Project.withKotlinAndroid(action: Project.() -> Unit) {
        pluginManager.withPlugin("org.jetbrains.kotlin.android") { action() }
    }

    private fun Project.withKotlinJvm(action: Project.() -> Unit) {
        pluginManager.withPlugin("org.jetbrains.kotlin.jvm") { action() }
    }

    private fun Project.withKotlinMultiplatform(action: Project.() -> Unit) {
        pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") { action() }
    }

    private fun Project.withKotlin(action: Project.() -> Unit) {
        withKotlinAndroid(action)
        withKotlinJvm(action)
        withKotlinMultiplatform(action)
    }

    private fun Project.suppressKopyOptInt() {
        configure<KotlinProjectExtension> {
            sourceSets.configureEach { sourceSet ->
                sourceSet.languageSettings.optIn("com.javiersc.kotlin.kopy.KopyOptIn")
            }
        }
    }
}

private const val kopyRuntime =
    "com.javiersc.kotlin:kopy-runtime:${KopyCompilerProjectData.Version}"
