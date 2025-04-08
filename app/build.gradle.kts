plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
}

android {
    namespace = "com.example.parchadosapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.parchadosapp"
        minSdk = 26
        targetSdk = 33
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
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    packaging {
        // Excluir archivos duplicados que causan el conflicto
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        // Agregar la opción para excluir el archivo META-INF/DEPENDENCIES
        resources {
            excludes += "/META-INF/DEPENDENCIES"
        }
    }
}

dependencies {
    // Dependencias de AndroidX
    implementation("androidx.core:core-ktx:1.9.0") // No es necesario duplicarla
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-runtime-ktx:2.8.8")
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Dependencias para pruebas
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Dependencias de mapas y servicios de ubicación
    implementation("com.google.android.gms:play-services-maps:18.1.0") // Solo esta versión
    implementation("com.google.android.gms:play-services-location:21.0.1") // Solo esta versión
    implementation("com.google.android.gms:play-services-base:17.6.0")
    implementation("com.google.android.gms:play-services-auth:20.0.1")
    implementation("com.google.maps.android:maps-compose:2.1.0")


    // Retrofit y Gson
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Otras dependencias de Google
    implementation("com.google.api-client:google-api-client-android:1.34.0")
    implementation("com.google.apis:google-api-services-calendar:v3-rev411-1.25.0")
    implementation("com.google.http-client:google-http-client-gson:1.34.0")

    // Otras dependencias necesarias
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Compact Calendar
    implementation("com.github.sundeepk:compact-calendar-view:3.0.0") {
        exclude(group = "com.android.support", module = "appcompat-v7")
        exclude(group = "com.android.support", module = "support-compat")
        exclude(group = "com.android.support", module = "support-fragment")
    }

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("io.github.jan-tennert.supabase:postgrest-kt:2.0.3")
    implementation("io.github.jan-tennert.supabase:realtime-kt:2.0.3")
    implementation("io.github.jan-tennert.supabase:supabase-kt:2.0.3")
    implementation("io.ktor:ktor-client-okhttp:2.3.4")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4")



}
