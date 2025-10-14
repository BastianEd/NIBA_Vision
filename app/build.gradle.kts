plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // Puedes mantener el compose plugin si lo quieres
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.niba_vision"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.niba_vision"
        minSdk = 33
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    // Mantengo Java/Kotlin 11 como pediste
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures { compose = true }

    // Si usas el plugin kotlin.compose, puedes omitir esta línea.
    // Si la quieres dejar, que sea coherente con el BOM que uses.
    composeOptions { kotlinCompilerExtensionVersion = "1.5.8" }
}

dependencies {
    // ===== Compose BOM (centraliza versiones) =====
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))

    // ===== Compose UI / Material 3 (sin versiones fijas) =====
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)


    // ===== Navigation + Lifecycle para Compose =====
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.navigation:navigation-compose:2.8.3")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.6")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0-rc02")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")


    // ===== Core / runtime =====
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // ===== Testing =====
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
