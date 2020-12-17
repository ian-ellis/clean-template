package com.github.ianellis.clean.data.catfacts

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class CatFactsMemoryCache : ReadableCatFactsCache, WritableCatFactsCache, ReactiveCatFactsCache {

    private val state = MutableStateFlow<List<CatFactEntity>?>(null)

    override val facts: Flow<List<CatFactEntity>?> = state

    override suspend fun setFacts(facts: List<CatFactEntity>) {
        state.value = facts
    }

    override suspend fun getFacts(): List<CatFactEntity>? = state.value
}
