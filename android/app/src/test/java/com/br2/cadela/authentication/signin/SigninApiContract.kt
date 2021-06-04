package com.br2.cadela.authentication.signin

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.security.InvalidParameterException

abstract class SigninApiContract {
    protected abstract val sut: SigninApi

    @Test
    fun `Get signin response for specific user email`() = runBlocking {
        val response = sut.signin("any_email@email.com", "any_password")
        Assertions.assertEquals("any_token", response.token)
        Assertions.assertEquals(Mocks.DEFAULT_USER, response.user)
    }

    @Test
    fun `Throw if email is empty`() = runBlocking {
        val exception = assertThrows<InvalidParameterException> { sut.signin("", "any_password") }
        Assertions.assertEquals("Empty email", exception.message)
    }

    @Test
    fun `Throw if token is empty`() = runBlocking {
        val exception =
            assertThrows<InvalidParameterException> { sut.signin("any_email@mail.com", "") }
        Assertions.assertEquals("Empty password", exception.message)
    }
}