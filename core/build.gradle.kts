plugins {
    alias(libs.plugins.kotlin.android)
    id("com.android.library")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.luckydut97.tennispark.core"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("boolean", "IS_DEBUG", "true")
        }
        release {
            isMinifyEnabled = false
            buildConfigField("boolean", "IS_DEBUG", "false")
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
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.6"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.android)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.gson)

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.16.0"))
    implementation("com.google.firebase:firebase-messaging")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Compose
    implementation("androidx.compose.ui:ui:1.5.8")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.8")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.activity:activity-compose:1.8.2")

    // DataStore (토큰 저장용)
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // 이미지 로딩 라이브러리
    implementation("io.coil-kt:coil-compose:2.4.0")

    // 테스트 의존성
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
