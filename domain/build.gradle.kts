import com.github.ianellis.clean.Dependencies
import com.github.ianellis.clean.Dependencies.testImplementations
import com.github.ianellis.clean.plugins.CoverageCheckPlugin
import com.github.ianellis.clean.plugins.CoverageCheckConfig

plugins {
    id("java-library")
    id("kotlin")
}
apply<CoverageCheckPlugin>()

java {
    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}

dependencies {
    implementation(Dependencies.Kotlin.stdLib)
    implementation(Dependencies.Kotlin.coroutines)

    testImplementations(Dependencies.Test.Unit.all)
    testImplementation(Dependencies.Test.Android.archCore)
}

configure<CoverageCheckConfig> {
    testCommand = "test"
    sourcePath = "src/main/java"
    classesPath = "$buildDir/classes/kotlin/main"
}
