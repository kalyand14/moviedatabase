package com.android.omdb.features.movie.presentation.movielist

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.android.omdb.core.util.EspressoIdlingResource
import com.android.omdb.features.movie.presentation.movielist.robot.movieList
import com.android.omdb.utils.FileReader
import com.android.omdb.utils.enqueueFromFile
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MovieListActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(MovieListActivity::class.java, true, false)

    private val mockWebServer = MockWebServer()

    @Before
    fun setup() {
        mockWebServer.start(8080)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        mockWebServer.shutdown()
    }

    @Test
    fun checkErrorShown() {
        mockWebServer.enqueue(MockResponse().apply { setResponseCode(500) })
        ActivityScenario.launch(MovieListActivity::class.java)
        movieList {
            isHomeScreen()
            isErrorShown()
        }
    }

    @Test
    fun checkTodoShownInList() {
        mockWebServer.enqueueFromFile("success_response.json")
        ActivityScenario.launch(MovieListActivity::class.java)
        movieList {
            isHomeScreen()
            isMovieListShown()
           /* isItemShownAt(0, "Batman Begins")
            isItemShownAt(1, "Batman v Superman: Dawn of Justice")*/
        }
    }

}