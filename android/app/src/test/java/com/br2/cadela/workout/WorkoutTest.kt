package com.br2.cadela.workout

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class WorkoutTest {
    @Test
    fun `When creating a new Session a session that contains all exercises & rest between exercises  return`(){
        val sut = WorkoutService()
        val session = sut.createNewSession()
        assertFalse(session.exercises.isEmpty())
        assertFalse(session.restsBetweenExercises.isEmpty())
    }

    @Test
    fun `The new session contains n exercises and n-1 rest between them`(){
        val sut = WorkoutService()
        val session = sut.createNewSession()
        assertEquals(1, session.exercises.size - session.restsBetweenExercises.size)
    }
}

class WorkoutService {
    fun createNewSession(): Session {
        return Session(listOf(Exercise(), Exercise()), listOf(Rest()))
    }
}

data class Session(val exercises: List<Exercise>, val restsBetweenExercises: List<Rest>)

class Rest {}
class Exercise{}
