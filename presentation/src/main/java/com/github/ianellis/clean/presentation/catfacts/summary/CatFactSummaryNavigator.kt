package com.github.ianellis.clean.presentation.catfacts.summary

import com.github.ianellis.clean.presentation.utils.navigator.Navigator
import com.ianellis.github.clean.domain.common.catfacts.summary.CatFactSummary

interface CatFactSummaryNavigator : Navigator {
    fun summarySelected(summary: CatFactSummary)
}
