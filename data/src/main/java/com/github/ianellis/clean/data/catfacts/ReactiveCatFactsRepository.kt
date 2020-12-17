package com.github.ianellis.clean.data.catfacts

import kotlinx.coroutines.flow.Flow

interface ReactiveCatFactsRepository {
    val facts: Flow<List<CatFactEntity>?>
    suspend fun load()
}
