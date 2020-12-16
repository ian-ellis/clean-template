import com.github.ianellis.clean.Dependencies

plugins {
    id("java-library")
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

dependencies {
    implementation(Dependencies.Kotlin.stdLib)
    implementation(Dependencies.Kotlin.coroutines)
}
