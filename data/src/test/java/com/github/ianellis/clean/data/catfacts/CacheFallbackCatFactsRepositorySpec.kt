package com.github.ianellis.clean.data.catfacts

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.coInvoking
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldThrow
import org.junit.Before
import org.junit.Test
import java.lang.RuntimeException

class CacheFallbackCatFactsRepositorySpec {

    private lateinit var readableCatFactsCache: ReadableCatFactsCache
    private lateinit var writableCatFactsCache: WritableCatFactsCache
    private lateinit var service: CatFactsService
    private lateinit var repository: CacheFallbackCatFactsRepository

    @Before
    fun setup() {
        readableCatFactsCache = mockk()
        writableCatFactsCache = mockk(relaxed = true)
        service = mockk()
        repository = CacheFallbackCatFactsRepository(
            readableCatFactsCache,
            writableCatFactsCache,
            service
        )
    }

    @Test
    fun `getFacts() - loads facts, writes to cache and returns`() = runBlockingTest {
        // give our service will load facts
        val loaded = listOf<CatFactEntity>(mockk(), mockk())
        coEvery { service.loadCatFacts() } returns loaded
        // when we getFacts
        val result = repository.getFacts()
        // then we call the service
        coVerify { service.loadCatFacts() }
        // write the results to the cache
        coVerify { writableCatFactsCache.setFacts(loaded) }
        // and we receive the results back
        result shouldBeEqualTo loaded
    }

    @Test
    fun `getFacts() - returns cached facts when load fails`() = runBlockingTest {
        // give our service will fail to load facts
        coEvery { service.loadCatFacts() } throws RuntimeException("OOPS")
        // and we have cached facts
        val cached = listOf<CatFactEntity>(mockk(), mockk())
        coEvery { readableCatFactsCache.getFacts() } returns cached
        // when we getFacts
        val result = repository.getFacts()
        // then we call the service
        coVerify { service.loadCatFacts() }
        // and we receive the cached results back
        result shouldBeEqualTo cached
    }

    @Test
    fun `getFacts() - throws network exception when nothing cached and network fails`() = runBlockingTest {
        // give our service will fail to load facts
        val exception = RuntimeException("OOPS")
        coEvery { service.loadCatFacts() } throws exception
        // and we have nothing cached facts
        coEvery { readableCatFactsCache.getFacts() } returns null
        // when we getFacts we get the network exception
        coInvoking { repository.getFacts() } shouldThrow exception
    }
}
