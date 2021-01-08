import com.github.ianellis.clean.Dependencies
import com.github.ianellis.clean.Dependencies.implementations
import com.github.ianellis.clean.Dependencies.testImplementations
import com.github.ianellis.clean.Sdk

plugins {
    id("com.android.library")
    id("kotlin-android")
    kotlin("kapt")
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

    buildFeatures {
        dataBinding = true
    }
}

dependencies {

    kapt(Dependencies.AnnotationProcessors.dagger)

    implementation(Dependencies.DependencyInjection.daggerAndroid)
    implementation(Dependencies.Kotlin.stdLib)
    implementation(Dependencies.AndroidX.core)
    implementation(Dependencies.AndroidX.appCompat)
    implementations(Dependencies.Network.OkHttp.all)
    implementations(Dependencies.Network.Retrofit.all)

    implementation(project(":data"))

    testImplementations(Dependencies.Test.Unit.all)
    testImplementation(Dependencies.Test.Android.archCore)

    androidTestImplementation(Dependencies.Test.Instrumented.testRunner)
    androidTestImplementation(Dependencies.Test.Instrumented.testRules)
    androidTestImplementation(Dependencies.Test.Instrumented.Espresso.core)
    androidTestImplementation(Dependencies.Test.Instrumented.kluent)
}
