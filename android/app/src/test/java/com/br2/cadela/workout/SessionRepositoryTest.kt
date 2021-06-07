package com.br2.cadela.workout

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SessionRepositoryTest {
    private lateinit var sessionDao: SessionDao
    private lateinit var sut: SessionRepository

    @BeforeEach
    fun setup(){
        sessionDao = mockk()
        sut = SessionRepository(sessionDao)
    }

    @Test
    fun `Load last session from Dao`() {
        every { sessionDao.getLastSession() } returns null

        sut.getLastSession()
        verify { sessionDao.getLastSession() }
    }
}