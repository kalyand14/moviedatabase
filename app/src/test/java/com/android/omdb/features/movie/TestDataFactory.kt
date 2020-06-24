package com.android.omdb.features.movie

import com.android.omdb.core.exception.Failure
import com.android.omdb.features.movie.data.model.MovieDetail
import com.android.omdb.features.movie.data.model.MovieSearchResult

class TestDataFactory {

    companion object Factory {

        const val NOT_FOUND = "Not found"

        val titleId = "tt0372784"

        private fun getMovieSearchItem(
            title: String = "Batman Begins",
            year: String = "2005",
            type: String = "movie",
            imdbId: String = "tt0372784",
            poster: String = "https://m.media-amazon.com/images/M/MV5BZmUwNGU2ZmItMmRiNC00MjhlLTg5YWUtODMyNzkxODYzMmZlXkEyXkFqcGdeQXVyNTIzOTk5ODM@._V1_SX300.jpg"
        ) = MovieSearchResult.MovieSearchItem(
            title = title,
            year = year,
            type = type,
            imdbId = imdbId,
            poster = poster
        )

        private fun getMovieSearchList() = mutableListOf(
            getMovieSearchItem(imdbId = "1"),
            getMovieSearchItem(imdbId = "2"),
            getMovieSearchItem(imdbId = "3")
        )


        fun getMovieSearchResult(
            totalResults: String = getMovieSearchList().size.toString(),
            search: List<MovieSearchResult.MovieSearchItem?> = getMovieSearchList()
        ) = MovieSearchResult(
            totalResults = totalResults,
            searchResults = search,
            response = "",
            error = ""
        )

        private fun getRating(
            value: String = "Internet Movie Database",
            source: String = "8.2/10"
        ) = MovieDetail.Ratings(
            source = source,
            value = value

        )

        private fun getRatings() = mutableListOf(
            getRating(
                source = "Internet Movie Database",
                value = "8.2/10"
            ),
            getRating(
                source = "Rotten Tomatoes",
                value = "84%"
            ),
            getRating(
                source = "Metacritic",
                value = "70/100"
            )
        )


        fun getMovieDetail(
            title: String = "Batman Begins",
            year: String = "2005",
            rated: String = "PG-13",
            released: String = "15 Jun 2005",
            runtime: String = "140 min",
            genre: String = "Action, Adventure",
            director: String = "Christopher Nolan",
            writer: String = "Bob Kane (characters), David S. Goyer (story), Christopher Nolan (screenplay), David S. Goyer (screenplay)",
            actors: String = "Christian Bale, Michael Caine, Liam Neeson, Katie Holmes",
            plot: String = "After training with his mentor, Batman begins his fight to free crime-ridden Gotham City from corruption.",
            language: String = "English, Mandarin",
            country: String = "USA, UK",
            awards: String = "Nominated for 1 Oscar. Another 14 wins & 72 nominations.",
            poster: String = "https://m.media-amazon.com/images/M/MV5BZmUwNGU2ZmItMmRiNC00MjhlLTg5YWUtODMyNzkxODYzMmZlXkEyXkFqcGdeQXVyNTIzOTk5ODM@._V1_SX300.jpg",
            metascore: String = "70",
            imdbRating: String = "8.2",
            imdbVotes: String = "1,261,772",
            imdbId: String = "tt0372784",
            type: String = "movie",
            dvd: String = "18 Oct 2005",
            boxOffice: String = "$204,100,000",
            production: String = "Warner Bros. Pictures",
            website: String = "N/A",
            response: String = "true"
        ) = MovieDetail(
            title = title,
            year = year,
            rated = rated,
            released = released,
            runtime = runtime,
            genre = genre,
            director = director,
            writer = writer,
            actors = actors,
            plot = plot,
            language = language,
            country = country,
            awards = awards,
            poster = poster,
            metascore = metascore,
            imdbrating = imdbRating,
            imdbvotes = imdbVotes,
            imdbid = imdbId,
            type = type,
            dvd = dvd,
            boxoffice = boxOffice,
            production = production,
            website = website,
            response = response,
            ratings = getRatings()
        )

        fun getDataError() = Failure.DataError(NOT_FOUND)

    }

}
