import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.me.recipe.core"
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

dependencies {
    api(platform(libs.compose.bom))
    implementation(libs.compose.runtime)

    implementation(libs.datastore.preferences)
    implementation(libs.kotlinx.collections.immutable)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
}
