package com.github.ianellis.clean.data.catfacts

import kotlinx.coroutines.flow.Flow

internal class SimpleReactiveCatFactsRepository(
    reactiveCache: ReactiveCatFactsCache,
    private val writableCache: WritableCatFactsCache,
    private val service: CatFactsService,
) : ReactiveCatFactsRepository {

    override val facts: Flow<List<CatFactEntity>?> = reactiveCache.facts

    override suspend fun load() {
        writableCache.setFacts(service.loadCatFacts())
    }
}
