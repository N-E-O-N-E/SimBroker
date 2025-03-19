import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.serialization)  // Kotlin Serialization (auch für Navigation benötigt)
    alias(libs.plugins.kotlin.ksp)                      // KSP-Plugin für Room Annotation Processing

}

android {

    namespace = "de.neone.simbroker"
    compileSdk = 35

    defaultConfig {
        applicationId = "de.neone.simbroker"
        minSdk = 30
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        android.buildFeatures.buildConfig = true

        val properties = Properties().apply {
            load(FileInputStream(rootProject.file("local.properties")))
        }

        buildConfigField(
            "String",
            "API_KEY",
            properties.getProperty("API_KEY")
        )

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui.text.google.fonts)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.navigation.compose)    // Navigation in Compose
    implementation(libs.kotlinx.serialization)          // Kotlin Serialization

    implementation(libs.androidx.lifecycle.viewmodel.compose) // Compose ViewModel

    implementation(libs.androidx.datastore.preferences) // DataStore Preferences

    implementation(libs.androidx.room.runtime)          // Room Runtime
    implementation(libs.androidx.room.ktx)              // Room Kotlin Extensions
    ksp(libs.androidx.room.compiler)                    // Room Compiler via KSP

    implementation(libs.moshi)                          // Moshi JSON Parser

    implementation(libs.retrofit)                       // Retrofit HTTP Client
    implementation(libs.converterMoshi)                 // Retrofit Converter für Moshi

    implementation(libs.logging.interceptor)            // HTTP Logging Interceptor

    implementation (libs.androidx.lifecycle.runtime.ktx)// Retrofit
    implementation (libs.kotlinx.coroutines.core)       // Retrofit
    implementation (libs.kotlinx.coroutines.android)    // Retrofit

    // --- Koin (Dependency Injection) ---
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

}