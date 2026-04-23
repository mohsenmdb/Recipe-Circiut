import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlin-parcelize")
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

ksp {
    arg("circuit.codegen.mode", "hilt")
}

android {
    namespace = "com.me.recipe"

    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.mycomposeapplication"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

kotlin {
    jvmToolchain(17)
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}

dependencies {
    with(projects) {
        implementation(shared)
        implementation(data)
        implementation(domain)
        implementation(core)
    }

    implementation(libs.circuit.foundation)
    implementation(libs.circuit.android)
    implementation(libs.circuit.codegen.annotations)
    implementation(libs.circuit.gesturenav)
    implementation(libs.circuit.overlay)
    ksp(libs.circuit.codegen)
    implementation(libs.androidx.appcompat)

    api(platform(libs.compose.bom))
    androidTestImplementation(platform(libs.compose.bom))
    debugImplementation(platform(libs.compose.bom))

    implementation(libs.compose.ui)
    implementation(libs.compose.animation)
    implementation(libs.compose.ui.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.navigation)
    implementation(libs.compose.activity)
    implementation(libs.compose.coil)

    lintChecks(libs.compose.lint.checks)
    implementation(libs.compose.icons.extended)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.hilt.work)
    ksp(libs.androidx.hilt.compiler)

    implementation(libs.datastore.preferences)
    implementation(libs.timber)
    implementation(libs.work)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.paging.compose)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    testImplementation(libs.hilt.testing)
    testImplementation(libs.junit)
    testImplementation(libs.assertk)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.circuit.test)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.robolectric)

    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso)
    androidTestImplementation(libs.androidx.navigation.testing)
}
