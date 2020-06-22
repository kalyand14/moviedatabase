package com.android.omdb.core.data

import com.android.omdb.core.exception.Failure
import com.android.omdb.core.functional.Either
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

open class BaseRepository {
    suspend fun <T : Any> safeApiCall(
        call: suspend () -> Response<T>
    ): Either<Failure, T> {
        val response = call.invoke()
        return if (response.isSuccessful) {
            Either.Right(response.body()!!)
        } else {
            val error = response.errorBody()?.toString()
            val message = StringBuilder()
            error?.let {
                try {
                    message.append(JSONObject(it).getString("Error"))
                } catch (e: JSONException) {
                }
            }
            Either.Left(Failure.DataError("OOps .. Something went wrong due to  $error"))
        }
    }
}