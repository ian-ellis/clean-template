import com.github.ianellis.clean.Dependencies
import com.github.ianellis.clean.Dependencies.testImplementations
import com.github.ianellis.clean.plugins.CoverageCheckPlugin
import com.github.ianellis.clean.plugins.CoverageCheckConfig

plugins {
    id("java-library")
    id("kotlin")
    kotlin("kapt")
}
apply<CoverageCheckPlugin>()

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

dependencies {
    kapt(Dependencies.AnnotationProcessors.dagger)

    implementation(project(":data"))

    implementation(Dependencies.DependencyInjection.dagger)
    implementation(Dependencies.Kotlin.stdLib)
    implementation(Dependencies.Kotlin.coroutines)

    testImplementations(Dependencies.Test.Unit.all)
    testImplementation(Dependencies.Test.Android.archCore)
}

configure<CoverageCheckConfig> {
    testCommand = "test"
    sourcePath = "src/main/java"
    classesPath = "$buildDir/classes/kotlin/main"
    excludes = listOf(
        "**/CatFactSummary.class"
    )
}
