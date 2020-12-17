package com.github.ianellis.clean.data.catfacts

class ServiceFallbackCatFactsRepository(
    private val readableCache: ReadableCatFactsCache,
    private val writableCatFactsCache: WritableCatFactsCache,
    private val service: CatFactsService
) : CatFactsRepository {

    override suspend fun getFacts(): List<CatFactEntity> {
        return readableCache.getFacts() ?: run {
            val facts = service.loadCatFacts()
            writableCatFactsCache.setFacts(facts)
            facts
        }
    }
}
