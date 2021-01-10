package com.github.ianellis.clean.presentation.di

import androidx.lifecycle.ViewModel
import kotlin.reflect.KClass
import dagger.MapKey

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)
