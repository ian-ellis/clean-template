package com.github.ianellis.clean.presentation.catfacts.summary.di

import androidx.lifecycle.ViewModel
import com.github.ianellis.clean.presentation.catfacts.summary.CatFactSummariesViewModel
import com.github.ianellis.clean.presentation.catfacts.summary.CatFactSummaryNavigator
import com.github.ianellis.clean.presentation.catfacts.summary.SimpleCatFactSummariesViewModel
import com.github.ianellis.clean.presentation.di.ViewModelKey
import com.ianellis.github.clean.domain.common.catfacts.summary.GetCatFactSummaries
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class CatFactsSummariesPresentationModule {

    @Binds
    @IntoMap
    @ViewModelKey(SimpleCatFactSummariesViewModel::class)
    internal abstract fun bindSimpleCatFactSummariesViewModel(vm: SimpleCatFactSummariesViewModel): ViewModel

    @Binds
    internal abstract fun bindCatFactSummariesViewModel(vm: SimpleCatFactSummariesViewModel): CatFactSummariesViewModel

    companion object {
        @Provides
        internal fun providesSimpleCatFactSummariesViewModel(
            getCatFactSummaries: GetCatFactSummaries,
            navigator: CatFactSummaryNavigator
        ): SimpleCatFactSummariesViewModel {
            return SimpleCatFactSummariesViewModel(
                getCatFactSummaries,
                navigator
            )
        }
    }
}
