package com.github.ianellis.clean.data.catfacts

interface ReadableCatFactsCache {
    suspend fun getFacts(): List<CatFactEntity>?
}
