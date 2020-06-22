package com.android.omdb.features.movie.data.model

import com.squareup.moshi.Json

data class MovieDetail(

    @field:Json(name = "Response")
    var response: String,

    @field:Json(name = "Website")
    var website: String,

    @field:Json(name = "Production")
    var production: String,

    @field:Json(name = "BoxOffice")
    var boxoffice: String,

    @field:Json(name = "DVD")
    var dvd: String,

    @field:Json(name = "Type")
    var type: String,

    @field:Json(name = "imdbID")
    var imdbid: String,

    @field:Json(name = "imdbVotes")
    var imdbvotes: String,

    @field:Json(name = "imdbRating")
    var imdbrating: String,

    @field:Json(name = "Metascore")
    var metascore: String,

    @field:Json(name = "Ratings")
    var ratings: List<Ratings>,

    @field:Json(name = "Poster")
    var poster: String,

    @field:Json(name = "Awards")
    var awards: String,

    @field:Json(name = "Country")
    var country: String,

    @field:Json(name = "Language")
    var language: String,

    @field:Json(name = "Plot")
    var plot: String,

    @field:Json(name = "Actors")
    var actors: String,

    @field:Json(name = "Writer")
    var writer: String,

    @field:Json(name = "Director")
    var director: String,

    @field:Json(name = "Genre")
    var genre: String,

    @field:Json(name = "Runtime")
    var runtime: String,

    @field:Json(name = "Released")
    var released: String,

    @field:Json(name = "Rated")
    var rated: String,

    @field:Json(name = "Year")
    var year: String,

    @field:Json(name = "Title")
    var title: String

) {
    data class Ratings(
        @field:Json(name = "Value")
        var value: String,
        @field:Json(name = "Source")
        var source: String
    )
}