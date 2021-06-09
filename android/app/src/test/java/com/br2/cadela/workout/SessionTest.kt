package com.br2.cadela.workout

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SessionTest {
    @Test
    fun `Session is complete if exercise's repetition contains one or more value more than 0`() {
        val completeSession = Session("any_name", listOf(
            Exercise("any_name", Series(2, mutableListOf(1, 1)), Rest(0)),
            Exercise("any_name", Series(2, mutableListOf(1, 1)), Rest(0)),
            Exercise("any_name", Series(2, mutableListOf(1, 1)), Rest(0))
        ))
        val anotherCompleteSession = Session("any_name", listOf(
            Exercise("any_name", Series(2, mutableListOf(1, 0)), Rest(0)),
            Exercise("any_name", Series(2, mutableListOf(1, 1)), Rest(0)),
            Exercise("any_name", Series(2, mutableListOf(1, 1)), Rest(0))
        ))
        val incompleteSession = Session("any_name", listOf(
            Exercise("any_name", Series(2, mutableListOf(0, 0)), Rest(0)),
            Exercise("any_name", Series(2, mutableListOf(1, 1)), Rest(0)),
            Exercise("any_name", Series(2, mutableListOf(1, 1)), Rest(0))
        ))

        assertTrue(completeSession.isComplete)
        assertTrue(anotherCompleteSession.isComplete)
        assertFalse(incompleteSession.isComplete)
    }

    @Test
    fun `Session is not started if all exercise's series repetitions are at 0`() {
        val startedSession = Session("any_name", listOf(
            Exercise("any_name", Series(2, mutableListOf(1, 1)), Rest(0)),
            Exercise("any_name", Series(2, mutableListOf(0, 0)), Rest(0)),
            Exercise("any_name", Series(2, mutableListOf(0, 0)), Rest(0)),
        ))
        val notStartedSession = Session("any_name", listOf(
            Exercise("any_name", Series(2, mutableListOf(0, 0)), Rest(0)),
            Exercise("any_name", Series(2, mutableListOf(0, 0)), Rest(0)),
            Exercise("any_name", Series(2, mutableListOf(0, 0)), Rest(0)),
            Exercise("any_name", Series(2, mutableListOf(0, 0)), Rest(0))
        ))

        assertFalse(startedSession.notStarted)
        assertTrue(notStartedSession.notStarted)
    }
}