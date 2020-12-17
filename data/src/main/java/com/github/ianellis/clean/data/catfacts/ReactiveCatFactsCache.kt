package com.github.ianellis.clean.data.catfacts

import kotlinx.coroutines.flow.Flow

interface ReactiveCatFactsCache {
    val facts: Flow<List<CatFactEntity>?>
}
