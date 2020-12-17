package com.github.ianellis.clean.data.catfacts

interface CatFactsService {
    suspend fun loadCatFacts(): List<CatFactEntity>
}
