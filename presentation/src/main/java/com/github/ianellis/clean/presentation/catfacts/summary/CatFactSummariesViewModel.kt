package com.github.ianellis.clean.presentation.catfacts.summary

import androidx.lifecycle.LiveData
import com.ianellis.github.clean.domain.common.catfacts.summary.CatFactSummary

interface CatFactSummariesViewModel {
    val loading: LiveData<Boolean>
    val error: LiveData<Throwable?>
    val summaries: LiveData<List<CatFactSummary>>
    fun selected(summary: CatFactSummary)
    fun refresh()
}
