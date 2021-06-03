package com.br2.cadela

import com.br2.cadela.authentication.signin.TokenRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.security.InvalidParameterException

abstract class TokenRepositoryContract {
    protected abstract val sut: TokenRepository

    @Test
    fun `Save token for specific user id`() {
        assertDoesNotThrow { sut.save("any_token", "any_user_id") }
    }

    @Test
    fun `Throw if user id is empty`() {
        val exception = assertThrows<InvalidParameterException> { sut.save("any_token", "") }
        Assertions.assertEquals("Empty user id", exception.message)
    }

    @Test
    fun `Throw if token is empty`() {
        val exception = assertThrows<InvalidParameterException> { sut.save("", "any_user_id") }
        Assertions.assertEquals("Empty token", exception.message)
    }
}