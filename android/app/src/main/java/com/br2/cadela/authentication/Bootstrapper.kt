package com.br2.cadela.authentication

import com.br2.cadela.authentication.signin.SigninApi
import com.br2.cadela.authentication.signin.SigninApiService
import com.br2.cadela.authentication.signin.SigninService
import com.br2.cadela.authentication.signin.TokenRepository
import com.br2.cadela.shared.ApiClient
import com.br2.cadela.shared.CadelaDatabase
import kotlin.jvm.javaClass

object AuthenticationServices {
    private lateinit var signinService: SigninService
    val SigninService: SigninService
        get() = signinService

    fun boostrap(db: CadelaDatabase, apiClient: ApiClient) {
        val tokenRepository = TokenRepository(db.tokenDao())
        val serviceApi = SigninApi(apiClient)
        signinService = SigninService(tokenRepository = tokenRepository, api = serviceApi)
    }
}