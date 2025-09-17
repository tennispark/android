plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.compose.compiler) apply false
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services") version "4.4.3" apply false
    // Add Hilt plugin
    id("com.google.dagger.hilt.android") version "2.48" apply false
}
