package com.ianellis.github.clean.domain.common.catfacts.summary

import com.github.ianellis.clean.data.catfacts.CatFactEntity
import com.github.ianellis.clean.data.catfacts.CatFactsRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Test

class SimpleGetCatFactSummariesSpec {

    private lateinit var repository: CatFactsRepository
    private lateinit var getFactSummaries: SimpleGetCatFactSummaries

    @Before
    fun setup() {
        repository = mockk()
        getFactSummaries = SimpleGetCatFactSummaries(repository)
    }

    @Test
    fun `getFacts() - returns summaries mapped from repository`() = runBlockingTest {
        // given we have cat facts
        coEvery { repository.getFacts() } returns listOf(entity("1", "fact1"), entity("2", "fact2"))
        // when we request the facts
        val result = getFactSummaries.getSummaries()
        // then the results provide the mapped id and text
        result.size shouldBeEqualTo 2
        result.first() shouldBeEqualTo CatFactSummary("1", "fact1")
        result.last() shouldBeEqualTo CatFactSummary("2", "fact2")
    }

    private fun entity(id: String, text: String): CatFactEntity {
        val entity = mockk<CatFactEntity>()
        every { entity.id } returns id
        every { entity.text } returns text
        return entity
    }
}
