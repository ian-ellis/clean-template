package com.github.ianellis.clean.common.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class CoroutineRequestManager(
    private val scope: CoroutineScope,
    private val namedRequests: ConcurrentMap<String, Deferred<*>> = ConcurrentHashMap()
) {

    private val mutex = Mutex()

    suspend fun <T> launchIfNotRunning(name: String, clazz: Class<T>, action: suspend CoroutineScope.() -> T): T {
        val key = name + clazz.name
        return getActionAsync(key, action).await()
    }

    private suspend fun <T> getActionAsync(key: String, action: suspend CoroutineScope.() -> T): Deferred<T> = mutex.withLock {
        val existing: Deferred<*>? = namedRequests[key]
        return if (existing.isNullOrComplete()) {
            addAsync(key, action)
        } else {
            existing
        } as Deferred<T>
    }

    private fun <T> addAsync(key: String, action: suspend CoroutineScope.() -> T): Deferred<T> {
        val deferred = scope.async(scope.coroutineContext, CoroutineStart.DEFAULT, action)
        namedRequests[key] = deferred
        return deferred
    }

    private fun Deferred<*>?.isNullOrComplete(): Boolean {
        return this == null || this.isCompleted
    }
}
