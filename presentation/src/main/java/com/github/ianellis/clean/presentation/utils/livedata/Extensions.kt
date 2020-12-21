package com.github.ianellis.clean.presentation.utils.livedata

import androidx.lifecycle.MutableLiveData

fun <T> mutableLiveData(initialValue: T): MutableLiveData<T> {
    val liveData = MutableLiveData<T>()
    liveData.value = initialValue
    return liveData
}
