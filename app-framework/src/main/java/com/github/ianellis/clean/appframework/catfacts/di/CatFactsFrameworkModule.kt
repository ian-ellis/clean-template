package com.github.ianellis.clean.appframework.catfacts.di

import com.github.ianellis.clean.appframework.catfacts.RetrofitCatFactsService
import com.github.ianellis.clean.data.catfacts.CatFactsService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class CatFactsFrameworkModule {

    @Provides
    fun providesRetrofitCatFacsService(retrofit: Retrofit): CatFactsService {
        return retrofit.create(RetrofitCatFactsService::class.java)
    }
}
