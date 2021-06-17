package com.br2.cadela.workout

import com.br2.cadela.workout.datas.Repetition
import com.br2.cadela.workout.datas.Session
import com.br2.cadela.workout.domain.WorkoutService
import com.br2.cadela.workout.repositories.SessionDao
import com.br2.cadela.workout.repositories.SessionRepository
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

abstract class WorkoutTestBase {
    protected lateinit var sessionDao: SessionDao

    protected lateinit var sut: WorkoutService
    protected lateinit var sessionRepository: SessionRepository

    @BeforeEach
    fun setup() {
        clearAllMocks()
        sessionDao = mockk()
        coEvery { sessionDao.getLastSession() } returns null
        coEvery { sessionDao.save(any()) } returns Unit
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
    fun `Start new session`() = runBlocking {
        sut = spyk(WorkoutService(sessionRepository))
        sut.startNewSession()
        coVerify { sessionRepository.getLastSession() }
        verify { sut.createNewSession(null) }
    }

    @Test
    fun `Current session is finished`() = runBlocking {
        val session = sut.startNewSession()
        sut.endSession(session)
        coVerify { sessionRepository.saveSession(any()) }
    }

    @Test
    fun `Pause and resume current session`() = runBlocking {
        coEvery { sessionRepository.getLastSession() } returnsMany listOf(
            Session.FIRST_PROGRAM,
            makeIncompleteSession()
        )

        val currentSession = sut.startNewSession()
        currentSession.exercises[0].series.repetitions[0] = Repetition(12)
        sut.pauseSession(currentSession)
        coVerify { sessionRepository.saveSession(any()) }
        sut.startNewSession()
        assertEquals(makeIncompleteSession(), currentSession)
    }

    private fun makeIncompleteSession() = Session(
        Session.FIRST_PROGRAM.name,
        Session.FIRST_PROGRAM.exercises.mapIndexed { index, it ->
            if (index == 0) {
                it.series.repetitions[0] = Repetition(12)
            }
            it
        })
}

