package com.github.ianellis.clean.presentation.utils.livedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Rule
import org.junit.Test

class ExtensionsSpec {

    @Rule
    fun rule() = InstantTaskExecutorRule()

    @Test
    fun `mutableLiveData() created MutableLiveData with initial value`() {
        val initialValue = "foo"
        val result = mutableLiveData(initialValue)
        result.value shouldBeEqualTo initialValue
    }
}
