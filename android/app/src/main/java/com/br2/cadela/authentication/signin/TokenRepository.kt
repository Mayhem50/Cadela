package com.br2.cadela.authentication.signin

import java.security.InvalidParameterException

class TokenRepository(private val tokenDao: TokenDao) {
    suspend fun save(token: String, userId: String) {
        if (userId.isEmpty()) {
            throw InvalidParameterException("Empty user id")
        }
        if (token.isEmpty()) {
            throw InvalidParameterException("Empty token")
        }

        tokenDao.save(TokenRecord(token, userId))
    }
}