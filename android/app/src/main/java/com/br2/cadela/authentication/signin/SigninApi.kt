package com.br2.cadela.authentication.signin

import com.br2.cadela.shared.ApiClient
import retrofit2.http.Body
import retrofit2.http.POST
import java.security.InvalidParameterException

data class SigninResponse(val user: User, val token: String)
data class Credentials(val email: String, val password: String)
data class SigninBody(val credential: Credentials)

interface SigninApiService {
    @POST("auth/signin")
    suspend fun signin(@Body credentials: SigninBody): SigninResponse
}

class SigninApi(client: ApiClient) {
    private val signinApi = client.createApiService(SigninApiService::class.java)

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