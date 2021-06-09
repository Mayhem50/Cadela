package com.br2.cadela.authentication.signin

import com.br2.cadela.authentication.signin.TokenRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import java.security.InvalidParameterException

abstract class TokenRepositoryContract {
    protected abstract val sut: TokenRepository

    @BeforeEach
    abstract fun setup()

    @Test
    fun `Save token for specific user id`() {
        assertDoesNotThrow { runBlocking { sut.save("any_token", "any_user_id") } }
    }

    @Test
    fun `Throw if user id is empty`() = runBlocking {
        val exception = assertThrows<InvalidParameterException> { sut.save("any_token", "") }
        Assertions.assertEquals("Empty user id", exception.message)
    }

    @Test
    fun `Throw if token is empty`() = runBlocking {
        val exception = assertThrows<InvalidParameterException> { sut.save("", "any_user_id") }
        Assertions.assertEquals("Empty token", exception.message)
    }
}