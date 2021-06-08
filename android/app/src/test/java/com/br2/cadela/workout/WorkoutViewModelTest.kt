package com.br2.cadela.workout

import androidx.lifecycle.Observer
import com.br2.cadela.InstantExecutorExtension
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(value = [InstantExecutorExtension::class])
class WorkoutViewModelTest {
    private lateinit var observer: Observer<Session?>
    private lateinit var sessionDao: SessionDao
    private lateinit var sessionRepository: SessionRepository
    private lateinit var workoutService: WorkoutService

    private lateinit var sut: WorkoutViewModel

    @BeforeEach
    fun setup() {
        observer = mockk()
        sessionDao = mockk()

        coEvery { sessionDao.getLastSession() } returns SessionRecord(0, Session.FIRST_LEVEL_TEST)
        coEvery { sessionDao.save(any()) } returns Unit
        every { observer.onChanged(any()) } returns Unit

        sessionRepository = SessionRepository(sessionDao)
        workoutService = spyk(WorkoutService(sessionRepository))
        sut = WorkoutViewModel(workoutService)
    }

    @AfterEach
    fun clean() {
        clearAllMocks()
    }

    @Test
    fun `Start new session`() = runBlocking {
        sut.currentSession.observeForever(observer)
        sut.startSession()
        coVerify { workoutService.startNewSession() }
        verify { observer.onChanged(any()) }
    }

    @Test
    fun `End session`() = runBlocking {
        sut.currentSession.observeForever(observer)
        sut.startSession().join()
        sut.endSession()
        coVerify { workoutService.endSession(any()) }
        verifyAll {
            observer.onChanged(any())
            observer.onChanged(null)
        }
    }

    @Test
    fun `Pause session`() = runBlocking {
        sut.currentSession.observeForever(observer)
        sut.startSession().join()
        sut.pauseSession()
        coVerify { workoutService.pauseSession(any()) }
        verifyAll {
            observer.onChanged(any())
        }
    }
}