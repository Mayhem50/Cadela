package com.br2.cadela

import com.br2.cadela.authentication.signin.TokenDao
import com.br2.cadela.authentication.signin.TokenRecord
import com.br2.cadela.authentication.signin.TokenRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class TokenRepositoryTest : TokenRepositoryContract() {
    override val sut: TokenRepository
        get() = _sut

    private val tokenDao = mockk<TokenDao>()
    private var _sut: TokenRepository

    private val token = "any_token"
    private val userId = "any_user_id"
    private val tokenRecord = TokenRecord(token, userId)

    init {
        every { tokenDao.save(tokenRecord) } returns mockk()
        _sut = TokenRepository(tokenDao)
    }

    @Test
    fun `Ensure Room client is called with right parameters`() {
        sut.save(token, userId)
        verify { tokenDao.save(tokenRecord) }
    }
}