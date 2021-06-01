package com.br2.cadela


import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException
import java.lang.Exception
import java.security.InvalidParameterException

class ApiException : Exception() {}

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
        val token = api.signin(email, password)
        tokenRepository.save(token)
        return token
    }
}

class SinginTest {
    private val api = spyk<Api>()
    private val tokenRepository = spyk<TokenRepository>()
    private var sut = SigninService(api, tokenRepository)

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

    @Test
    fun `Throw exception if api fails`(){
        val failApi = mockk<Api>()
        every { failApi.signin(email, password) } throws  ApiException()
        sut = SigninService(failApi, tokenRepository)
        assertThrows<ApiException> { sut.signin(email, password) }
    }

    @Test
    fun `Throw exception if repository fails`(){
        val failRepository = mockk<TokenRepository>()
        sut = SigninService(api, failRepository)
        every { failRepository.save("any_token") } throws  IOException()
        assertThrows<IOException> { sut.signin(email, password) }
    }
}
