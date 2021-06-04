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
        val sessionResult = SessionResult(
            name = "1st Level Test",
            exercises = listOf(Exercise("B", Series(1, listOf(3))), Exercise("C", Series(1, listOf(1))))
        )
        val session = sut.createNewSession(sessionResult)

        assertEquals("1st Program", session.name)
        assertEquals(
            listOf("A", "D", "C1", "E", "F", "G", "K2"),
            session.exercises.stream().map { it.name }.toList()
        )
        assertEquals(
            List(7) { 2 },
            session.exercises.stream().map { it.series.count }.toList()
        )
        assertEquals(
            List(6) { 120 },
            session.restsBetweenExercises.stream().map { it.duration }.toList()
        )
    }

    @Test
    fun `When session result on test session is under 4 for exercise B and C is 0 next session wil be First Program with C4 instead of C1`() {
        val sessionResult = SessionResult(
            name = "1st Level Test",
            exercises = listOf(Exercise("B", Series(1, listOf(3))), Exercise("C", Series(1, listOf(0))))
        )
        val session = sut.createNewSession(sessionResult)

        assertEquals("1st Program", session.name)
        assertEquals(
            listOf("A", "D", "C4", "E", "F", "G", "K2"),
            session.exercises.stream().map { it.name }.toList()
        )
        assertEquals(
            List(6) { 120 },
            session.restsBetweenExercises.stream().map { it.duration }.toList()
        )
    }

    @Test
    fun `When session result on first program session is under 12 on first series for C4 next session wil be First Program with C4 again`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(Exercise("C4", Series(1, listOf(11))))
        )
        val session = sut.createNewSession(sessionResult)

        assertEquals("1st Program", session.name)
        assertEquals(
            listOf("A", "D", "C4", "E", "F", "G", "K2"),
            session.exercises.stream().map { it.name }.toList()
        )
        assertEquals(
            List(6) { 120 },
            session.restsBetweenExercises.stream().map { it.duration }.toList()
        )
    }

    @Test
    fun `When session result on first program session is 12 on first series for C4 next session wil be First Program with C5 instead of C4`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(Exercise("C4", Series(1, listOf(12))))
        )
        val session = sut.createNewSession(sessionResult)

        assertEquals("1st Program", session.name)
        assertEquals(
            listOf("A", "D", "C5", "E", "F", "G", "K2"),
            session.exercises.stream().map { it.name }.toList()
        )
        assertEquals(
            List(6) { 120 },
            session.restsBetweenExercises.stream().map { it.duration }.toList()
        )
    }

    @Test
    fun `When session result on first program session is under 12 on first series for C5 next session wil be First Program with C5 again`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(Exercise("C5", Series(1, listOf(11))))
        )
        val session = sut.createNewSession(sessionResult)

        assertEquals("1st Program", session.name)
        assertEquals(
            listOf("A", "D", "C5", "E", "F", "G", "K2"),
            session.exercises.stream().map { it.name }.toList()
        )
        assertEquals(
            List(6) { 120 },
            session.restsBetweenExercises.stream().map { it.duration }.toList()
        )
    }

    @Test
    fun `When session result on first program session is 12 on first series for C5 next session wil be First Program with C6 instead of C5`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(Exercise("C5", Series(1, listOf(12))))
        )
        val session = sut.createNewSession(sessionResult)

        assertEquals("1st Program", session.name)
        assertEquals(
            listOf("A", "D", "C6", "E", "F", "G", "K2"),
            session.exercises.stream().map { it.name }.toList()
        )
        assertEquals(
            List(6) { 120 },
            session.restsBetweenExercises.stream().map { it.duration }.toList()
        )
    }

    @Test
    fun `When session result on first program session is under 12 on first series for C6 next session wil be First Program with C6 again`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(Exercise("C6", Series(1, listOf(11))))
        )
        val session = sut.createNewSession(sessionResult)

        assertEquals("1st Program", session.name)
        assertEquals(
            listOf("A", "D", "C6", "E", "F", "G", "K2"),
            session.exercises.stream().map { it.name }.toList()
        )
        assertEquals(
            List(6) { 120 },
            session.restsBetweenExercises.stream().map { it.duration }.toList()
        )
    }

    @Test
    fun `When session result on first program session is 12 on first series for C6 next session wil be First Program with C6 instead of C1`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(Exercise("C6", Series(1, listOf(12))))
        )
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
        val sessionResult = SessionResult(
            name = "1st Level Test",
            exercises = listOf(Exercise("B", Series(1, listOf(4))))
        )
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

