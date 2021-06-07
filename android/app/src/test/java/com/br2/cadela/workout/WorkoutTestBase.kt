package com.br2.cadela.workout

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class WorkoutTestBase {
    protected lateinit var sut: WorkoutService
    protected val sessionRepository = mockk<SessionRepository>()

    @BeforeEach
    fun setup(){
        every { sessionRepository.getLastSession() } returns null
        sut = WorkoutService(sessionRepository)
    }
}

class WorkoutTest : WorkoutTestBase() {
    @Test
    fun `When creating a new Session a session that contains all exercises & rest between exercises  return`() {
        val session = sut.createNewSession()
        assertFalse(session.exercises.isEmpty())
    }

    @Test
    fun `Start new session`() {
        sut = spyk(WorkoutService(sessionRepository))
        sut.startNewSession()
        verify { sessionRepository.getLastSession() }
        verify { sut.createNewSession(null) }
        assertNotNull(sut.currentSession)
    }
}

