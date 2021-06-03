package com.br2.cadela.authentication.signin

import retrofit2.http.Body
import retrofit2.http.POST

data class SigninResponse(val user: User, val token: String)
data class Credentials(val email: String, val password: String)
data class SigninBody(val credential: Credentials)

interface SigninApi {
    @POST("auth/signin")
    suspend fun signin(@Body credentials: SigninBody): SigninResponse
}