// app/build.gradle.kts (Module Level)

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.optiroute.com"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.optiroute.com"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.1.new"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // signingConfig = signingConfigs.getByName("release") // Perlu konfigurasi penandatanganan rilis
        }
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8" // Cocokkan dengan versi Compose dan Kotlin Anda
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/LICENSE.md" // Add this line
            excludes += "/META-INF/LICENSE-notice.md" // Add this line
        }
    }
}

dependencies {
    // Core Kotlin Libraries
    implementation("androidx.core:core-ktx:1.13.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3") // Atau versi stabil terbaru
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3") // Untuk await() pada Task Play Services

    // Jetpack Lifecycle Components
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // Jetpack Compose UI Toolkit
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.2.1") // Atau versi stabil terbaru
    implementation("androidx.compose.material:material-icons-core:1.6.7")
    implementation("androidx.compose.material:material-icons-extended:1.6.7")
    implementation("androidx.activity:activity-compose:1.9.0")

    // Navigation Component for Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Accompanist Libraries
    implementation("com.google.accompanist:accompanist-permissions:0.37.3")

    // Room Persistence Library
    implementation("androidx.room:room-runtime:2.6.1")
    implementation(libs.screenshot.validation.junit.engine)
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation(libs.androidx.room.runtime.android)
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // Hilt for Dependency Injection
    implementation("com.google.dagger:hilt-android:2.51.1")
    ksp("com.google.dagger:hilt-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Google Maps SDK for Android
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.maps.android:maps-compose:4.3.3") // Sesuaikan dengan versi terbaru yang stabil
    implementation("com.google.maps.android:maps-compose-utils:4.3.3")
    implementation("com.google.maps.android:maps-compose-widgets:4.3.3")
    implementation("com.google.android.gms:play-services-location:21.2.0") // Untuk FusedLocationProviderClient
    implementation("com.google.android.libraries.places:places:3.4.0")

    // WorkManager (jika VRP berat, saat ini tidak diimplementasikan untuk berjalan di WorkManager)
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.hilt:hilt-work:1.2.0")
    ksp("androidx.hilt:hilt-compiler:1.2.0")

    // Retrofit for Network Requests
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // Gson for JSON Parsing (digunakan di TypeConverters)
    implementation("com.google.code.gson:gson:2.10.1")

    // Timber for logging
    implementation("com.jakewharton.timber:timber:5.0.1")

    // Material 3 dependency
    implementation("com.google.android.material:material:1.10.0")

    // SplashScreen API (for Android 12+)
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Other dependencies
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.mockito:mockito-core:5.5.0")

    // Testing Libraries
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Add constraints to enforce the project's Kotlin version for stdlib
    constraints {
        val projectKotlinVersion = "1.9.22" // Matches version in libs.versions.toml
        implementation("org.jetbrains.kotlin:kotlin-stdlib") {
            version {
                strictly(projectKotlinVersion)
            }
            because("Ensure KSP compatibility by aligning stdlib with project Kotlin version ${projectKotlinVersion}")
        }
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7") {
            version {
                strictly(projectKotlinVersion)
            }
            because("Ensure KSP compatibility by aligning stdlib-jdk7 with project Kotlin version ${projectKotlinVersion}")
        }
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8") {
            version {
                strictly(projectKotlinVersion)
            }
            because("Ensure KSP compatibility by aligning stdlib-jdk8 with project Kotlin version ${projectKotlinVersion}")
        }
    }
}