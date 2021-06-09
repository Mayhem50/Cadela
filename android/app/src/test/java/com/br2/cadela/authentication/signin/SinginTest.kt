package com.br2.cadela.authentication.signin


import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.IOException
import java.security.InvalidParameterException

class ApiException : Exception() {}

object Mocks {
    val repository = mockk<TokenRepository>()
    val api = mockk<SigninApi>()

    val DEFAULT_USER = User("any_user_id", "John", "McLane")

    fun init() {
        clearAllMocks()
        coEvery { repository.save(any(), any()) } returns mockk()
        coEvery { repository.save("", any()) } throws InvalidParameterException("Empty token")
        coEvery { repository.save(any(), "") } throws InvalidParameterException("Empty user id")

        coEvery { api.signin(any(), any()) } returns SigninResponse(
            DEFAULT_USER, "any_token"
        )
        coEvery { api.signin("", any()) } throws InvalidParameterException("Empty email")
        coEvery { api.signin(any(), "") } throws InvalidParameterException("Empty password")
    }
}

class SinginServiceTest {
    private val api = Mocks.api
    private val tokenRepository = Mocks.repository
    private var sut = SigninService(api, tokenRepository)

    private val email = "any_email@mail.com"
    private val password = "any_password"

    @BeforeEach
    fun setup(){
        Mocks.init()
    }

    @Test
    fun `Save token for userId if valid email & password provided`() = runBlocking {
        val token = sut.signin(email, password)
        coVerify {
            api.signin(email, password)
            tokenRepository.save(token, "any_user_id")
        }
        assertEquals(token, "any_token")
    }

    @Test
    fun `Throw exception if email empty`() = runBlocking  {
        val exception = assertThrows<InvalidParameterException> { sut.signin("", password) }
        assertEquals("Empty email", exception.message)
    }

    @Test
    fun `Throw exception if password empty `() = runBlocking  {
        val exception = assertThrows<InvalidParameterException> { sut.signin(email, "") }
        assertEquals("Empty password", exception.message)
    }

    @Test
    fun `Throw exception if api fails`() = runBlocking  {
        val failApi = mockk<SigninApi>()
        coEvery { failApi.signin(email, password) } throws ApiException()
        sut = SigninService(failApi, tokenRepository)
        assertThrows<ApiException> { sut.signin(email, password) }
        return@runBlocking
    }

    @Test
    fun `Throw exception if repository fails`() = runBlocking  {
        val failRepository = mockk<TokenRepository>()
        sut = SigninService(api, failRepository)
        coEvery { failRepository.save("any_token", "any_user_id") } throws IOException()
        val exception = assertThrows<IOException> { sut.signin(email, password) }
        return@runBlocking
    }

    @Test
    fun `Trim email`() = runBlocking {
        val nonTrimmedEmail = " any_email@mail.com "
        sut.signin(nonTrimmedEmail, password)
        coVerify {
            api.signin("any_email@mail.com", password)
        }
    }
}

class MockApiTest : SigninApiContract() {
    override val sut = Mocks.api
}

class MockTokenRepositoryContract : TokenRepositoryContract() {
    override val sut = Mocks.repository
    override fun setup() {
        Mocks.init()
    }
}
