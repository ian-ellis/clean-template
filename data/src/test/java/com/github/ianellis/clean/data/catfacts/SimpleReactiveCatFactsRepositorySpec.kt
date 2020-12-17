package com.github.ianellis.clean.data.catfacts

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

class SimpleReactiveCatFactsRepositorySpec {

    private lateinit var reactiveCatFactsCache: ReactiveCatFactsCache
    private lateinit var writableCatFactsCache: WritableCatFactsCache
    private lateinit var cachedFacts: ConflatedBroadcastChannel<List<CatFactEntity>?>
    private lateinit var service: CatFactsService
    private lateinit var repository: SimpleReactiveCatFactsRepository

    @Before
    fun setup() {
        reactiveCatFactsCache = mockk()
        writableCatFactsCache = mockk(relaxed = true)
        service = mockk()
        cachedFacts = ConflatedBroadcastChannel()
        every { reactiveCatFactsCache.facts } returns cachedFacts.asFlow()
        repository = SimpleReactiveCatFactsRepository(
            reactiveCatFactsCache,
            writableCatFactsCache,
            service

        )
    }

    @Test
    fun `facts - are returned from the cache`() = runBlockingTest {
        // given we have cached facts
        val initial = listOf<CatFactEntity>(mockk(), mockk())
        val update = listOf<CatFactEntity>(mockk(), mockk())
        // when we observe the repository
        val result = mutableListOf<List<CatFactEntity>?>()
        val job = launch {
            repository.facts.collect { result.add(it) }
        }
        // and the cache fires
        cachedFacts.send(initial)
        // then we receive the cached events
        result.size shouldBeEqualTo 1
        result.last() shouldBeEqualTo initial
        // when the cache updates
        cachedFacts.send(update)
        // then we receive the update
        result.last() shouldBeEqualTo update

        job.cancel()
    }

    @Test
    fun `load() - loads data from services and writes to cache`() = runBlockingTest {
        // given our service will load facts
        val loaded = listOf<CatFactEntity>(mockk(), mockk())
        coEvery { service.loadCatFacts() } returns loaded
        // when we load
        repository.load()
        // the we load data
        coVerify(exactly = 1) { service.loadCatFacts() }
        // and we cache them
        coVerify(exactly = 1) { writableCatFactsCache.setFacts(loaded) }
    }
}
