package com.github.ianellis.clean

import org.gradle.api.artifacts.dsl.DependencyHandler

private object Versions {
    // https://kotlinlang.org/docs/reference/using-gradle.html
    const val kotlin = "1.4.21"

    // https://github.com/Kotlin/kotlinx.coroutines
    const val kotlinCoroutines = "1.4.1"

    // https://developer.android.com/studio/releases/gradle-plugin
    const val androidPlugin = "4.1.1"
}

object Plugins {
    const val android = "com.android.tools.build:gradle:${Versions.androidPlugin}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"

    // https://docs.gradle.org/current/userguide/jacoco_plugin.html
    const val jacoco = "org.jacoco:org.jacoco.core:0.8.6"
}

object Dependencies {

    object AnnotationProcessors {
        const val dataBinding = "androidx.databinding:databinding-compiler:${Versions.androidPlugin}"
    }

    object Kotlin {
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
    }

    object JsonParsing {

        object Moshi {
            // https://github.com/square/moshi
            private const val version = "1.9.2"
            const val moshi = "com.squareup.moshi:moshi:$version"
            const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:$version"
            const val adapters = "com.squareup.moshi:moshi-adapters:$version"
            const val codegen = "com.squareup.moshi:moshi-kotlin-codegen:$version"
            val kotlin = listOf(
                moshi,
                moshiKotlin
            )
            val all = listOf(
                moshi,
                moshiKotlin,
                adapters
            )
        }
    }

    object Images {
        object Glide {
            // https://github.com/bumptech/glide
            private const val version = "4.11.0"

            const val glide = "com.github.bumptech.glide:glide:$version"
            const val okhttp = "com.github.bumptech.glide:okhttp3-integration:$version"
            val all = listOf(
                glide,
                okhttp
            )
        }
    }

    object Network {
        object OkHttp {
            // https://github.com/square/okhttp
            private const val version = "4.8.1"

            const val okHttp = "com.squareup.okhttp3:okhttp:$version"
            const val interceptor = "com.squareup.okhttp3:logging-interceptor:$version"
            val all = listOf(
                okHttp,
                interceptor
            )
        }

        object Retrofit {
            // https://github.com/square/retrofit
            private const val version = "2.9.0"

            const val retrofit = "com.squareup.retrofit2:retrofit:$version"
            const val moshi = "com.squareup.retrofit2:converter-moshi:$version"
            val all = listOf(
                retrofit,
                moshi
            )
        }
    }

    object Stetho {
        private const val version = "1.5.1"
        const val stetho = "com.facebook.stetho:stetho:$version"
        const val okhttp = "com.facebook.stetho:stetho-okhttp3:$version"
        val all = listOf(
            stetho,
            okhttp
        )
    }

    object AndroidX {
        // https://developer.android.com/jetpack/androidx/releases/appcompat
        const val appCompat = "androidx.appcompat:appcompat:1.1.0"

        // https://developer.android.com/jetpack/androidx/releases/annotation
        const val annotations = "androidx.annotation:annotation:1.1.0"

        // https://developer.android.com/jetpack/androidx/releases/constraintlayout
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"

        // https://developer.android.com/jetpack/androidx/releases/core
        const val core = "androidx.core:core:1.2.0"

        // https://developer.android.com/kotlin/ktx
        const val coreKtx = "androidx.core:core-ktx:1.3.1"

        // https://developer.android.com/jetpack/androidx/releases/recyclerview
        const val recyclerView = "androidx.recyclerview:recyclerview:1.1.0"

        // https://developer.android.com/jetpack/androidx/releases/swiperefreshlayout
        const val swipeRefresh = "androidx.swiperefreshlayout:swiperefreshlayout:1.0.0"

        object Lifecycle {
            // https://developer.android.com/jetpack/androidx/releases/lifecycle
            private const val version = "2.2.0"

            const val common = "androidx.lifecycle:lifecycle-common-java8:$version"
            const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"
            const val runtime = "androidx.lifecycle:lifecycle-runtime:$version"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
        }
    }

    object Google {
        // https://github.com/material-components/material-components-android/releases
        const val material = "com.google.android.material:material:1.2.0"
    }

    object Test {

        object Android {
            const val archCore = "android.arch.core:core-testing:1.1.1"
            const val junit = "androidx.test.ext:junit:1.1.1"
        }

        object Unit {
            const val junit = "junit:junit:4.12"

            // https://github.com/mockk/mockk
            const val mockk = "io.mockk:mockk:1.10.0"

            // https://github.com/MarkusAmshove/Kluent
            const val kluent = "org.amshove.kluent:kluent:1.61"

            const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinCoroutines}"
            val all = listOf(
                junit,
                mockk,
                kluent,
                coroutines
            )
        }

        object Instrumented {
            private const val androidSupportTestVersion = "1.0.2"
            const val supportTestRunner = "com.android.support.test:runner:$androidSupportTestVersion"
            const val supportOrchestrator = "com.android.support.test:orchestrator:$androidSupportTestVersion"

            const val testRunner = "androidx.test:runner:1.2.0"
            const val testRules = "androidx.test:rules:1.2.0"
            const val auiAutomator = "androidx.test.uiautomator:uiautomator:2.2.0"
            const val cucumber = "io.cucumber:cucumber-android:4.3.1"
            const val kluent = "org.amshove.kluent:kluent-android:1.33"
            const val junit = "androidx.test.ext:junit:1.1.1"
            const val fragment = "androidx.fragment:fragment-testing:1.2.5"

            object Espresso {
                // https://developer.android.com/training/testing/set-up-project
                private const val version = "3.2.0"
                const val core = "androidx.test.espresso:espresso-core:$version"
                const val contrib = "androidx.test.espresso:espresso-contrib:$version"
                const val intents = "androidx.test.espresso:espresso-intents:$version"

                private const val idlingResouceVersion = "3.0.2"
                const val idlingResouce = "com.android.support.test.espresso.idling:idling-concurrent:$idlingResouceVersion"
                const val idlingConcurrent = "com.android.support.test.espresso.idling:idling-concurrent:$idlingResouceVersion"
            }
        }
    }

    fun DependencyHandler.`implementations`(dependencyNotation: List<Any>) = dependencyNotation.forEach {
        add("implementation", it)
    }

    fun DependencyHandler.`testImplementations`(dependencyNotation: List<Any>) = dependencyNotation.forEach {
        add("testImplementation", it)
    }

    fun DependencyHandler.`debugImplementations`(dependencyNotation: List<Any>) = dependencyNotation.forEach {
        add("debugImplementation", it)
    }
}

object Sdk {
    const val min = 23
    const val target = 30
    const val compile = 30
    const val buildTools = "29.0.2"
}

object Tools {
    object Detekt {
        // https://github.com/detekt/detekt/releases
        private const val version = "1.14.2"
        const val cli = "io.gitlab.arturbosch.detekt:detekt-cli:$version"
        const val formatting = "io.gitlab.arturbosch.detekt:detekt-formatting:$version"
    }
}
