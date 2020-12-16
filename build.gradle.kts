// Top-level build file where you can add configuration options common to all sub-projects/modules.
apply {
    from("detekt/detekt.gradle")
}

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(com.github.ianellis.clean.Plugins.android)
        classpath(com.github.ianellis.clean.Plugins.kotlin)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
