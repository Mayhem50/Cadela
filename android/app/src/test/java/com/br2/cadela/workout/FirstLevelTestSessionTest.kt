package com.br2.cadela.workout

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.streams.toList

class FirstLevelTestSessionTest : WorkoutTestBase() {
    @Test
    fun `The first session is a Test session that contains exercise A, B, C, A1 and rests between are 3min`() {
        val session = sut.createNewSession()

        Assertions.assertEquals("1st Level Test", session.name)
        Assertions.assertEquals(
            listOf("A", "B", "C", "A1"),
            session.exercises.stream().map { it.name }.toList()
        )
        Assertions.assertEquals(
            listOf(180, 180, 180),
            session.restsBetweenExercises.stream().map { it.duration }.toList()
        )
    }

    @Test
    fun `B session result is under 4, next session wil be First Program`() {
        val sessionResult = SessionResult(
            name = "1st Level Test",
            exercises = listOf(
                Exercise("B", Series(1, listOf(3))),
                Exercise("C", Series(1, listOf(1)))
            )
        )
        val session = sut.createNewSession(sessionResult)

        Assertions.assertEquals("1st Program", session.name)
        Assertions.assertEquals(
            listOf("A1", "D", "C1", "E", "F", "G", "K2"),
            session.exercises.stream().map { it.name }.toList()
        )
        Assertions.assertEquals(
            List(7) { 2 },
            session.exercises.stream().map { it.series.count }.toList()
        )
        Assertions.assertEquals(
            List(6) { 120 },
            session.restsBetweenExercises.stream().map { it.duration }.toList()
        )
    }

    @Test
    fun `B session result is under 4 and C is 0, next session wil be First Program with C4 instead of C1`() {
        val sessionResult = SessionResult(
            name = "1st Level Test",
            exercises = listOf(
                Exercise("B", Series(1, listOf(3))),
                Exercise("C", Series(1, listOf(0)))
            )
        )
        val session = sut.createNewSession(sessionResult)

        Assertions.assertEquals("1st Program", session.name)
        Assertions.assertEquals(
            listOf("A1", "D", "C4", "E", "F", "G", "K2"),
            session.exercises.stream().map { it.name }.toList()
        )
        Assertions.assertEquals(
            List(6) { 120 },
            session.restsBetweenExercises.stream().map { it.duration }.toList()
        )
    }

    @Test
    fun `B session result is 4 or more, next session wil be Second Program`() {
        val sessionResult = SessionResult(
            name = "1st Level Test",
            exercises = listOf(Exercise("B", Series(1, listOf(4))))
        )
        val session = sut.createNewSession(sessionResult)

        Assertions.assertEquals("2nd Program", session.name)
        Assertions.assertEquals(
            listOf("B", "A1", "D", "C1", "E", "F", "G", "K2"),
            session.exercises.stream().map { it.name }.toList()
        )
        Assertions.assertEquals(
            List(7) { 120 },
            session.restsBetweenExercises.stream().map { it.duration }.toList()
        )
    }
}