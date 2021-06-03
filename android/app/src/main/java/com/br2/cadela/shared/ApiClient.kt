package com.br2.cadela.shared

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient(baseUrl: HttpUrl = HttpUrl.get("https://cadela.herokuapp.com/")) {
    private var token: String? = null
    private val okHttpClient = OkHttpClient.Builder().readTimeout(5000, TimeUnit.MILLISECONDS)
        .connectTimeout(5000, TimeUnit.MILLISECONDS).build()
    private val client =
        Retrofit.Builder().client(okHttpClient).baseUrl(baseUrl)
            .addConverterFactory(
                GsonConverterFactory.create()
            ).build()

    fun<T> createApiService(javaClass: Class<T>): T {
        return client.create(javaClass)
    }
}

