package com.github.ianellis.clean.common.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class CoroutineJobManager(
    private val scope: CoroutineScope,
    private val namedJobs: ConcurrentMap<String, Job> = ConcurrentHashMap()
) {

    fun launchIfNotRunning(name: String, action: suspend CoroutineScope.() -> Unit) {
        if (namedJobs[name]?.isActive != true) {
            launchAndSave(name, action)
        }
    }

    fun cancelAndLaunch(name: String, action: suspend CoroutineScope.() -> Unit) {
        val existingJob: Job? = namedJobs[name]
        if (existingJob?.isCompleted == false) {
            existingJob.cancel()
        }
        launchAndSave(name, action)
    }

    fun clear() {
        scope.cancel()
        namedJobs.clear()
    }

    private fun launchAndSave(name: String, action: suspend CoroutineScope.() -> Unit) {
        val job = scope.launch(scope.coroutineContext, CoroutineStart.DEFAULT, action)
        namedJobs[name] = job
    }
}
