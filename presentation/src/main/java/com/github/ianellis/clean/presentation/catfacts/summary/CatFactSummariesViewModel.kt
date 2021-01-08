package com.github.ianellis.clean.presentation.catfacts.summary

import androidx.lifecycle.LiveData
import com.github.ianellis.clean.presentation.utils.navigator.Navigator
import com.ianellis.github.clean.domain.common.catfacts.summary.CatFactSummary

interface CatFactSummariesViewModel {
    val navigator: Navigator
    val loading: LiveData<Boolean>
    val error: LiveData<Throwable?>
    val summaries: LiveData<List<CatFactSummary>>
    fun selected(summary: CatFactSummary)
    fun refresh()
}
