package com.android.omdb.features.movie.data.source

import com.android.omdb.core.exception.Failure
import com.android.omdb.core.functional.Either
import com.android.omdb.features.movie.data.model.MovieDetail
import com.android.omdb.features.movie.data.model.MovieSearchResult

interface MovieDataSource {
    suspend fun getMovieSearchResult(
        searchTitle: String,
        pageIndex: Int
    ): Either<Failure, MovieSearchResult>

    suspend fun getMovieDetail(
        titleId: String
    ): Either<Failure, MovieDetail>
}