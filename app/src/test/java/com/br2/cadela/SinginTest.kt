package com.br2.cadela


import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class Api {
    fun signin(email: String, password: String): String {
        return "any_token"
    }
}

class TokenRepository {
    fun save(token: String) {

    }
}

class SigninService(private val api: Api, private val tokenRepository: TokenRepository) {
    fun signin(email: String, password: String): String {
        val response = api.signin(email, password)
        tokenRepository.save(response)
        return response
    }
}

class SinginTest {
    val api = spyk<Api>()
    val tokenRepository = spyk<TokenRepository>()

    @Test
    fun `Save token for userId if valid email & password provided`() {
        val sut = SigninService(api, tokenRepository)
        val email = "any_email@mail.com"
        val password = "any_password"
        val token = sut.signin(email, password)
        verify {
            api.signin(email, password)
            tokenRepository.save(token)
        }
        assertEquals(token, "any_token")
    }
}