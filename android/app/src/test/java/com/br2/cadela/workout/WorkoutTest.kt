package com.br2.cadela.workout

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class WorkoutTest {
    @Test
    fun `When creating a new Session a session that contains all exercises & rest between exercises  return`() {
        val sut = WorkoutService()
        val session = sut.createNewSession()
        assertFalse(session.exercises.isEmpty())
        assertFalse(session.restsBetweenExercises.isEmpty())
    }

    @Test
    fun `The new session contains n exercises and n-1 rest between them`() {
        val sut = WorkoutService()
        val session = sut.createNewSession()
        assertEquals(1, session.exercises.size - session.restsBetweenExercises.size)
    }

    @Test
    fun `The first session is a Test session that contains exercise A, B, C, A1 and rests between are 3min`() {
        val sut = WorkoutService()
        val session = sut.createNewSession()

        assertEquals(4, session.exercises.size)
        assertEquals("A", session.exercises[0].name)
        assertEquals("B", session.exercises[1].name)
        assertEquals("C", session.exercises[2].name)
        assertEquals("A1", session.exercises[3].name)

        assertEquals(180, session.restsBetweenExercises[0].duration)
        assertEquals(180, session.restsBetweenExercises[1].duration)
        assertEquals(180, session.restsBetweenExercises[2].duration)
    }
}

class WorkoutService {
    fun createNewSession(): Session {
        return Session(
            listOf(
                Exercise(name = "A"),
                Exercise(name = "B"),
                Exercise(name = "C"),
                Exercise(name = "A1")
            ),
            listOf(
                Rest(duration = 180),
                Rest(duration = 180),
                Rest(duration = 180)
            )
        )
    }
}

data class Session(val exercises: List<Exercise>, val restsBetweenExercises: List<Rest>)

data class Rest(val duration: Int)
data class Exercise(val name: String)
