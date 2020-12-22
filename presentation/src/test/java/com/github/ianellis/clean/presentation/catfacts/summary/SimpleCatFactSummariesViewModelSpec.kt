package com.github.ianellis.clean.presentation.catfacts.summary

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.github.ianellis.clean.test.presentation.CoroutineTestRule
import com.ianellis.github.clean.domain.common.catfacts.summary.CatFactSummary
import com.ianellis.github.clean.domain.common.catfacts.summary.GetCatFactSummaries
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SimpleCatFactSummariesViewModelSpec {

    @Rule
    fun rule() = InstantTaskExecutorRule()

    @get:Rule
    var coroutineRule = CoroutineTestRule()

    private lateinit var getCatFactSummaries: GetCatFactSummaries
    private lateinit var navigator: CatFactSummaryNavigator
    private lateinit var viewModel: SimpleCatFactSummariesViewModel

    @Before
    fun setup() {
        getCatFactSummaries = mockk()
        navigator = mockk(relaxed = true)
    }

    @Test
    fun `SimpleCatFactSummariesViewModel() - loads data on init`() = runBlockingTest {
        // given we will load successfully
        val summaries = listOf<CatFactSummary>(mockk(), mockk())
        coEvery { getCatFactSummaries.getSummaries() } coAnswers {
            delay(1000)
            summaries
        }
        `when the view model is initialised`()
        // then we show a loading state
        viewModel.loading.value shouldBeEqualTo true
        // and no data or error
        viewModel.error.value shouldBeEqualTo null
        viewModel.summaries.value shouldBeEqualTo emptyList()
        // when the data loads
        coroutineRule.testDispatcher.advanceTimeBy(1000)
        // then we show it
        viewModel.summaries.value shouldBeEqualTo summaries
        // and stop loading
        viewModel.loading.value shouldBeEqualTo false
    }

    @Test
    fun `SimpleCatFactSummariesViewModel() - loads data on init, shows errors`() = runBlockingTest {
        // given the load will fail
        val exception = RuntimeException("OOPS")
        coEvery { getCatFactSummaries.getSummaries() } coAnswers {
            delay(1000)
            throw exception
        }
        `when the view model is initialised`()
        // then we show a loading state
        viewModel.loading.value shouldBeEqualTo true
        // when the data loads
        coroutineRule.testDispatcher.advanceTimeBy(1000)
        // then we show it
        viewModel.summaries.value shouldBeEqualTo emptyList()
        // and stop loading
        viewModel.loading.value shouldBeEqualTo false
        // and show the error
        viewModel.error.value shouldBeEqualTo exception
    }

    @Test
    fun `selected() - calls navigator`() {
        // given we will load successfully
        val summaries = listOf<CatFactSummary>(mockk(), mockk())
        coEvery { getCatFactSummaries.getSummaries() } returns summaries

        `when the view model is initialised`()
        // and we select a summary
        val selected = mockk<CatFactSummary>()
        viewModel.selected(selected)
        // then we pass it to the navigator
        coVerify { navigator.summarySelected(selected) }
    }

    @Test
    fun `refresh() - reloads data`() {
        // given we will load successfully
        val summaries = listOf<CatFactSummary>(mockk(), mockk())
        coEvery { getCatFactSummaries.getSummaries() } returns summaries
        `when the view model is initialised`()
        // and given reload will give us new data
        val freshSummaries = listOf<CatFactSummary>(mockk(), mockk(), mockk())
        coEvery { getCatFactSummaries.getSummaries() } coAnswers {
            delay(1000)
            freshSummaries
        }
        // when we refresh
        viewModel.refresh()
        // then we show loading
        viewModel.loading.value shouldBeEqualTo true
        // when the data loads
        coroutineRule.testDispatcher.advanceTimeBy(1000)
        // then we stop loading and show the new data
        viewModel.loading.value shouldBeEqualTo false
        viewModel.summaries.value shouldBeEqualTo freshSummaries
    }

    @Test
    fun `refresh() - reloads data, shows errors`() {
        // given we will load successfully
        val summaries = listOf<CatFactSummary>(mockk(), mockk())
        coEvery { getCatFactSummaries.getSummaries() } returns summaries
        `when the view model is initialised`()
        // and given reload will fail
        val exception = java.lang.RuntimeException("OOPS")
        coEvery { getCatFactSummaries.getSummaries() } coAnswers {
            delay(1000)
            throw exception
        }
        // when we refresh
        viewModel.refresh()
        // then we show loading
        viewModel.loading.value shouldBeEqualTo true
        // when the data loads
        coroutineRule.testDispatcher.advanceTimeBy(1000)
        // then we stop loading, and show the old data and error
        viewModel.loading.value shouldBeEqualTo false
        viewModel.summaries.value shouldBeEqualTo summaries
        viewModel.error.value shouldBeEqualTo exception
    }

    @Test
    fun `refresh() - reloads data, clears errors until load complete`() {
        // given the load will fail
        val exception = RuntimeException("OOPS")
        coEvery { getCatFactSummaries.getSummaries() } throws exception
        `when the view model is initialised`()
        // then we show the error
        viewModel.error.value shouldBeEqualTo exception
        // given the second load will fail after a second
        val exception2 = RuntimeException("OOPS AGAIN")
        coEvery { getCatFactSummaries.getSummaries() } coAnswers {
            delay(1000)
            throw exception2
        }
        // when we refresh
        viewModel.refresh()
        // then we clear the error and start loading
        viewModel.error.value shouldBeEqualTo null
        viewModel.loading.value shouldBeEqualTo true
        // when the data loads
        coroutineRule.testDispatcher.advanceTimeBy(1000)
        // we show the new exception
        viewModel.error.value shouldBeEqualTo exception2
    }

    @Test
    fun `refresh() - ignored if loading initially`() {
        // given we will load successfully
        val summaries = listOf<CatFactSummary>(mockk(), mockk())
        coEvery { getCatFactSummaries.getSummaries() } coAnswers {
            delay(1000)
            summaries
        }
        `when the view model is initialised`()
        // and we refresh
        viewModel.refresh()
        // then we have only made one call to load
        coVerify(exactly = 1) { getCatFactSummaries.getSummaries() }
        // when the load finishes
        coroutineRule.testDispatcher.advanceTimeBy(1000)
        // and we call refresh again
        viewModel.refresh()
        // then we make another call to load
        coVerify(exactly = 2) { getCatFactSummaries.getSummaries() }

        coroutineRule.testDispatcher.advanceTimeBy(1000)
    }

    @Test
    fun `refresh() - ignored if already refreshing initially`() {
        // given we will load successfully
        val summaries = listOf<CatFactSummary>(mockk(), mockk())
        coEvery { getCatFactSummaries.getSummaries() } coAnswers {
            delay(1000)
            summaries
        }
        `when the view model is initialised`()
        // and the data loads
        coroutineRule.testDispatcher.advanceTimeBy(1000)
        // and we refresh twice
        viewModel.refresh()
        // then we have only made 2 calls to load (initial and refresh)
        coVerify(exactly = 2) { getCatFactSummaries.getSummaries() }
        // when the load finishes
        coroutineRule.testDispatcher.advanceTimeBy(1000)
        // and we call refresh again
        viewModel.refresh()
        // then we make another call to load
        coVerify(exactly = 3) { getCatFactSummaries.getSummaries() }

        coroutineRule.testDispatcher.advanceTimeBy(1000)
    }

    private fun `when the view model is initialised`() {
        viewModel = SimpleCatFactSummariesViewModel(
            getCatFactSummaries,
            navigator
        )
    }
}
