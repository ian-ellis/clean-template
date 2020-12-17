package com.github.ianellis.clean.data.catfacts

interface WritableCatFactsCache {
    suspend fun setFacts(facts: List<CatFactEntity>)
}
