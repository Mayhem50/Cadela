package com.br2.cadela.workout

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import kotlin.streams.toList

class WorkoutTest {
    private val sut = WorkoutService()

    @Test
    fun `When creating a new Session a session that contains all exercises & rest between exercises  return`() {
        val session = sut.createNewSession()
        assertFalse(session.exercises.isEmpty())
        assertFalse(session.restsBetweenExercises.isEmpty())
    }

    @Test
    fun `The new session contains n exercises and n-1 rest between them`() {
        val session = sut.createNewSession()
        assertEquals(1, session.exercises.size - session.restsBetweenExercises.size)
    }

    @Test
    fun `The first session is a Test session that contains exercise A, B, C, A1 and rests between are 3min`() {
        val session = sut.createNewSession()

        assertEquals("1st Level Test", session.name)
        assertEquals(
            listOf("A", "B", "C", "A1"),
            session.exercises.stream().map { it.name }.toList()
        )
        assertEquals(
            listOf(180, 180, 180),
            session.restsBetweenExercises.stream().map { it.duration }.toList()
        )
    }

    @Test
    fun `When session result on test session is under 4 for exercise B next session wil be First Program`() {
        val sessionResult = SessionResult(listOf(Exercise("B", 3)))
        val session = sut.createNewSession(sessionResult)

        assertEquals("1st Program", session.name)
        assertEquals(
            listOf("A", "D", "C1", "E", "F", "G", "K2"),
            session.exercises.stream().map { it.name }.toList()
        )
        assertEquals(
            List(6) { 120 },
            session.restsBetweenExercises.stream().map { it.duration }.toList()
        )
    }

    @Test
    fun `When session result on test session is over or equal 4 for exercise B next session wil be First Program`() {
        val sessionResult = SessionResult(listOf(Exercise("B", 4)))
        val session = sut.createNewSession(sessionResult)

        assertEquals("2nd Program", session.name)
        assertEquals(
            listOf("B", "A1", "D", "C1", "E", "F", "G", "K2"),
            session.exercises.stream().map { it.name }.toList()
        )
        assertEquals(
            List(7) { 120 },
            session.restsBetweenExercises.stream().map { it.duration }.toList()
        )
    }
}

class WorkoutService {
    fun createNewSession(sessionResult: SessionResult? = null): Session {
        sessionResult?.let {
            val repetitions = sessionResult.exercises.find { it.name == "B" }?.repetitions ?: 0
            return if (repetitions < 4) {
                Session.FIRST_PROGRAM
            } else {
                Session.SECOND_PROGRAM
            }
        }

        return Session.FIRST_LEVEL_TEST
    }
}

data class SessionResult(val exercises: List<Exercise>)
