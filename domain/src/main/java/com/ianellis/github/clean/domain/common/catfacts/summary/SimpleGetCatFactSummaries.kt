package com.ianellis.github.clean.domain.common.catfacts.summary

import com.github.ianellis.clean.data.catfacts.CatFactEntity
import com.github.ianellis.clean.data.catfacts.CatFactsRepository

internal class SimpleGetCatFactSummaries(
    private val repository: CatFactsRepository
) : GetCatFactSummaries {

    override suspend fun getSummaries(): List<CatFactSummary> {
        return repository.getFacts().toSummaries()
    }

    private fun List<CatFactEntity>.toSummaries() = this.map { entity ->
        CatFactSummary(
            id = entity.id,
            text = entity.text,
        )
    }
}
