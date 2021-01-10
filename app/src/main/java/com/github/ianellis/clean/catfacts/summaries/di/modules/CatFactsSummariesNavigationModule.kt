package com.github.ianellis.clean.catfacts.summaries.di.modules

import com.github.ianellis.clean.catfacts.summaries.AppCatFactSummariesNavigator
import com.github.ianellis.clean.presentation.catfacts.summary.CatFactSummaryNavigator
import dagger.Module
import dagger.Provides

@Module
class CatFactsSummariesNavigationModule {

    @Provides
    fun providesCatFactSummariesNavigator(): CatFactSummaryNavigator = AppCatFactSummariesNavigator()
}
