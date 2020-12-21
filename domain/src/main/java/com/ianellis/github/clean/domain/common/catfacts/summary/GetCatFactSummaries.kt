package com.ianellis.github.clean.domain.common.catfacts.summary

interface GetCatFactSummaries {
    suspend fun getSummaries(): List<CatFactSummary>
}
