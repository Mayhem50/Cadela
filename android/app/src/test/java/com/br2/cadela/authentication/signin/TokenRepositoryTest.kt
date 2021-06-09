package com.br2.cadela.authentication.signin

import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class TokenRepositoryTest : TokenRepositoryContract() {
    override val sut: TokenRepository
        get() = _sut

    override fun setup() {

    }

    private val tokenDao = mockk<TokenDao>()
    private var _sut: TokenRepository

    private val token = "any_token"
    private val userId = "any_user_id"
    private val tokenRecord = TokenRecord(token, userId)

    init {
        coEvery { tokenDao.save(tokenRecord) } returns mockk()
        _sut = TokenRepository(tokenDao)
    }

    @Test
    fun `Ensure Room client is called with right parameters`() = runBlocking {
        sut.save(token, userId)
        coVerify { tokenDao.save(tokenRecord) }
    }
}