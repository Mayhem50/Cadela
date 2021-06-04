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
}

class WorkoutService {
    fun createNewSession(): Session {
        return Session(listOf(Exercise()), listOf(Rest()))
    }
}

data class Session(val exercises: List<Exercise>, val restsBetweenExercises: List<Rest>)

class Rest {}
class Exercise{}
