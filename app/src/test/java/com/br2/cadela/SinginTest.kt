package com.br2.cadela


import com.br2.cadela.authentication.signin.SigninService
import com.br2.cadela.authentication.signin.TokenRepository
import com.br2.cadela.authentication.signin.User
import com.br2.cadela.shared.Api
import com.br2.cadela.shared.SigninResponse
import io.mockk.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.io.IOException
import java.security.InvalidParameterException

class ApiException : Exception() {}

object Mocks {
    val repository = mockk<TokenRepository>()
    val api = mockk<Api>()

    val DEFAULT_USER = User("any_user_id", "John", "McLane")

    init {
        every { repository.save(any(), any()) } returns mockk()
        every { repository.save("", any()) } throws InvalidParameterException("Empty token")
        every { repository.save(any(), "") } throws InvalidParameterException("Empty user id")

        every { api.signin(any(), any()) } returns SigninResponse(
            DEFAULT_USER, "any_token"
        )
        every { api.signin("", any()) } throws InvalidParameterException("Empty email")
        every { api.signin(any(), "") } throws InvalidParameterException("Empty password")
    }
}

class SinginServiceTest {
    private val api = Mocks.api
    private val tokenRepository = Mocks.repository
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
    fun `Throw exception if email empty`() {
        val exception = assertThrows<InvalidParameterException> { sut.signin("", password) }
        assertEquals("Empty email", exception.message)
    }

    @Test
    fun `Throw exception if password empty `() {
        val exception = assertThrows<InvalidParameterException> { sut.signin(email, "") }
        assertEquals("Empty password", exception.message)
    }

    @Test
    fun `Throw exception if api fails`() {
        val failApi = mockk<Api>()
        every { failApi.signin(email, password) } throws ApiException()
        sut = SigninService(failApi, tokenRepository)
        assertThrows<ApiException> { sut.signin(email, password) }
    }

    @Test
    fun `Throw exception if repository fails`() {
        val failRepository = mockk<TokenRepository>()
        sut = SigninService(api, failRepository)
        every { failRepository.save("any_token", "any_user_id") } throws IOException()
        assertThrows<IOException> { sut.signin(email, password) }
    }
}

class SigninApiTest {

    private val sut = Mocks.api

    @Test
    fun `Get signin response for specific user email`() {
        val response = sut.signin("any_email@email.com", "any_password")
        assertEquals("any_token", response.token)
        assertEquals(Mocks.DEFAULT_USER, response.user)
    }

    @Test
    fun `Throw if email is empty`() {
        val exception = assertThrows<InvalidParameterException> { sut.signin("", "any_password") }
        Assertions.assertEquals("Empty email", exception.message)
    }

    @Test
    fun `Throw if token is empty`() {
        val exception =
            assertThrows<InvalidParameterException> { sut.signin("any_email@mail.com", "") }
        Assertions.assertEquals("Empty password", exception.message)
    }
}

class MockTokenRepositoryContract : TokenRepositoryContract() {
    override val sut = Mocks.repository
}
