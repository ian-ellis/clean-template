package com.github.ianellis.clean.presentation.catfacts.summary

import com.github.ianellis.clean.presentation.utils.livedata.mutableLiveData
import com.github.ianellis.clean.presentation.utils.viewmodel.BaseViewModel
import com.ianellis.github.clean.domain.common.catfacts.summary.CatFactSummary
import com.ianellis.github.clean.domain.common.catfacts.summary.GetCatFactSummaries

internal class SimpleCatFactSummariesViewModel(
    private val getCatFactSummaries: GetCatFactSummaries,
    private val navigator: CatFactSummaryNavigator,
) : BaseViewModel(), CatFactSummariesViewModel {

    override val loading = mutableLiveData(false)
    override val error = mutableLiveData<Throwable?>(null)
    override val summaries = mutableLiveData<List<CatFactSummary>>(emptyList())

    init {
        refresh()
    }

    override fun selected(summary: CatFactSummary) {
        navigator.summarySelected(summary)
    }

    override fun refresh() {

        jobManager.launchIfNotRunning(JOB_LOAD) {
            error.postValue(null)
            loading.postValue(true)
            try {
                summaries.postValue(getCatFactSummaries.getSummaries())
            } catch (e: Exception) {
                error.postValue(e)
            } finally {
                loading.postValue(false)
            }
        }
    }

    companion object {
        private const val JOB_LOAD = "LOAD"
    }
}
