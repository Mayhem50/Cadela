package com.br2.cadela.shared

import com.br2.cadela.authentication.signin.*
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.InvalidParameterException
import java.util.concurrent.TimeUnit

class Api(signinClass: Class<SigninApi>, baseUrl: HttpUrl = HttpUrl.get("http://localhost:3000/api/")) {
    private var token: String? = null
    private val okHttpClient = OkHttpClient.Builder().readTimeout(5000, TimeUnit.MILLISECONDS)
        .connectTimeout(5000, TimeUnit.MILLISECONDS).build()
    private val client =
        Retrofit.Builder().client(okHttpClient).baseUrl(baseUrl)
            .addConverterFactory(
                GsonConverterFactory.create()
            ).build()


    private val signinApi = client.create(signinClass)
    suspend fun signin(email: String, password: String): SigninResponse {
        if (email.isEmpty()) {
            throw InvalidParameterException("Empty email")
        }
        if (password.isEmpty()) {
            throw InvalidParameterException("Empty password")
        }
        val body = SigninBody(Credentials(email, password))
        return signinApi.signin(body)
    }
}