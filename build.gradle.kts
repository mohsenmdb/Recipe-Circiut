import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.spotless)
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            ktlint()
        }
        kotlinGradle {
            target("**/*.kts")
            ktlint()
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            if (project.findProperty("recipeApp.enableComposeCompilerReports") == "true") {
                val metricsPath =
                    layout.buildDirectory.dir("compose_metrics").get().asFile.absolutePath
                freeCompilerArgs.addAll(
                    listOf(
                        "-P",
                        "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=$metricsPath"
                    )
                )
                freeCompilerArgs.addAll(
                    listOf(
                        "-P",
                        "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=$metricsPath"
                    )
                )
            }
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}

tasks.named<Delete>("clean") {
    delete(layout.buildDirectory)
}
