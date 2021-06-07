package com.br2.cadela.workout

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

class SessionRepositoryTest {
    private lateinit var sessionDao: SessionDao
    private lateinit var sut: SessionRepository

    @BeforeEach
    fun setup(){
        sessionDao = spyk()
        sut = SessionRepository(sessionDao)
    }

    @Test
    fun `Load last session from Dao`() {
        val record: SessionRecord = SessionRecord("any_id", Session.FIRST_LEVEL_TEST, LocalDate.parse("2021-05-15"), LocalDate.parse("2021-05-15"))

        every { sessionDao.getLastSession() } returns record
        val session = sut.getLastSession()
        verify { sessionDao.getLastSession() }
        assertEquals(record.session, session)
    }

    @Test
    fun `Save session`() {
        sut.saveSession(Session.FIRST_LEVEL_TEST)
        verify { sessionDao.save(SessionRecord(session = Session.FIRST_LEVEL_TEST)) }
    }
}