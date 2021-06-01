package com.br2.cadela


import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.security.InvalidParameterException

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
        if(email.isEmpty()){
            throw InvalidParameterException("Empty email")
        }
        if(password.isEmpty()){
            throw InvalidParameterException("Empty password")
        }
        val response = api.signin(email, password)
        tokenRepository.save(response)
        return response
    }
}

class SinginTest {
    private val api = spyk<Api>()
    private val tokenRepository = spyk<TokenRepository>()
    private val sut = SigninService(api, tokenRepository)

    private val email = "any_email@mail.com"
    private val password = "any_password"

    @Test
    fun `Save token for userId if valid email & password provided`() {
        val token = sut.signin(email, password)
        verify {
            api.signin(email, password)
            tokenRepository.save(token)
        }
        assertEquals(token, "any_token")
    }

    @Test
    fun `Throw exception if email empty`(){
        val exception = assertThrows<InvalidParameterException> { sut.signin("", password) }
        assertEquals("Empty email", exception.message)
    }

    @Test
    fun `Throw exception if password empty `(){
        val exception = assertThrows<InvalidParameterException> { sut.signin(email, "") }
        assertEquals("Empty password", exception.message)
    }
}