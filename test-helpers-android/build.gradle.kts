import com.github.ianellis.clean.Dependencies
import com.github.ianellis.clean.Sdk

plugins {
    id("com.android.library")
    id("kotlin-android")
}

android {
    compileSdkVersion(Sdk.compile)
    buildToolsVersion(Sdk.buildTools)

    defaultConfig {
        minSdkVersion(Sdk.min)
        targetSdkVersion(Sdk.target)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
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
}

dependencies {

    implementation(Dependencies.Kotlin.stdLib)
    implementation(Dependencies.AndroidX.appCompat)
    implementation(Dependencies.AndroidX.Lifecycle.viewModel)

    implementation(Dependencies.Test.Instrumented.testRunner)
    implementation(Dependencies.Test.Instrumented.testRules)
    implementation(Dependencies.Test.Instrumented.Espresso.core)
    implementation(Dependencies.Test.Instrumented.auiAutomator)

    implementation(Dependencies.Test.Unit.coroutines)
}
