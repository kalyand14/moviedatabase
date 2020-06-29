package com.android.omdb.features.movie.data.source

import com.android.omdb.features.movie.data.model.MovieDetail
import com.android.omdb.features.movie.data.model.MovieSearchResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApiService {
    @GET("?type=movie")
    suspend fun getMovieSearchResult(
        @Query(value = "s") searchTitle: String,
        @Query(value = "page") pageIndex: Int
    ): MovieSearchResult

    @GET("?plot=full")
    suspend fun getMovieDetail(
        @Query(value = "i") titleId: String
    ): Response<MovieDetail>
}