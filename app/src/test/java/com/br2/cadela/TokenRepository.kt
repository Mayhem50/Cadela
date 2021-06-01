package com.br2.cadela

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.security.InvalidParameterException

class TokenDao {
    fun save(token: String, userId: String) {}
}

class TokenRepository(private val tokenDao: TokenDao) {
    fun save(token: String, userId: String) {
        if (userId.isEmpty()) {
            throw InvalidParameterException("Empty user id")
        }
        if (token.isEmpty()) {
            throw InvalidParameterException("Empty token")
        }

        tokenDao.save(token, userId)
    }
}

class TokenRepositoryTest : TokenRepositoryContract() {
    override val sut: TokenRepository
        get() = _sut

    private val tokenDao = mockk<TokenDao>()
    private var _sut: TokenRepository

    init {
        every { tokenDao.save(any(), any()) } returns mockk()
        _sut = TokenRepository(tokenDao)
    }

    @Test
    fun `Ensure Room client is called with right parameters`() {
        sut.save("any_token", "any_user_id")
        verify { tokenDao.save(any(), any()) }
    }
}