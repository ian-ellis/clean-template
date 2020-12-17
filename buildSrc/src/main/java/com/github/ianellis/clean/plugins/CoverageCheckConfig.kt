package com.github.ianellis.clean.plugins

open class CoverageCheckConfig {
    var useDefaultExcludes: Boolean = true
    var sourcePath: String? = null
    var classesPath: String? = null
    var testCommand: String? = null

    var excludes = emptyList<String>()

    val limits = Limits()

    data class Limits(
        var instructions: Float = 100f,
        var branch: Float = 100f,
        var line: Float = 100f,
        var complexity: Float = 100f,
        var method: Float = 100f,
        var clazz: Float = 100f
    )
}
