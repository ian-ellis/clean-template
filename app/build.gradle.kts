import com.github.ianellis.clean.Dependencies
import com.github.ianellis.clean.Dependencies.testImplementations
import com.github.ianellis.clean.Sdk

plugins {
    id("com.android.application")
    id("kotlin-android")
    kotlin("kapt")
}

android {
    compileSdkVersion(Sdk.compile)
    buildToolsVersion(Sdk.buildTools)

    defaultConfig {
        applicationId = "com.github.ianellis.clean"
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
    kapt(Dependencies.AnnotationProcessors.dataBinding)
    kapt(Dependencies.AnnotationProcessors.dagger)
    kaptAndroidTest(Dependencies.AnnotationProcessors.dataBinding)

    implementation(project(":app-framework"))
    implementation(project(":app-presentation"))
    implementation(project(":presentation"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":common"))

    implementation(Dependencies.DependencyInjection.daggerAndroid)
    implementation(Dependencies.DependencyInjection.daggerAndroidSupport)
    implementation(Dependencies.Kotlin.stdLib)
    implementation(Dependencies.AndroidX.core)
    implementation(Dependencies.AndroidX.appCompat)
    implementation(Dependencies.AndroidX.constraintLayout)
    implementation(Dependencies.Google.material)

    testImplementations(Dependencies.Test.Unit.all)
    testImplementation(Dependencies.Test.Android.archCore)

    androidTestImplementation(Dependencies.Test.Instrumented.testRunner)
    androidTestImplementation(Dependencies.Test.Instrumented.testRules)
    androidTestImplementation(Dependencies.Test.Instrumented.Espresso.core)
    androidTestImplementation(Dependencies.Test.Instrumented.kluent)
}
