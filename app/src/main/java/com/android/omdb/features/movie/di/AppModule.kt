package com.android.omdb.features.movie.di

import com.android.omdb.BuildConfig
import com.android.omdb.features.movie.data.source.OmdbApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(get(), BuildConfig.BASE_URL) }
    single { provideOmdbApiService(get()) }
}


private fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .build()
} else OkHttpClient
    .Builder()
    .build()


private val authInterceptor = Interceptor { chain ->
    val newUrl = chain.request().url
        .newBuilder()
        .addQueryParameter("apikey", BuildConfig.API_KEY)
        .build()

    val newRequest = chain.request()
        .newBuilder()
        .url(newUrl)
        .build()

    chain.proceed(newRequest)
}


private fun provideRetrofit(
    okHttpClient: OkHttpClient,
    BASE_URL: String
): Retrofit =
    Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

private fun provideOmdbApiService(retrofit: Retrofit): OmdbApiService =
    retrofit.create(OmdbApiService::class.java)
