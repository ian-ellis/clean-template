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

class ServiceFallbackCatFactsRepositorySpec {

    private lateinit var readableCatFactsCache: ReadableCatFactsCache
    private lateinit var writableCatFactsCache: WritableCatFactsCache
    private lateinit var service: CatFactsService
    private lateinit var repository: ServiceFallbackCatFactsRepository

    @Before
    fun setup() {
        readableCatFactsCache = mockk()
        writableCatFactsCache = mockk(relaxed = true)
        service = mockk()
        repository = ServiceFallbackCatFactsRepository(
            readableCatFactsCache,
            writableCatFactsCache,
            service
        )
    }

    @Test
    fun `getFacts() - returns cached facts if available`() = runBlockingTest {
        // give our service will load facts
        val cached = listOf<CatFactEntity>(mockk(), mockk())
        coEvery { readableCatFactsCache.getFacts() } returns cached
        // when we getFacts
        val result = repository.getFacts()
        // then we do not call the service
        coVerify(exactly = 0) { service.loadCatFacts() }
        // and we receive the cached results back
        result shouldBeEqualTo cached
    }

    @Test
    fun `getFacts() - loads and saves new data when nothing is cached`() = runBlockingTest {
        // give we have nothing cached
        coEvery { readableCatFactsCache.getFacts() } returns null
        // and loading will return data
        val loaded = listOf<CatFactEntity>(mockk(), mockk())
        coEvery { service.loadCatFacts() } returns loaded
        // when we getFacts
        val result = repository.getFacts()
        // then we call the service
        coVerify(exactly = 1) { service.loadCatFacts() }
        // and save the result
        coVerify { writableCatFactsCache.setFacts(loaded) }
        // and we receive the loaded results back
        result shouldBeEqualTo loaded
    }

    @Test
    fun `getFacts() - throws exception when service fails`() = runBlockingTest {
        // give we have nothing cached
        coEvery { readableCatFactsCache.getFacts() } returns null
        // and loading will fail
        val exception = RuntimeException("OOPS")
        coEvery { service.loadCatFacts() } throws exception
        // when we getFacts we get the service exception
        coInvoking { repository.getFacts() } shouldThrow exception
    }
}
