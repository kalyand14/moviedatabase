package com.android.omdb.features.movie.data.repository

import com.android.omdb.core.functional.Either
import com.android.omdb.features.movie.TestDataFactory
import com.android.omdb.features.movie.data.source.MovieDataSource
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MovieDataRepositoryTest {
    private val dispatcher = TestCoroutineDispatcher()

    private lateinit var repository: MovieRepository

    private val dataSource: MovieDataSource = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = MovieDataRepository(dataSource)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun givenMovieDetail_whenFetch_shouldReturnSuccess() = dispatcher.runBlockingTest {
        // Assume
        coEvery { dataSource.getMovieDetail(TestDataFactory.titleId) } returns (Either.build { TestDataFactory.getMovieDetail() })
        // Act
        val result = repository.getMovieDetail(TestDataFactory.titleId)
        // Assert
        coVerify { dataSource.getMovieDetail(TestDataFactory.titleId) }
        Truth.assertThat(result.isRight).isTrue()
        Truth.assertThat(result.fold({ }, { it })).isEqualTo(TestDataFactory.getMovieDetail())
    }


}