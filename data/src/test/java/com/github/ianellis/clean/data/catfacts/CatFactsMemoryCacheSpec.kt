package com.github.ianellis.clean.data.catfacts

import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

internal class CatFactsMemoryCacheSpec {

    private lateinit var cache: CatFactsMemoryCache

    @Before
    fun setup() {
        cache = CatFactsMemoryCache()
    }

    @Test
    fun `getFacts() - returns null initially to represent an empty cache`() = runBlockingTest {
        cache.getFacts() shouldBeEqualTo null
    }

    @Test
    fun `facts - starts with null to represent an empty cache`() = runBlockingTest {
        // given we are listening to facts
        val result = mutableListOf<List<CatFactEntity>?>()
        val job = launch {
            cache.facts.collect { result.add(it) }
        }
        // then we get an initial result that is null
        result.size shouldBeEqualTo 1
        result.last() shouldBeEqualTo null

        job.cancel()
    }

    @Test
    fun `setFacts() sets facts, emitted to flow`() = runBlockingTest {
        // given we are listening to facts
        val result = mutableListOf<List<CatFactEntity>?>()
        val job = launch {
            cache.facts.collect { result.add(it) }
        }
        // when we set facts
        val facts = listOf<CatFactEntity>(mockk(), mockk())
        cache.setFacts(facts)
        // then we get the update
        result.size shouldBeEqualTo 2
        result.last() shouldBeEqualTo facts

        job.cancel()
    }

    @Test
    fun `setFacts() sets facts, available by getFacts`() = runBlockingTest {
        // when we set facts
        val facts = listOf<CatFactEntity>(mockk(), mockk())
        cache.setFacts(facts)
        // then get facts returns the changes
        cache.getFacts() shouldBeEqualTo facts
    }
}
