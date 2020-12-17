package com.github.ianellis.clean.data.catfacts

internal class CacheFallbackCatFactsRepository(
    private val readableCache: ReadableCatFactsCache,
    private val writableCatFactsCache: WritableCatFactsCache,
    private val service: CatFactsService
) : CatFactsRepository {

    override suspend fun getFacts(): List<CatFactEntity> {
        return try {
            val facts = service.loadCatFacts()
            writableCatFactsCache.setFacts(facts)
            facts
        } catch (e: Exception) {
            readableCache.getFacts() ?: throw e
        }
    }
}
