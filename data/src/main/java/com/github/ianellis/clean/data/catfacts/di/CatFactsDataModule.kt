package com.github.ianellis.clean.data.catfacts.di

import com.github.ianellis.clean.data.catfacts.CacheFallbackCatFactsRepository
import com.github.ianellis.clean.data.catfacts.CatFactsMemoryCache
import com.github.ianellis.clean.data.catfacts.CatFactsRepository
import com.github.ianellis.clean.data.catfacts.CatFactsService
import com.github.ianellis.clean.data.catfacts.ReactiveCatFactsCache
import com.github.ianellis.clean.data.catfacts.ReactiveCatFactsRepository
import com.github.ianellis.clean.data.catfacts.ReadableCatFactsCache
import com.github.ianellis.clean.data.catfacts.ServiceFallbackCatFactsRepository
import com.github.ianellis.clean.data.catfacts.SimpleReactiveCatFactsRepository
import com.github.ianellis.clean.data.catfacts.WritableCatFactsCache
import com.github.ianellis.clean.data.di.CacheFallback
import com.github.ianellis.clean.data.di.ServiceFallback
import dagger.Module
import dagger.Provides

@Module
class CatFactsDataModule {

    @Provides
    internal fun providesCatFactsMemoryCache(): CatFactsMemoryCache {
        return CatFactsMemoryCache()
    }

    @Provides
    internal fun providesReactiveCatFactsCache(memoryCache: CatFactsMemoryCache): ReactiveCatFactsCache = memoryCache

    @Provides
    internal fun providesReadableCatFactsCache(memoryCache: CatFactsMemoryCache): ReadableCatFactsCache = memoryCache

    @Provides
    internal fun providesWritableCatFactsCache(memoryCache: CatFactsMemoryCache): WritableCatFactsCache = memoryCache

    @Provides
    @CacheFallback
    fun providesCacheFallbackCatFactsRepository(
        readableCache: ReadableCatFactsCache,
        writableCatFactsCache: WritableCatFactsCache,
        service: CatFactsService
    ): CatFactsRepository {
        return CacheFallbackCatFactsRepository(
            readableCache,
            writableCatFactsCache,
            service
        )
    }

    @Provides
    @ServiceFallback
    fun providesServiceFallbackCatFactsRepository(
        readableCache: ReadableCatFactsCache,
        writableCatFactsCache: WritableCatFactsCache,
        service: CatFactsService
    ): CatFactsRepository {
        return ServiceFallbackCatFactsRepository(
            readableCache,
            writableCatFactsCache,
            service
        )
    }

    @Provides
    fun providesReactiveCatFactsRepository(
        reactiveCache: ReactiveCatFactsCache,
        writableCache: WritableCatFactsCache,
        service: CatFactsService,
    ): ReactiveCatFactsRepository {
        return SimpleReactiveCatFactsRepository(
            reactiveCache,
            writableCache,
            service
        )
    }
}
