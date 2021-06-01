package com.br2.cadela


import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.io.IOException
import java.lang.Exception
import java.security.InvalidParameterException

data class User(val id: String, val firstName: String, val lastName: String)
data class SigninResponse(val user: User, val token: String)

class ApiException : Exception() {}

class Api {
    fun signin(email: String, password: String): SigninResponse {
        return SigninResponse(User("any_user_id", "John", "McLane"), "any_token")
    }
}

class TokenRepository {
    fun save(token: String, userId: String) {
        if(userId.isEmpty()){
            throw InvalidParameterException("Empty user id")
        }
        if(token.isEmpty()){
            throw InvalidParameterException("Empty token")
        }
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
        tokenRepository.save(response.token, response.user.id)
        return response.token
    }
}

class SinginServiceTest {
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
            tokenRepository.save(token, "any_user_id")
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
        every { failRepository.save("any_token", "any_user_id") } throws  IOException()
        assertThrows<IOException> { sut.signin(email, password) }
    }
}

class TokenRepositoryTest {
    private val sut =  TokenRepository()

    @Test
    fun `Save token for specific user id`(){
        assertDoesNotThrow { sut.save("any_token", "any_user_id") }
    }

    @Test
    fun `Throw if user id is empty`(){
        val exception = assertThrows<InvalidParameterException> { sut.save("any_token", "") }
        assertEquals("Empty user id", exception.message)
    }

    @Test
    fun `Throw if token is empty`(){
        val exception = assertThrows<InvalidParameterException> { sut.save("", "any_user_id") }
        assertEquals("Empty token", exception.message)
    }
}
