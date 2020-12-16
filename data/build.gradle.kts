import com.github.ianellis.clean.Dependencies

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
    implementation(Dependencies.JsonParsing.Moshi.moshi)
    implementation(Dependencies.Kotlin.stdLib)
    implementation(Dependencies.Kotlin.coroutines)
}
