package com.br2.cadela.shared

import com.br2.cadela.authentication.signin.User
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

data class SigninResponse(val user: User, val token: String)

private object ApiResources {
    private var token: String? = null
    private val okHttpClient = OkHttpClient.Builder().readTimeout(5000, TimeUnit.MILLISECONDS)
        .connectTimeout(5000, TimeUnit.MILLISECONDS).build()
    val client =
        Retrofit.Builder().client(okHttpClient).baseUrl("https://cadela.herokuapp.com/api").build()
}

class Api {
    private data class Credentials(val email: String, val password: String)
    private interface SigninApi {
        @POST("/auth/signin")
        suspend fun signin(@Body credentials: HashMap<String, Credentials>): SigninResponse
    }

    private val signinApi = ApiResources.client.create(SigninApi::class.java)
    suspend fun signin(email: String, password: String): SigninResponse {
        val body = HashMap<String, Credentials>()
        body["credential"] = Credentials(email, password)
        return signinApi.signin(body)
    }
}