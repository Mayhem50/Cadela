package com.br2.cadela.workout.domain

import com.br2.cadela.workout.datas.*
import com.br2.cadela.workout.repositories.SessionRepository
import kotlinx.coroutines.*
import java.lang.Integer.max
import java.time.LocalDate

// TODO: Handle beginning of level -> 4 sessions/week then 3 sessions/week
// TODO: Change 2nd Program to 2nd Level if doing session less than 3/week

class WorkoutService(private val sessionRepository: SessionRepository) {
    private var _currentExerciseIndex: Int = 0
    private var _currentSession: Session? = null

    fun createNewSession(previousSession: Session? = null): Session {
        val nextSession = when (previousSession?.name) {
            "first_level_test" -> nextSessionAfterFirstLevelTest(previousSession)
            "first_program" -> nextSessionAfter1stProgram(previousSession)
            "only_b_test" -> nextSessionAfterOnlyBTest(previousSession)
            "second_program" -> nextSessionAfter2ndProgram(previousSession)
            "level_2" -> nextSessionAfterLevel2(previousSession)
            else -> Session.FIRST_LEVEL_TEST
        }

        return if (stillOnSameLevel(previousSession, nextSession)
        ) {
            nextSession.clone(previousSession?.levelStartedAt).updateTargetRepsBasedOnPreviousSession(previousSession)
        } else nextSession
    }

    suspend fun startNewSession(): Session = withContext(Dispatchers.IO) {
        val lastSession = sessionRepository.getLastSession()
        _currentSession = if (lastSession?.isComplete == false) lastSession
        else createNewSession(lastSession)
        return@withContext _currentSession!!
    }

    suspend fun endSession(session: Session) {
        saveAndClearCurrentSession(session, LocalDate.now())
    }

    suspend fun pauseSession(session: Session) {
        saveAndClearCurrentSession(session)
    }

    private suspend fun saveAndClearCurrentSession(session: Session, endDate: LocalDate? = null)  {
        sessionRepository.saveSession(session, endDate)
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
        _currentExerciseIndex = _currentSession?.currentExerciseIndex ?: _currentExerciseIndex
        return _currentSession?.exercises?.elementAt(_currentExerciseIndex)
    }
}


