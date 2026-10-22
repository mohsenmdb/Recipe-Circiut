import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.me.recipe.data"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

dependencies {
    with(projects) {
        implementation(shared)
        implementation(network)
        implementation(domain)
        implementation(cache)
    }

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.timber)
    implementation(libs.squareup.okhttp)
    implementation(libs.androidx.paging.paging.common)
    implementation(libs.threeTenAbp)
    api(libs.store5)
}
