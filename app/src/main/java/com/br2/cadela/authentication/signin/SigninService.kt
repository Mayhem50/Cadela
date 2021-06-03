package com.br2.cadela.authentication.signin

import com.br2.cadela.shared.Api
import java.security.InvalidParameterException

class SigninService(private val api: Api, private val tokenRepository: TokenRepository) {
    suspend fun signin(email: String, password: String): String {
        if (email.isEmpty()) {
            throw InvalidParameterException("Empty email")
        }
        if (password.isEmpty()) {
            throw InvalidParameterException("Empty password")
        }
        val response = api.signin(email.trim(), password)
        tokenRepository.save(response.token, response.user.id)
        return response.token
    }
}