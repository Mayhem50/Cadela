package com.br2.cadela.workout

import io.mockk.*
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class WorkoutTestBase {
    protected lateinit var sut: WorkoutService
    protected lateinit var sessionRepository: SessionRepository

    @BeforeEach
    fun setup() {
        val sessionDao = mockk<SessionDao>()
        every { sessionDao.getLastSession() } returns null
        every { sessionDao.save(any()) } returns Unit
        sessionRepository = spyk(SessionRepository(sessionDao))
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
        coVerify { sessionRepository.getLastSession() }
        verify { sut.createNewSession(null) }
        assertNotNull(sut.currentSession)
    }

    @Test
    fun `Current session is finished`() {
        sut.startNewSession()
        sut.endSession()
        coVerify { sessionRepository.saveSession(any()) }
        assertNull(sut.currentSession)
    }

    @Test
    fun `Pause and resume current session`() {
        coEvery { sessionRepository.getLastSession() } returnsMany listOf(
            Session.FIRST_PROGRAM,
            makeIncompleteSession()
        )

        sut.startNewSession()
        val currentSession = Session(
            sut.currentSession!!.name,
            sut.currentSession!!.exercises,
            sut.currentSession!!.levelStartedAt
        )
        currentSession.exercises[0].series.repetitions[0] = 12
        sut.pauseSession()
        coVerify { sessionRepository.saveSession(any()) }
        assertNull(sut.currentSession)
        sut.startNewSession()
        assertEquals(currentSession, sut.currentSession)
    }

    private fun makeIncompleteSession() = Session(
    Session.FIRST_PROGRAM.name,
    Session.FIRST_PROGRAM.exercises.mapIndexed { index, it ->
        if (index == 0) {
            it.series.repetitions[0] = 12
        }
        it
    })
}

