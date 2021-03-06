package com.android.omdb.features.movie.data.model

import com.squareup.moshi.Json

data class MovieSearchResult(
    @field:Json(name = "Response")
    var response: String,

    @field:Json(name = "Error")
    var error: String,

    @field:Json(name = "totalResults")
    var totalResults: String,

    @field:Json(name = "Search")
    var searchResults: List<MovieSearchItem?>

) {
    data class MovieSearchItem(
        @field:Json(name = "Type")
        var type: String,

        @field:Json(name = "Year")
        var year: String,

        @field:Json(name = "imdbID")
        var imdbId: String,

        @field:Json(name = "Poster")
        var poster: String,

        @field:Json(name = "Title")
        var title: String
    )
}