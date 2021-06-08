package com.br2.cadela.authentication

import com.br2.cadela.authentication.signin.SigninApi
import com.br2.cadela.authentication.signin.SigninService
import com.br2.cadela.authentication.signin.SigninViewModel
import com.br2.cadela.authentication.signin.TokenRepository
import com.br2.cadela.shared.ApiClient
import com.br2.cadela.shared.CadelaDatabase

object AuthenticationModule {
    private lateinit var _signinVm: SigninViewModel
    val signinVm: SigninViewModel = _signinVm

    fun boostrap(db: CadelaDatabase, apiClient: ApiClient) {
        val tokenRepository = TokenRepository(db.tokenDao())
        val serviceApi = SigninApi(apiClient)
        val signinService = SigninService(serviceApi, tokenRepository)
        _signinVm = SigninViewModel(signinService)
    }
}