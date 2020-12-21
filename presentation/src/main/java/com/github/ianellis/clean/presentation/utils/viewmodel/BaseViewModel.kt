package com.github.ianellis.clean.presentation.utils.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.ianellis.clean.common.coroutines.CoroutineJobManager
import kotlinx.coroutines.CoroutineScope

abstract class BaseViewModel(
    jobManagerFactory: JobManagerFactory = JobManagerFactory()
) : ViewModel() {

    class JobManagerFactory : (CoroutineScope) -> CoroutineJobManager {
        override fun invoke(p1: CoroutineScope): CoroutineJobManager {
            return CoroutineJobManager(p1)
        }
    }

    protected val jobManager: CoroutineJobManager by lazy { jobManagerFactory(this.viewModelScope) }

    override fun onCleared() {
        jobManager.clear()
    }
}
