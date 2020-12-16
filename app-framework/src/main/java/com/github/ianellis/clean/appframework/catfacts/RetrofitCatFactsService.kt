package com.github.ianellis.clean.appframework.catfacts

import com.github.ianellis.clean.data.catfacts.CatFactEntity
import com.github.ianellis.clean.data.catfacts.CatFactsService
import retrofit2.http.GET

interface RetrofitCatFactsService : CatFactsService {
    @GET("facts")
    override suspend fun loadCatFacts(): List<CatFactEntity>
}
