package com.github.ianellis.clean.data.catfacts

interface CatFactsRepository {
    suspend fun getFacts(): List<CatFactEntity>
}
