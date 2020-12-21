package com.github.ianellis.clean.presentation.utils.viewmodel

import androidx.lifecycle.viewModelScope
import com.github.ianellis.clean.common.coroutines.CoroutineJobManager
import io.mockk.CapturingSlot
import io.mockk.MockKGateway
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkConstructor
import kotlinx.coroutines.CoroutineScope
import org.amshove.kluent.shouldBeEqualTo
import org.junit.After
import org.junit.Test

class BaseViewModelSpec {

    class TestViewModel(factory: JobManagerFactory) : BaseViewModel(factory) {
        fun getProtectedJobManager(): CoroutineJobManager = jobManager
        fun invokeOnCleared() = this.onCleared()
    }

    class DefaultTestViewModel : BaseViewModel() {
        fun getProtectedJobManager(): CoroutineJobManager = jobManager
    }

    @After
    fun teardown() {
        unmockkConstructor(CoroutineJobManager::class)
    }

    @Test
    fun `BaseViewModel - creates job manager using factory`() {
        val factory = mockk<BaseViewModel.JobManagerFactory>()
        val manager = mockk<CoroutineJobManager>()
        every { factory.invoke(any()) } returns manager

        val vm = TestViewModel(factory)

        vm.getProtectedJobManager() shouldBeEqualTo manager
    }

    @Test
    fun `BaseViewModel - viewModelScope as scope`() {
        val factory = mockk<BaseViewModel.JobManagerFactory>()
        val slot = CapturingSlot<CoroutineScope>()

        val manager = mockk<CoroutineJobManager>()
        every { factory.invoke(capture(slot)) } returns manager

        val vm = TestViewModel(factory)
        vm.getProtectedJobManager()

        slot.captured shouldBeEqualTo vm.viewModelScope
    }

    @Test
    fun `BaseViewModel - default constructor uses viewModelScope`() {
        mockkConstructor(CoroutineJobManager::class)

        val vm = DefaultTestViewModel()
        val manager = vm.getProtectedJobManager()
        MockKGateway.implementation().mockFactory.isMock(manager) shouldBeEqualTo true
    }

    @Test
    fun `onCleared() - cancels jobs`() {
        val factory = mockk<BaseViewModel.JobManagerFactory>()
        val manager = mockk<CoroutineJobManager>(relaxed = true)
        every { factory.invoke(any()) } returns manager
        val vm = TestViewModel(factory)
        vm.invokeOnCleared()
        coVerify(exactly = 1) { manager.clear() }
    }
}
