package com.br2.cadela.workout

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.streams.toList

class FirstLevelTestSessionTest : WorkoutTestBase() {
    @Test
    fun `The first session is a Test session that contains exercise A, B, C, A1 and rests between are 3min`() {
        val session = sut.createNewSession()

        Assertions.assertEquals(Session.FIRST_LEVEL_TEST, session)
    }

    @Test
    fun `B session result is under 4, next session wil be First Program`() {
        val previousSession = Session(
            name = "first_level_test",
            exercises = listOf(
                Exercise("B", Series(1, mutableListOf(3)), restAfter = Rest(120)),
                Exercise("C", Series(1, mutableListOf(1)), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)
        Assertions.assertEquals(Session.FIRST_PROGRAM, session)
    }

    @Test
    fun `B session result is under 4 and C is 0, next session wil be First Program with C4 instead of C1`() {
        val previousSession = Session(
            name = "first_level_test",
            exercises = listOf(
                Exercise("B", Series(1, mutableListOf(3)), restAfter = Rest(120)),
                Exercise("C", Series(1, mutableListOf(0)), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)
        Assertions.assertEquals(Session.FIRST_PROGRAM_WITH_C4, session)
    }

    @Test
    fun `B session result is 4 or more, next session wil be Second Program`() {
        val previousSession = Session(
            name = "first_level_test",
            exercises = listOf(Exercise("B", Series(1, mutableListOf(4)), restAfter = null))
        )
        val session = sut.createNewSession(previousSession)
        Assertions.assertEquals(Session.SECOND_PROGRAM, session)
    }
}