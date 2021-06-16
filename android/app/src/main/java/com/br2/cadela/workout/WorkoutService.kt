package com.br2.cadela.workout

import com.br2.cadela.workout.datas.*
import com.br2.cadela.workout.repositories.SessionRepository
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class WorkoutService(private val sessionRepository: SessionRepository) {
    private var _currentExerciseIndex: Int = 0
    private var _currentSession: Session? = null
    private val TWO_WEEKS = 2

    fun createNewSession(previousSession: Session? = null): Session {
        val nextSession = when (previousSession?.name) {
            "first_level_test" -> nextSessionAfterFirstLevelTest(previousSession)
            "first_program" -> nextSessionAfter1stProgram(previousSession)
            "only_b_test" -> nextSessionAfterOnlyBTest(previousSession)
            "second_program" -> nextSessionAfter2ndProgram(previousSession)
            else -> Session.FIRST_LEVEL_TEST
        }

        return if (stillOnSameLevel(previousSession, nextSession)
        ) {
            nextSession.clone(previousSession?.levelStartedAt)
        } else nextSession
    }

    private fun nextSessionAfter2ndProgram(previousSession: Session): Session {
        val repetition = previousSession.exercises[0].series.repetitions[0].done
        return if (repetition < 8) Session.SECOND_PROGRAM else Session.SECOND_LEVEL
    }

    private fun stillOnSameLevel(
        previousSession: Session?,
        nextSession: Session
    ) = previousSession?.name == nextSession.name ||
            (stillOnFirstLevel(previousSession, nextSession))

    private fun stillOnFirstLevel(
        previousSession: Session?,
        nextSession: Session
    ) =
        previousSession?.name == Session.FIRST_PROGRAM.name && nextSession.name == Session.SECOND_PROGRAM.name

    suspend fun startNewSession(): Session = withContext(Dispatchers.IO) {
        val lastSession = sessionRepository.getLastSession()
        _currentSession = if (lastSession?.isComplete == false) lastSession
        else createNewSession(lastSession)
        return@withContext _currentSession!!
    }

    suspend fun endSession(session: Session) {
        saveAndClearCurrentSession(session)
    }

    suspend fun pauseSession(session: Session) {
        saveAndClearCurrentSession(session)
    }

    private fun nextSessionAfterOnlyBTest(previousSession: Session): Session {
        if (previousSession.exercises[0].series.repetitions[0].done >= 8) {
            return Session.SECOND_LEVEL
        }
        if (previousSession.exercises[0].series.repetitions[0].done > 5) {
            return Session.SECOND_PROGRAM
        }
        return Session.FIRST_PROGRAM_WITH_B1
    }

    private fun nextSessionAfter1stProgram(previousSession: Session): Session {
        if (sessionIsStartedSince2Weeks(previousSession)) {
            return Session.SECOND_PROGRAM
        }
        val exercises = previousSession.exercises.toMutableList()

        changeExercise("C4", "C5", 12, exercises)
        changeExercise("C5", "C6", 12, exercises)
        changeExercise("C6", "C1", 12, exercises)

        changeExercise("A1", "A2", 8, exercises)
        changeExercise("A3", "A4", 8, exercises)
        changeExercise("A4", "A5", 8, exercises)
        changeExercise("A5", "A6", 8, exercises)

        exercises.find { it.name == "A2" }?.let {
            if (shouldReplaceA2(it, exercises)) {
                exercises.add(0, Exercise("A3", Series(2), Rest(120)))
            }
        }

        exercises.find { it.name == "A6" }?.let {
            if (it.series.repetitions[0].done >= 8) {
                return Session.ONLY_B_TEST
            }
        }

        return Session(previousSession.name, exercises.map {
            Exercise(it.name, Series(it.series.count), it.restAfter)
        }.toList())
    }

    private fun sessionIsStartedSince2Weeks(previousSession: Session) =
        ChronoUnit.WEEKS.between(previousSession.levelStartedAt, LocalDate.now()) >= TWO_WEEKS

    private fun shouldReplaceA2(
        exercise: Exercise,
        exercises: List<Exercise>
    ) = exercise.series.repetitions[0].done >= 8 && exercises.find {
        listOf("A4", "A5", "A6").contains(
            it.name
        )
    } == null

    private fun changeExercise(
        searchName: String,
        replaceBy: String,
        threshold: Int,
        exercises: MutableList<Exercise>
    ) {
        val index = exercises.indexOfFirst { it.name == searchName }
        if (index >= 0) {
            val exercise = exercises[index]
            if (exercise.series.repetitions[0].done >= threshold) {
                exercises[index] =
                    Exercise(replaceBy, Series(exercise.series.count), exercise.restAfter)
            }
        }
    }

    private fun nextSessionAfterFirstLevelTest(previousSession: Session): Session {
        val repetitionsForB =
            previousSession.exercises.find { it.name == "B" }?.series?.repetitions?.elementAt(0)
                ?: Repetition(0)
        val repetitionsForC =
            previousSession.exercises.find { it.name == "C" }?.series?.repetitions?.elementAt(0)
                ?: Repetition(0)
        return if (repetitionsForB.done < 4) {
            return if (repetitionsForC.done > 0) Session.FIRST_PROGRAM else Session.FIRST_PROGRAM_WITH_C4
        } else {
            Session.SECOND_PROGRAM
        }
    }

    private suspend fun saveAndClearCurrentSession(session: Session) {
        sessionRepository.saveSession(session)
        _currentSession = null
    }

    fun moveToNextExercise(): Exercise? {
        return _currentSession?.let {
            _currentExerciseIndex += 1
            if (_currentExerciseIndex < it.exercises.size) it.exercises[_currentExerciseIndex]
            else null
        }
    }

    fun runSession(): Exercise? {
        _currentExerciseIndex = -1
        return moveToNextExercise()
    }
}