package com.android.omdb.features.movie.data.source

import com.android.omdb.core.data.BaseRemoteDatSource
import com.android.omdb.core.exception.Failure
import com.android.omdb.core.functional.Either
import com.android.omdb.features.movie.data.model.MovieDetail

class MovieRemoteDataSource(val apiService: OmdbApiService) : BaseRemoteDatSource(),
    MovieDataSource {
    override suspend fun getMovieDetail(titleId: String): Either<Failure, MovieDetail> {
        return safeApiCall { apiService.getMovieDetail(titleId) }
    }
}