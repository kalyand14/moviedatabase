package com.android.omdb.features.movie.data.repository

import com.android.omdb.core.data.BaseRepository
import com.android.omdb.core.exception.Failure
import com.android.omdb.core.functional.Either
import com.android.omdb.features.movie.data.model.MovieDetail
import com.android.omdb.features.movie.data.model.MovieSearchResult
import com.android.omdb.features.movie.data.source.OmdbApi

class MovieDataRepository(private val api: OmdbApi) : BaseRepository(), MovieRepository {

    override suspend fun getMovieSearchResult(
        searchTitle: String,
        pageIndex: Int
    ): Either<Failure, MovieSearchResult> {
        return safeApiCall { api.getMovieSearchResult(searchTitle, pageIndex) }
    }

    override suspend fun getMovieDetail(titleId: String): Either<Failure, MovieDetail> {
        return safeApiCall { api.getMovieDetail(titleId) }
    }
}