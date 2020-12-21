package com.github.ianellis.clean.common.coroutines

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

class CoroutineRequestManagerSpec {
    private lateinit var manager: CoroutineRequestManager
    private lateinit var scope: TestCoroutineScope

    @Before
    fun setup() {
        scope = TestCoroutineScope()
        manager = CoroutineRequestManager(scope)
    }

    @Test
    fun `launchIfNotRunning - executes provided action and returns result`() = runBlockingTest {
        val result = "bar"
        manager.launchIfNotRunning("FOO", String::class.java) {
            result
        } shouldBeEqualTo result
    }

    @Test
    fun `launchIfNotRunning - returns result of in progress job with matching name and type`() = runBlockingTest {
        val name = "foo"
        val result1 = "bar"
        val result2 = "baz"
        val task1 = async {
            manager.launchIfNotRunning(name, String::class.java) {
                delay(1000)
                result1
            }
        }
        val task2 = async {
            manager.launchIfNotRunning(name, String::class.java) {
                delay(1000)
                result2
            }
        }
        scope.advanceTimeBy(2000)

        val results = listOf(task1, task2).awaitAll()
        results.first() shouldBeEqualTo result1
        results.last() shouldBeEqualTo result1
    }

    @Test
    fun `launchIfNotRunning - returns new task if previous completed`() = runBlockingTest {
        val name = "foo"
        val result1 = "bar"
        val result2 = "baz"
        println("Running test")
        val task1 = async {
            println("   task1")
            manager.launchIfNotRunning(name, String::class.java) {
                println("       task1 running")
                delay(1000)
                println("       task1 delay complete")
                result1
            }
        }
        println("task1 returned")
        scope.advanceTimeBy(2000)
        println("2 seconds later")
        val task2 = async {
            println("   task2")
            manager.launchIfNotRunning(name, String::class.java) {
                println("       task2 running")
                delay(1000)
                println("       task2 delay complete")
                result2
            }
        }
        println("task2 returned")
        scope.advanceTimeBy(2000)
        println("4 seconds later")

        val results = listOf(task1, task2).awaitAll()
        results.first() shouldBeEqualTo result1
        results.last() shouldBeEqualTo result2
    }

    @Test
    fun `launchIfNotRunning - runs tasks concurrently if different types but same key`() = runBlockingTest {
        val name = "foo"
        val result1 = "bar"
        val result2 = 1
        val task1 = async {
            manager.launchIfNotRunning(name, String::class.java) {
                delay(1000)
                result1
            }
        }
        val task2 = async {
            manager.launchIfNotRunning(name, Int::class.java) {
                delay(1000)
                result2
            }
        }
        scope.advanceTimeBy(2000)

        val results = listOf(task1, task2).awaitAll()
        results.first() shouldBeEqualTo result1
        results.last() shouldBeEqualTo result2
    }
}
