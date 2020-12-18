package com.github.ianellis.clean.common.coroutines

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestCoroutineScope
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldNotEqual
import org.junit.Before
import org.junit.Test

class CoroutineJobManagerSpec {

    private lateinit var manager: CoroutineJobManager
    private lateinit var scope: TestCoroutineScope

    @Before
    fun setup() {
        scope = TestCoroutineScope()
        manager = CoroutineJobManager(scope)
    }

    @Test
    fun `launchIfNotRunning() - allows only one job with provided name to run`() {
        val fun1 = mockFunction()
        val fun2 = mockFunction()
        val fun3 = mockFunction()
        val name = "LOAD"
        // when we call launchIfNotRunning initially
        manager.launchIfNotRunning(name) {
            fun1()
            delay(1)
        }

        // then our action is invoked
        verify(exactly = 1) { fun1() }

        // when we try again
        manager.launchIfNotRunning(name) {
            fun2()
            delay(1)
        }
        // and time passes
        scope.advanceTimeBy(1)

        // then the second action is not run
        verify(exactly = 0) { fun2() }

        // and we try again
        manager.launchIfNotRunning(name) {
            fun3()
            delay(1)
        }

        // and time passes
        scope.advanceTimeBy(1)

        verify(exactly = 1) { fun3() }
    }

    @Test
    fun `cancelAndLaunch() - cancels any existing jobs and runs new job`() {
        val fun1 = mockFunction()
        val fun2 = mockFunction()
        val fun3 = mockFunction()
        val fun4 = mockFunction()
        val name = "LOAD"
        // when we call cancelAndLaunch initially
        manager.cancelAndLaunch(name) {
            fun1()
            delay(1)
            fun2()
        }

        // then our action is invoked, but suspended
        verify(exactly = 1) { fun1() }
        verify(exactly = 0) { fun2() }

        // when we try again before the first has completed
        manager.cancelAndLaunch(name) {
            fun3()
            delay(1)
            fun4()
        }

        // then our first action ius dropped, nothing after its suspension point is run
        verify(exactly = 1) { fun1() }
        verify(exactly = 0) { fun2() }

        // and our second is run, up to its suspension point
        verify(exactly = 1) { fun3() }
        verify(exactly = 0) { fun4() }

        // when time passes
        scope.advanceTimeBy(1)

        // then the second action continues
        verify(exactly = 1) { fun4() }

        // the first action never completes
        verify(exactly = 0) { fun2() }
    }

    @Test
    fun `cancelAndLaunch() - runs new job immediately if previous job finished`() {
        val fun1 = mockFunction()
        val fun2 = mockFunction()
        val name = "LOAD"

        // when we call cancelAndLaunch initially
        manager.cancelAndLaunch(name) {
            fun1()
        }
        // and the action is run, and reaches the first suspension point
        scope.advanceUntilIdle()

        // then our action is invoked
        verify(exactly = 1) { fun1() }

        // when we try again, after the first one has finished
        manager.cancelAndLaunch(name) {
            fun2()
        }
        // and the action is run
        scope.advanceUntilIdle()

        // then our second action is also run
        verify(exactly = 1) { fun2() }
    }

    @Test
    fun `clear() - clears cache of named jobs`() {
        val jobCache: ConcurrentHashMap<String, Job> = ConcurrentHashMap()
        val name = "LOAD"
        manager = CoroutineJobManager(CoroutineScope(Dispatchers.Unconfined), jobCache)

        // when we call cancelAndLaunch initially
        manager.cancelAndLaunch(name) {
            delay(1)
        }
        // then we have aved the job in the cache
        jobCache[name] shouldNotEqual null

        // when we clear
        manager.clear()

        // then the cache is empty
        jobCache.isEmpty() shouldBe true
    }

    @Test
    fun `clear() - cancels main jobs`() {
        val job = Job()
        val context = Dispatchers.Unconfined + job
        manager = CoroutineJobManager(CoroutineScope(context))

        job.isCancelled shouldBe false

        // when we clear
        manager.clear()

        // then the cache is empty
        job.isCancelled shouldBe true
    }

    @Test
    fun `clear() - cancels named jobs`() {
        val jobCache: ConcurrentHashMap<String, Job> = ConcurrentHashMap()
        val name = "LOAD"
        manager = CoroutineJobManager(CoroutineScope(TestCoroutineScope().coroutineContext + Job()), jobCache)

        // when we call cancelAndLaunch initially
        manager.cancelAndLaunch(name) {
            delay(1)
        }

        // then we have have the job in the cache
        val cachedJob = jobCache[name]
        cachedJob shouldNotEqual null

        // when we clear
        manager.clear()

        // then the cache is empty
        cachedJob!!.isCancelled shouldBe true
    }

    private fun mockFunction(): () -> Unit {
        val function: () -> Unit = mockk()
        every { function() } just Runs
        return function
    }
}
