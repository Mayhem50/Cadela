package com.br2.cadela.workout

import androidx.lifecycle.Observer
import com.br2.cadela.InstantExecutorExtension
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(value = [InstantExecutorExtension::class])
class WorkoutViewModelTest {
    private lateinit var observer: Observer<Session?>
    private lateinit var exerciseObserver: Observer<Exercise?>
    private lateinit var sessionDao: SessionDao
    private lateinit var sessionRepository: SessionRepository
    private lateinit var workoutService: WorkoutService

    private lateinit var sut: WorkoutViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(Dispatchers.Default)
        observer = mockk()
        exerciseObserver = mockk()
        sessionDao = mockk()

        coEvery { sessionDao.getLastSession() } returns SessionRecord(0, Session.FIRST_LEVEL_TEST)
        coEvery { sessionDao.save(any()) } returns Unit
        every { observer.onChanged(any()) } returns Unit
        every { exerciseObserver.onChanged(any()) } returns Unit

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
        sut.startSession().join()
        coVerify { workoutService.startNewSession() }
        verify { observer.onChanged(any()) }
    }

    @Test
    fun `End session`() = runBlocking {
        sut.currentSession.observeForever(observer)
        sut.currentExercise.observeForever(exerciseObserver)
        sut.startSession().join()
        sut.endSession()
        coVerify { workoutService.endSession(any()) }
        verifyAll {
            observer.onChanged(any())
            observer.onChanged(null)
            exerciseObserver.onChanged(null)
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

    @Test
    fun `When run a new session the first exercise will be the current one`() = runBlocking  {
        sut.currentExercise.observeForever(exerciseObserver)
        sut.startSession().join()
        sut.runSession()
        verify { exerciseObserver.onChanged(Session.FIRST_LEVEL_TEST.exercises[0]) }
    }

    @Test
    fun `When move to next exercise current exercise is update to the next exercise of the list`() = runBlocking {
        sut.currentExercise.observeForever(exerciseObserver)
        sut.startSession().join()
        sut.runSession()
        sut.moveToNextExercise()
        verify { exerciseObserver.onChanged(Session.FIRST_LEVEL_TEST.exercises[1]) }
    }

    @Test
    fun `When move to next exercise and no more exercise current exercise is update to null`() = runBlocking {
        sut.currentExercise.observeForever(exerciseObserver)
        sut.startSession().join()
        sut.runSession()
        sut.moveToNextExercise()
        sut.moveToNextExercise()
        sut.moveToNextExercise()
        sut.moveToNextExercise()
        verify { exerciseObserver.onChanged(null) }
    }

    @Test
    fun `Update current serie current repetition`() = runBlocking {
        val doneReps = 5
        sut.startSession().join()
        sut.runSession()
        sut.setRepsForCurrentSerie(doneReps)
        assertEquals(doneReps, sut.currentExercise.value!!.series.repetitions[0])
    }


    @Test
    fun `Update current serie current repetition make move to next exercise if no more reps to do`() = runBlocking {
        sut.currentExercise.observeForever(exerciseObserver)
        coEvery { sessionDao.getLastSession() } returns SessionRecord(0, Session.FIRST_LEVEL_TEST)
        sut.startSession().join()
        sut.runSession()
        sut.setRepsForCurrentSerie(5)
        verify { exerciseObserver.onChanged(Session.FIRST_LEVEL_TEST.exercises[1]) }
    }
}