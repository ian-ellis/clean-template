package com.github.ianellis.clean.presentation.utils.navigator

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import java.lang.ref.WeakReference
import kotlin.reflect.KClass

abstract class AbstractLifecycleOwnerManager<T>(
    private val clazz: KClass<T>,
    private val weakReferenceFactory: (LifecycleOwner?) -> WeakReference<LifecycleOwner?> = ::WeakReference
) : LifecycleObserver where T : Any, T : LifecycleOwner {

    private var lifecycleOwnerReference: WeakReference<LifecycleOwner?> = WeakReference(null)

    private fun setLifecycleOwner(value: LifecycleOwner) {
        if (!clazz.isInstance(value)) {
            throw IllegalStateException("lifecycleOwner must be ${clazz.java.name} is ${value::class.java.name}")
        }
        lifecycleOwnerReference = weakReferenceFactory.invoke(value)
    }

    @Suppress("UNCHECKED_CAST")
    protected val owner: T?
        get() = lifecycleOwnerReference.get() as? T

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart(owner: LifecycleOwner) {
        setLifecycleOwner(owner)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner) {
        lifecycleOwnerReference.clear()
        owner.lifecycle.removeObserver(this)
    }
}
