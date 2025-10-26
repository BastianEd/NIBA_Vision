// Define los plugins necesarios para una aplicación Android con Kotlin y Compose.
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
}

// Configuración específica del módulo de la aplicación Android.
android {
    namespace = "com.example.niba_vision"
    compileSdk = 36 // La versión del SDK contra la que se compila el proyecto.

    defaultConfig {
        applicationId = "com.example.niba_vision"
        minSdk = 28 // La versión mínima de Android requerida para ejecutar la app.
        targetSdk = 36 // La versión de Android para la que la app fue probada.
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Configuración para las compilaciones de lanzamiento (release).
    buildTypes {
        release {
            isMinifyEnabled = false // Desactiva la ofuscación de código.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // Opciones de compilación para Java y Kotlin.
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    // Habilita el uso de Jetpack Compose.
    buildFeatures { compose = true }

    // Opciones específicas del compilador de Compose.
    composeOptions { kotlinCompilerExtensionVersion = "1.5.8" }
}

// Lista de dependencias del proyecto.
dependencies {
    // ===== Compose BOM (Bill of Materials) =====
    // Centraliza las versiones de las librerías de Compose para asegurar la compatibilidad.
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))

    // ===== Dependencias de Jetpack Compose =====
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3) // Material Design 3.
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)



    // ===== Dependencias de Navegación y Ciclo de Vida =====
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.navigation:navigation-compose:2.8.3") // Navegación en Compose.
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0-rc02")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    // ICONOS
    implementation("androidx.compose.material:material-icons-extended:1.6.8") // O la versión más reciente
    //  COIL (CARGA DE IMÁGENES)
    implementation("io.coil-kt:coil-compose:2.6.0")

    // ===== Core de AndroidX y Kotlin =====
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // ===== Dependencias de Testing =====
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // ===== Dependencías de SQLite =====
    val room_version = "2.8.3"

    implementation("androidx.room:room-runtime:$room_version")

    // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
    // See Add the KSP plugin to your project
    ksp("androidx.room:room-compiler:$room_version")
    // If this project only uses Java source, use the Java annotationProcessor
    // No additional plugins are necessary
    annotationProcessor("androidx.room:room-compiler:$room_version")
}