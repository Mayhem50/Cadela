package com.br2.cadela.workout

import android.os.CountDownTimer
import androidx.lifecycle.Observer
import com.br2.cadela.InstantExecutorExtension
import com.br2.cadela.workout.datas.*
import com.br2.cadela.workout.domain.WorkoutService
import com.br2.cadela.workout.repositories.SessionDao
import com.br2.cadela.workout.repositories.SessionRecord
import com.br2.cadela.workout.repositories.SessionRepository
import com.br2.cadela.workout.views.CountdownTimerFactory
import com.br2.cadela.workout.views.WorkoutViewModel
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
    private lateinit var restObserver: Observer<Rest?>
    private lateinit var sessionDao: SessionDao
    private lateinit var sessionRepository: SessionRepository
    private lateinit var workoutService: WorkoutService

    private lateinit var sut: WorkoutViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(Dispatchers.Default)
        observer = mockk()
        exerciseObserver = mockk()
        restObserver = mockk()
        sessionDao = mockk()

        coEvery { sessionDao.getLastSession() } returns SessionRecord(Session.FIRST_LEVEL_TEST)
        coEvery { sessionDao.save(any()) } returns Unit
        every { observer.onChanged(any()) } returns Unit
        every { exerciseObserver.onChanged(any()) } returns Unit
        every { restObserver.onChanged(any()) } returns Unit

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
        assertEquals(null, sut.currentSession.value)
        assertEquals(null, sut.currentExercise.value)
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
    fun `When run a new session the first exercise will be the current one`() = runBlocking {
        sut.currentExercise.observeForever(exerciseObserver)
        sut.startSession().join()
        sut.runSession()
        verify { workoutService.runSession() }
        verify { exerciseObserver.onChanged(Session.FIRST_LEVEL_TEST.exercises[0]) }
    }

    @Test
    fun `When move to next exercise and no more exercise current exercise is update to null`() =
        runBlocking {
            val session = Session(
                name = "dummy_name",
                exercises = listOf(
                    Exercise(
                        name = "A", series = Series(
                            count = 1, repetitions = mutableListOf(
                                Repetition(0)
                            ), restAfter = Rest(0)
                        ), restAfter = Rest(0)
                    )
                )
            )
            val spyOnNextSerie = spyk<() -> Unit>("onNextSerie")
            val spyOnNextExercise = spyk<() -> Unit>("onNextExercise")
            val spyOnEndSession = spyk<() -> Unit>("onEnd")
            coEvery { sessionDao.getLastSession() } returns SessionRecord(session)
            sut.currentExercise.observeForever(exerciseObserver)
            sut.startSession().join()
            sut.runSession()
            sut.moveToNext({spyOnNextSerie()}, { spyOnNextExercise() }, { spyOnEndSession() })
            verify { spyOnEndSession() }
            verify(exactly = 0) { spyOnNextSerie() }
            verify(exactly = 0) { spyOnNextExercise() }
        }

    @Test
    fun `Update current serie current repetition`() = runBlocking {
        val doneReps = 5
        val session = Session(
            name = "dummy_name",
            exercises = listOf(
                Exercise(
                    name = "A",
                    series = Series(count = 1, restAfter = Rest(0)),
                    restAfter = Rest(0)
                ),
                Exercise(
                    name = "B",
                    series = Series(count = 1, restAfter = Rest(0)),
                    restAfter = Rest(0)
                )
            )
        )
        coEvery { sessionDao.getLastSession() } returns SessionRecord(session)

        sut.startSession().join()
        sut.runSession()
        val currentExercise = sut.currentExercise.value!!
        sut.setRepsForCurrentSerie(doneReps)

        assertEquals(doneReps, currentExercise.series.repetitions[0].done)
    }

    @Test
    fun `When exercise have more than 1 serie moveToNext move to next serie`() =
        runBlocking {
            val session = Session(
                name = "dummy_name",
                exercises = listOf(
                    Exercise(
                        name = "A",
                        series = Series(count = 2, restAfter = Rest(1)),
                        restAfter = Rest(3)
                    ),
                    Exercise(
                        name = "B",
                        series = Series(count = 2, restAfter = Rest(2)),
                        restAfter = Rest(4)
                    )
                )
            )

            val spyOnNextSerie = spyk<() -> Unit>("onNextSerie")
            val spyOnNextExercise = spyk<() -> Unit>("onNextExercise")
            val spyOnEndSession = spyk<() -> Unit>("onEnd")
            coEvery { sessionDao.getLastSession() } returns SessionRecord(session)

            sut.startSession().join()
            sut.runSession()
            sut.moveToNext({spyOnNextSerie()}, { spyOnNextExercise() }, { spyOnEndSession() })

            verify { spyOnNextSerie() }
            verify(exactly = 0) { spyOnEndSession() }
            verify(exactly = 0) { spyOnNextExercise() }
            assertEquals(1, sut.currentSerieIndex.value)
        }

    @Test
    fun `When move to next serie update rest time with serie's rest time`() =
        runBlocking {
            val countdownTimerFactory = spyk<CountdownTimerFactory>()

            sut = WorkoutViewModel(workoutService, countdownTimerFactory)
            sut.currentExercise.observeForever(exerciseObserver)
            val expectedRestInSec = 10
            val expectedRestInMs = expectedRestInSec * 1000
            val otherRest = 100

            val session = Session(
                name = "dummy_name",
                exercises = listOf(
                    Exercise(
                        name = "A",
                        series = Series(count = 3, restAfter = Rest(expectedRestInSec)),
                        restAfter = Rest(otherRest)
                    ),
                    Exercise(
                        name = "B",
                        series = Series(count = 2, restAfter = Rest(otherRest)),
                        restAfter = Rest(otherRest)
                    )
                )
            )
            coEvery { sessionDao.getLastSession() } returns SessionRecord(session)

            sut.startSession().join()
            sut.runSession()
            sut.moveToNext({}, {}, {})
            sut.startRest()

            verify { countdownTimerFactory.createCountDownTimer(expectedRestInMs, any(), any()) }
        }

    @Test
    fun `When move to next exercise update rest time with exercise's rest time`() =
        runBlocking {
            val countdownTimerFactory = spyk<CountdownTimerFactory>()
            sut = WorkoutViewModel(workoutService, countdownTimerFactory)
            sut.currentExercise.observeForever(exerciseObserver)
            val expectedRestInSec = 10
            val expectedRestInMs = expectedRestInSec * 1000
            val otherRest = 100
            val session = Session(
                name = "dummy_name",
                exercises = listOf(
                    Exercise(
                        name = "A",
                        series = Series(count = 1, restAfter = Rest(otherRest)),
                        restAfter = Rest(expectedRestInSec)
                    ),
                    Exercise(
                        name = "B",
                        series = Series(count = 2, restAfter = Rest(otherRest)),
                        restAfter = Rest(otherRest)
                    )
                )
            )
            coEvery { sessionDao.getLastSession() } returns SessionRecord(session)

            sut.startSession().join()
            sut.runSession()
            sut.moveToNext({}, {}, {})
            sut.startRest()

            verify { countdownTimerFactory.createCountDownTimer(expectedRestInMs, any(), any()) }
        }
}