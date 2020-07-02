package com.android.omdb.features.movie.presentation.movielist.paging

import androidx.paging.PagingSource
import com.android.omdb.core.util.wrapEspressoIdlingResource
import com.android.omdb.features.movie.data.model.MovieSearchResult
import com.android.omdb.features.movie.data.source.OmdbApiService

class MoviePagingSource(
    private val currentQuery: String,
    private val api: OmdbApiService
) : PagingSource<Int, MovieSearchResult.MovieSearchItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieSearchResult.MovieSearchItem> {
        return try {

            wrapEspressoIdlingResource {

                val currentPageNumber = params.key ?: 1

                val response = api.getMovieSearchResult(currentQuery, currentPageNumber)

                LoadResult.Page(
                    data = response.searchResults,
                    prevKey = null, // Only paging forward.
                    nextKey = currentPageNumber + 1
                )

            }

        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}