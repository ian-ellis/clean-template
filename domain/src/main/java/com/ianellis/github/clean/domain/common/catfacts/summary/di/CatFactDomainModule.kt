package com.ianellis.github.clean.domain.common.catfacts.summary.di

import com.github.ianellis.clean.data.catfacts.CatFactsRepository
import com.github.ianellis.clean.data.di.CacheFallback
import com.ianellis.github.clean.domain.common.catfacts.summary.GetCatFactSummaries
import com.ianellis.github.clean.domain.common.catfacts.summary.SimpleGetCatFactSummaries
import dagger.Module
import dagger.Provides

@Module
class CatFactDomainModule {

    @Provides
    fun providesGetCatFactSummaries(
        @CacheFallback
        repository: CatFactsRepository
    ): GetCatFactSummaries {
        return SimpleGetCatFactSummaries(
            repository
        )
    }
}
