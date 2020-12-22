package com.github.ianellis.clean.plugins

import groovy.util.Node
import groovy.util.XmlParser
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.task
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import kotlin.math.roundToInt

open class CoverageCheckPlugin : Plugin<Project> {

    private val excludes = listOf(
        // generated classes
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/*_Factory*.class",
        "**/*_MembersInjector*.class",
        "**/BR.class",
        // dependency injection
        "**/di/**",
        // android classes
        "**/Manifest*.*",
        "android/**",
        "**/*Activity.class",
        "**/*Activity$*.class",
        "**/*Dialog.class",
        "**/*Dialog$*.class",
        "**/*Fragment.class",
        "**/*Fragment$*.class",
        "**/*ViewPager.class",
        "**/framework/**",
        // android sdk
        "com/android/**",
        // data bindings
        "**/databinding/**",
        "**/bindings/**",
        "**/*Decoration.class",
        "**/*Decoration$*.class",
        "**/*Bindings.class",
        "**/*Bindings$*.class",
        // common presentation class suffixes
        "**/*Decoration.class",
        "**/*Diff.class",
        "**/*Recycler*.class",
        "**/*RecyclerView*.class",
        "**/*Recycler$*.class",
        "**/*RecyclerView$*.class",
        "**/*Adapter.class",
        "**/*Adapter$*.class",
        "**/*Layout*.class",
        "**/*ItemHolder*.class",
        "**/*ViewHolder*.class",
        "**/*DiffUtil*.class",
        "**/*Navigator*.class",
        // exceptions
        "**/*Exception.class",
        // Nav components
        "**/*Directions.class",
        "**/*Directions$*.class"
    )

    override fun apply(target: Project) {
        target.pluginManager.apply("jacoco")

        val extension = target.extensions.create("coverageCheck", CoverageCheckConfig::class.java)
        val reportsPath = "${target.buildDir}/reports/jacoco"

        target.extensions.configure(JacocoPluginExtension::class.java) {
            toolVersion = "0.8.6"
        }

        target.afterEvaluate {
            val coverage = target.task("coverage", JacocoReport::class, configure(extension, target, reportsPath))
            target.task("CoverageCheck")
                .dependsOn(coverage)
                .doLast {
                    val report = target.file("$reportsPath/coverage/coverage.xml")

                    val parser = XmlParser()
                    parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
                    parser.setFeature("http://apache.org/xml/features/disallow-doctype-decl", false)

                    val results: Node = parser.parse(report)
                    val counters: List<Node> = results.children().filter { it is Node && it.name() == "counter" }.map { it as Node }

                    val htmlReportLocation = target.file("$reportsPath/html")
                    logger.lifecycle("Checking coverage results: $htmlReportLocation/index.html")

                    val result = Result()
                    result.update("Instructions", extension.limits.instructions, counters.getCoveragePercentage("INSTRUCTION"))
                    result.update("Branch", extension.limits.branch, counters.getCoveragePercentage("BRANCH"))
                    result.update("Line", extension.limits.line, counters.getCoveragePercentage("LINE"))
                    result.update("Complexity", extension.limits.complexity, counters.getCoveragePercentage("COMPLEXITY"))
                    result.update("Method", extension.limits.method, counters.getCoveragePercentage("METHOD"))
                    result.update("Class", extension.limits.clazz, counters.getCoveragePercentage("CLASS"))

                    result.print(logger)

                    if (!result.passed()) {
                        throw GradleException("Failed Coverage Check")
                    }
                }
        }
    }

    private fun configure(
        extension: CoverageCheckConfig,
        target: Project,
        reportsPath: String
    ): JacocoReport.() -> Unit {
        return {
            extension.validate()
            dependsOn(extension.testCommand)
            group = "Reporting"
            description = "Generate Jacoco Coverage Reports"

            classDirectories.setFrom(
                target.fileTree(
                    mapOf(
                        "dir" to extension.classesPath,
                        "excludes" to if (extension.useDefaultExcludes) {
                            excludes + extension.excludes
                        } else {
                            extension.excludes
                        }
                    )
                )
            )
            sourceDirectories.setFrom(
                target.file(extension.sourcePath!!)
            )
            additionalSourceDirs.setFrom(
                target.file(extension.sourcePath!!)
            )
            executionData.setFrom(target.files("${target.buildDir}/jacoco/${extension.testCommand}.exec"))
            reports {
                xml.isEnabled = true
                html.isEnabled = true
                html.destination = target.file("$reportsPath/html")
            }
        }
    }

    private fun CoverageCheckConfig.validate() {
        val errorMessages = mutableListOf<String>()
        if (this.testCommand == null) {
            errorMessages.add(
                "You Must Provide an test command, for java modules `test` for android modules `testDebug` for example"
            )
        }

        if (this.sourcePath == null) {
            errorMessages.add(
                "You Must Provide a path to the source files, it should be relative your module: eg `src/main/java`"
            )
        }

        if (this.classesPath == null) {
            errorMessages.add(
                "You Must Provide a path to the class files, it should be relative your module: eg `\$buildDir/classes/kotlin/main`"
            )
        }

        if (errorMessages.isNotEmpty()) {
            throw IllegalStateException(
                "CoverageCheck is not configured correctly:\n ${errorMessages.joinToString("\n")}"
            )
        }
    }

    private fun List<Node>.getCoveragePercentage(key: String): Float {
        return this.firstOrNull { it.attribute("type") == key }.percentage()
    }

    @Suppress("MagicNumber")
    private fun Node?.percentage(): Float {
        return if (this == null) {
            100f
        } else {
            val missed = (this.attribute("missed") as String).toFloat()
            val covered = (this.attribute("covered") as String).toFloat()
            ((covered / (covered + missed)) * 100).round(2)
        }
    }

    @Suppress("MagicNumber")
    private fun Float.round(decimals: Int): Float {
        var multiplier = 1.0f
        repeat(decimals) { multiplier *= 10 }
        return (this * multiplier).roundToInt() / multiplier
    }

    private class Result(
        val failures: MutableList<String> = mutableListOf(),
        val passes: MutableList<String> = mutableListOf()
    ) {
        fun update(
            name: String,
            limit: Float,
            value: Float
        ) {
            if (value < limit) {
                failures.add("- $name coverage rate is: $value%, minimum is $limit%")
            } else {
                passes.add("- $name coverage rate is: $value%, minimum is $limit%")
            }
        }

        fun passed(): Boolean = failures.isEmpty()
        fun print(logger: Logger) {
            if (passed()) {
                logger.quiet("****************** Code Coverage Passed ***********************")
                passes.forEach {
                    logger.quiet(it)
                }
                logger.quiet("***************************************************************")
            } else {
                logger.quiet("****************** Code Coverage Failed ***********************")
                failures.forEach {
                    logger.quiet(it)
                }
                logger.quiet("***************************************************************")
            }
        }
    }
}
