package com.android.omdb.features.movie.data.repository

import com.android.omdb.core.exception.Failure
import com.android.omdb.core.functional.Either
import com.android.omdb.features.movie.data.model.MovieDetail
import com.android.omdb.features.movie.data.model.MovieSearchResult
import com.android.omdb.features.movie.data.source.MovieDataSource

class MovieDataRepository(private val movieDataSource: MovieDataSource) : MovieRepository {

    override suspend fun getMovieSearchResult(
        searchTitle: String,
        pageIndex: Int
    ): Either<Failure, MovieSearchResult> {
        //If caching is needed it can be implemented here
        return movieDataSource.getMovieSearchResult(searchTitle, pageIndex)
    }

    override suspend fun getMovieDetail(titleId: String): Either<Failure, MovieDetail> {
        //If caching is needed it can be implemented here
        return movieDataSource.getMovieDetail(titleId)
    }
}