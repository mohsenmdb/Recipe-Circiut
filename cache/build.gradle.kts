import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.android.library)
    alias(libs.plugins.spotless)
}

android {
    namespace = "com.me.recipe.cache"
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
    implementation(project(":domain"))

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)
    implementation(libs.kotlinx.collections.immutable)

    compileOnly(libs.spotless.gradlePlugin)
}
