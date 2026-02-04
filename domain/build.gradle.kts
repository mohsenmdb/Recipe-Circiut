import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.android.library)
    alias(libs.plugins.spotless)
}

android {
    namespace = "com.me.recipe.domain"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

spotless {
    kotlin {
        target("src/**/*.kt")
        ktlint()
    }

    kotlinGradle {
        target("*.kts")
        ktlint()
    }
}

dependencies {

    implementation(project(":shared"))
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.compose.runtime)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.atomicfu)
    implementation(libs.timber)

    compileOnly(libs.spotless.gradlePlugin)
}
