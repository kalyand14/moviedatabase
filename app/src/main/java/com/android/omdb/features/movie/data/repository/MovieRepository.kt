package com.android.omdb.features.movie.data.repository

import com.android.omdb.core.exception.Failure
import com.android.omdb.core.functional.Either
import com.android.omdb.features.movie.data.model.MovieDetail
import com.android.omdb.features.movie.data.model.MovieSearchResult

interface MovieRepository {
    suspend fun getMovieDetail(titleId: String): Either<Failure, MovieDetail>
}