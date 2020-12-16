import com.github.ianellis.clean.Dependencies
import com.github.ianellis.clean.Dependencies.testImplementations

plugins {
    id("java-library")
    id("kotlin")
    kotlin("kapt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

dependencies {
    kapt(Dependencies.JsonParsing.Moshi.codegen)

    testImplementations(Dependencies.Test.Unit.all)

    implementation(Dependencies.JsonParsing.Moshi.moshi)
    implementation(Dependencies.Kotlin.stdLib)
    implementation(Dependencies.Kotlin.coroutines)
}
