package com.br2.cadela.workout

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.streams.toList

class FirstLevelSessionFirstProgramTest : WorkoutTestBase() {
    @Test
    fun `C4 session result is under 12, next session wil be the same`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(Exercise("C4", Series(1, listOf(11))))
        )
        val session = sut.createNewSession(sessionResult)

        Assertions.assertEquals("1st Program", session.name)
        Assertions.assertEquals(
            listOf("A", "D", "C4", "E", "F", "G", "K2"),
            session.exercises.stream().map { it.name }.toList()
        )
        Assertions.assertEquals(
            List(6) { 120 },
            session.restsBetweenExercises.stream().map { it.duration }.toList()
        )
    }

    @Test
    fun `C4 session result is 12 or over, next session replace C4 by C5`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(Exercise("C4", Series(1, listOf(12))))
        )
        val session = sut.createNewSession(sessionResult)

        Assertions.assertEquals("1st Program", session.name)
        Assertions.assertEquals(
            listOf("A", "D", "C5", "E", "F", "G", "K2"),
            session.exercises.stream().map { it.name }.toList()
        )
        Assertions.assertEquals(
            List(6) { 120 },
            session.restsBetweenExercises.stream().map { it.duration }.toList()
        )
    }

    @Test
    fun `C5 session result is under 12 next session wil be the same`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(Exercise("C5", Series(1, listOf(11))))
        )
        val session = sut.createNewSession(sessionResult)

        Assertions.assertEquals("1st Program", session.name)
        Assertions.assertEquals(
            listOf("A", "D", "C5", "E", "F", "G", "K2"),
            session.exercises.stream().map { it.name }.toList()
        )
        Assertions.assertEquals(
            List(6) { 120 },
            session.restsBetweenExercises.stream().map { it.duration }.toList()
        )
    }

    @Test
    fun `C5 session result is 12 or over, next session replace C5 by C6`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(Exercise("C5", Series(1, listOf(12))))
        )
        val session = sut.createNewSession(sessionResult)

        Assertions.assertEquals("1st Program", session.name)
        Assertions.assertEquals(
            listOf("A", "D", "C6", "E", "F", "G", "K2"),
            session.exercises.stream().map { it.name }.toList()
        )
        Assertions.assertEquals(
            List(6) { 120 },
            session.restsBetweenExercises.stream().map { it.duration }.toList()
        )
    }

    @Test
    fun `C6 session result is under 12 next session wil be the same`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(Exercise("C6", Series(1, listOf(11))))
        )
        val session = sut.createNewSession(sessionResult)

        Assertions.assertEquals("1st Program", session.name)
        Assertions.assertEquals(
            listOf("A", "D", "C6", "E", "F", "G", "K2"),
            session.exercises.stream().map { it.name }.toList()
        )
        Assertions.assertEquals(
            List(6) { 120 },
            session.restsBetweenExercises.stream().map { it.duration }.toList()
        )
    }

    @Test
    fun `C6 session result is 12 or over, next session replace C6 by C1`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(Exercise("C6", Series(1, listOf(12))))
        )
        val session = sut.createNewSession(sessionResult)

        Assertions.assertEquals("1st Program", session.name)
        Assertions.assertEquals(
            listOf("A", "D", "C1", "E", "F", "G", "K2"),
            session.exercises.stream().map { it.name }.toList()
        )
        Assertions.assertEquals(
            List(6) { 120 },
            session.restsBetweenExercises.stream().map { it.duration }.toList()
        )
    }
}