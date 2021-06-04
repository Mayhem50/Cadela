package com.br2.cadela.workout

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.streams.toList

class FirstLevelSessionFirstProgramTest : WorkoutTestBase() {

    private fun assertSession(session: Session, seriesCount: Int, vararg exerciseNames: String) {
        Assertions.assertEquals("1st Program", session.name)
        Assertions.assertEquals(
            exerciseNames.toList(),
            session.exercises.stream().map { it.name }.toList()
        )
        Assertions.assertEquals(
            List(session.exercises.size) { Series(seriesCount) },
            session.exercises.stream().map { it.series }.toList()
        )
        Assertions.assertEquals(
            List(session.exercises.size - 1) { 120 },
            session.restsBetweenExercises.stream().map { it.duration }.toList()
        )
    }

    @Test
    fun `C4 session result is less than 12, next session will be the same`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A1", series = Series(2)),
                Exercise(name = "D", series = Series(2)),
                Exercise(name = "C4", series = Series(2, listOf(11))),
                Exercise(name = "E", series = Series(2)),
                Exercise(name = "F", series = Series(2)),
                Exercise(name = "G", series = Series(2)),
                Exercise(name = "K2", series = Series(2)))
        )
        val session = sut.createNewSession(sessionResult)

        assertSession(session, 2, "A1", "D", "C4", "E", "F", "G", "K2")
    }

    @Test
    fun `C4 session result is 12 or more, next session replace C4 by C5`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A1", series = Series(2)),
                Exercise(name = "D", series = Series(2)),
                Exercise(name = "C4", series = Series(2, listOf(12))),
                Exercise(name = "E", series = Series(2)),
                Exercise(name = "F", series = Series(2)),
                Exercise(name = "G", series = Series(2)),
                Exercise(name = "K2", series = Series(2))
            )
        )
        val session = sut.createNewSession(sessionResult)
        assertSession(session, 2, "A1", "D", "C5", "E", "F", "G", "K2")
    }

    @Test
    fun `C5 session result is less than 12 next session will be the same`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A1", series = Series(2)),
                Exercise(name = "D", series = Series(2)),
                Exercise(name = "C5", series = Series(2, listOf(11))),
                Exercise(name = "E", series = Series(2)),
                Exercise(name = "F", series = Series(2)),
                Exercise(name = "G", series = Series(2)),
                Exercise(name = "K2", series = Series(2)))
        )
        val session = sut.createNewSession(sessionResult)

        assertSession(session, 2, "A1", "D", "C5", "E", "F", "G", "K2")
    }

    @Test
    fun `C5 session result is 12 or more, next session replace C5 by C6`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A1", series = Series(2)),
                Exercise(name = "D", series = Series(2)),
                Exercise(name = "C5", series = Series(2, listOf(12))),
                Exercise(name = "E", series = Series(2)),
                Exercise(name = "F", series = Series(2)),
                Exercise(name = "G", series = Series(2)),
                Exercise(name = "K2", series = Series(2)))
        )
        val session = sut.createNewSession(sessionResult)
        assertSession(session, 2, "A1", "D", "C6", "E", "F", "G", "K2")
    }

    @Test
    fun `C6 session result is less than 12 next session will be the same`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A1", series = Series(2)),
                Exercise(name = "D", series = Series(2)),
                Exercise(name = "C6", series = Series(2, listOf(11))),
                Exercise(name = "E", series = Series(2)),
                Exercise(name = "F", series = Series(2)),
                Exercise(name = "G", series = Series(2)),
                Exercise(name = "K2", series = Series(2)))
        )
        val session = sut.createNewSession(sessionResult)
        assertSession(session, 2, "A1", "D", "C6", "E", "F", "G", "K2")
    }

    @Test
    fun `C6 session result is 12 or more, next session replace C6 by C1`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A1", series = Series(2)),
                Exercise(name = "D", series = Series(2)),
                Exercise(name = "C6", series = Series(2, listOf(12))),
                Exercise(name = "E", series = Series(2)),
                Exercise(name = "F", series = Series(2)),
                Exercise(name = "G", series = Series(2)),
                Exercise(name = "K2", series = Series(2)))
        )
        val session = sut.createNewSession(sessionResult)
        assertSession(session, 2, "A1", "D", "C1", "E", "F", "G", "K2")
    }

    @Test
    fun `A1 session result is less than 8, next session will be the same`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A1", series = Series(2, listOf(7))),
                Exercise(name = "D", series = Series(2)),
                Exercise(name = "C1", series = Series(2)),
                Exercise(name = "E", series = Series(2)),
                Exercise(name = "F", series = Series(2)),
                Exercise(name = "G", series = Series(2)),
                Exercise(name = "K2", series = Series(2)))
        )
        val session = sut.createNewSession(sessionResult)
        assertSession(session, 2, "A1", "D", "C1", "E", "F", "G", "K2")
    }

    @Test
    fun `A1 session result is 8 or more, next session replace A1 by A2`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A1", series = Series(2, listOf(8))),
                Exercise(name = "D", series = Series(2)),
                Exercise(name = "C1", series = Series(2)),
                Exercise(name = "E", series = Series(2)),
                Exercise(name = "F", series = Series(2)),
                Exercise(name = "G", series = Series(2)),
                Exercise(name = "K2", series = Series(2)))
        )
        val session = sut.createNewSession(sessionResult)
        assertSession(session, 2, "A2", "D", "C1", "E", "F", "G", "K2")
    }

    @Test
    fun `A2 session result is less than 8, next session is the same`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A2", series = Series(2, listOf(7))),
                Exercise(name = "D", series = Series(2)),
                Exercise(name = "C1", series = Series(2)),
                Exercise(name = "E", series = Series(2)),
                Exercise(name = "F", series = Series(2)),
                Exercise(name = "G", series = Series(2)),
                Exercise(name = "K2", series = Series(2)))
        )
        val session = sut.createNewSession(sessionResult)
        assertSession(session, 2, "A2", "D", "C1", "E", "F", "G", "K2")
    }

    @Test
    fun `A2 session result is 8 or more, next session add A3 before A2`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A2", series = Series(2, listOf(8))),
                Exercise(name = "D", series = Series(2)),
                Exercise(name = "C1", series = Series(2)),
                Exercise(name = "E", series = Series(2)),
                Exercise(name = "F", series = Series(2)),
                Exercise(name = "G", series = Series(2)),
                Exercise(name = "K2", series = Series(2)))
        )
        val session = sut.createNewSession(sessionResult)
        assertSession(session, 2, "A3", "A2", "D", "C1", "E", "F", "G", "K2")
    }

    @Test
    fun `A3 session result is less than 8, next session will be the same`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A3", series = Series(2, listOf(7))),
                Exercise(name = "A2", series = Series(2)),
                Exercise(name = "D", series = Series(2)),
                Exercise(name = "C1", series = Series(2)),
                Exercise(name = "E", series = Series(2)),
                Exercise(name = "F", series = Series(2)),
                Exercise(name = "G", series = Series(2)),
                Exercise(name = "K2", series = Series(2)))
        )
        val session = sut.createNewSession(sessionResult)
        assertSession(session, 2, "A3", "A2", "D", "C1", "E", "F", "G", "K2")
    }

    @Test
    fun `A3 session result is 8 or more, next session replace A3 by A4`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A3", series = Series(2, listOf(8))),
                Exercise(name = "A2", series = Series(2)),
                Exercise(name = "D", series = Series(2)),
                Exercise(name = "C1", series = Series(2)),
                Exercise(name = "E", series = Series(2)),
                Exercise(name = "F", series = Series(2)),
                Exercise(name = "G", series = Series(2)),
                Exercise(name = "K2", series = Series(2)))
        )
        val session = sut.createNewSession(sessionResult)
        assertSession(session, 2, "A4", "A2", "D", "C1", "E", "F", "G", "K2")
    }

    @Test
    fun `Session result is A2-8 A3-10, next session will replace A3 by A4`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A3", series = Series(2, listOf(10))),
                Exercise(name = "A2", series = Series(2, listOf(8))),
                Exercise(name = "D", series = Series(2)),
                Exercise(name = "C1", series = Series(2)),
                Exercise(name = "E", series = Series(2)),
                Exercise(name = "F", series = Series(2)),
                Exercise(name = "G", series = Series(2)),
                Exercise(name = "K2", series = Series(2)))
        )
        val session = sut.createNewSession(sessionResult)
        assertSession(session, 2, "A4", "A2", "D", "C1", "E", "F", "G", "K2")
    }

    @Test
    fun `A4 session result is less than 8, next session will be the same`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A4", series = Series(2, listOf(7))),
                Exercise(name = "A2", series = Series(2)),
                Exercise(name = "D", series = Series(2)),
                Exercise(name = "C1", series = Series(2)),
                Exercise(name = "E", series = Series(2)),
                Exercise(name = "F", series = Series(2)),
                Exercise(name = "G", series = Series(2)),
                Exercise(name = "K2", series = Series(2)))
        )
        val session = sut.createNewSession(sessionResult)
        assertSession(session, 2, "A4", "A2", "D", "C1", "E", "F", "G", "K2")
    }

    @Test
    fun `A4 session result is 8 or more, next session replace A4 by A5`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A4", series = Series(2, listOf(8))),
                Exercise(name = "A2", series = Series(2)),
                Exercise(name = "D", series = Series(2)),
                Exercise(name = "C1", series = Series(2)),
                Exercise(name = "E", series = Series(2)),
                Exercise(name = "F", series = Series(2)),
                Exercise(name = "G", series = Series(2)),
                Exercise(name = "K2", series = Series(2)))
        )
        val session = sut.createNewSession(sessionResult)
        assertSession(session, 2, "A5", "A2", "D", "C1", "E", "F", "G", "K2")
    }

    @Test
    fun `Session result is A2-8 A4-10, next session will replace A4 by A5`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A4", series = Series(2, listOf(10))),
                Exercise(name = "A2", series = Series(2, listOf(8))),
                Exercise(name = "D", series = Series(2)),
                Exercise(name = "C1", series = Series(2)),
                Exercise(name = "E", series = Series(2)),
                Exercise(name = "F", series = Series(2)),
                Exercise(name = "G", series = Series(2)),
                Exercise(name = "K2", series = Series(2)))
        )
        val session = sut.createNewSession(sessionResult)
        assertSession(session, 2, "A5", "A2", "D", "C1", "E", "F", "G", "K2")
    }

    @Test
    fun `A5 session result is less than 8, next session will be the same`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A5", series = Series(2, listOf(7))),
                Exercise(name = "A2", series = Series(2)),
                Exercise(name = "D", series = Series(2)),
                Exercise(name = "C1", series = Series(2)),
                Exercise(name = "E", series = Series(2)),
                Exercise(name = "F", series = Series(2)),
                Exercise(name = "G", series = Series(2)),
                Exercise(name = "K2", series = Series(2)))
        )
        val session = sut.createNewSession(sessionResult)
        assertSession(session, 2, "A5", "A2", "D", "C1", "E", "F", "G", "K2")
    }

    @Test
    fun `A5 session result is 8 or more, next session replace A5 by A6`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A5", series = Series(2, listOf(8))),
                Exercise(name = "A2", series = Series(2)),
                Exercise(name = "D", series = Series(2)),
                Exercise(name = "C1", series = Series(2)),
                Exercise(name = "E", series = Series(2)),
                Exercise(name = "F", series = Series(2)),
                Exercise(name = "G", series = Series(2)),
                Exercise(name = "K2", series = Series(2)))
        )
        val session = sut.createNewSession(sessionResult)
        assertSession(session, 2, "A6", "A2", "D", "C1", "E", "F", "G", "K2")
    }

    @Test
    fun `Session result is A2-8 A5-10, next session will replace A5 by A6`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A5", series = Series(2, listOf(10))),
                Exercise(name = "A2", series = Series(2, listOf(8))),
                Exercise(name = "D", series = Series(2)),
                Exercise(name = "C1", series = Series(2)),
                Exercise(name = "E", series = Series(2)),
                Exercise(name = "F", series = Series(2)),
                Exercise(name = "G", series = Series(2)),
                Exercise(name = "K2", series = Series(2)))
        )
        val session = sut.createNewSession(sessionResult)
        assertSession(session, 2, "A6", "A2", "D", "C1", "E", "F", "G", "K2")
    }

    @Test
    fun `A6 session result is less than 8, next session will be the same`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A6", series = Series(2, listOf(7))),
                Exercise(name = "A2", series = Series(2)),
                Exercise(name = "D", series = Series(2)),
                Exercise(name = "C1", series = Series(2)),
                Exercise(name = "E", series = Series(2)),
                Exercise(name = "F", series = Series(2)),
                Exercise(name = "G", series = Series(2)),
                Exercise(name = "K2", series = Series(2)))
        )
        val session = sut.createNewSession(sessionResult)
        assertSession(session, 2, "A6", "A2", "D", "C1", "E", "F", "G", "K2")
    }

    @Test
    fun `A6 session result is 8 or more, next session is a test on only B`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A6", series = Series(2, listOf(8))),
                Exercise(name = "A2", series = Series(2)),
                Exercise(name = "D", series = Series(2)),
                Exercise(name = "C1", series = Series(2)),
                Exercise(name = "E", series = Series(2)),
                Exercise(name = "F", series = Series(2)),
                Exercise(name = "G", series = Series(2)),
                Exercise(name = "K2", series = Series(2)))
        )
        val session = sut.createNewSession(sessionResult)
        Assertions.assertEquals("Only B Test", session.name)
        Assertions.assertEquals(
            listOf("B"),
            session.exercises.stream().map { it.name }.toList()
        )
        Assertions.assertEquals(
            List(session.exercises.size) { Series(1) },
            session.exercises.stream().map { it.series }.toList()
        )
        Assertions.assertEquals(
            List(session.exercises.size - 1) { 120 },
            session.restsBetweenExercises.stream().map { it.duration }.toList()
        )
    }

    @Test
    fun `B session result is less than 5, next session will add B1 to an A6 session`(){
        val sessionResult = SessionResult(
            name = Session.ONLY_B_TEST.name,
            exercises = listOf(
                Exercise(name = "B", series = Series(1, listOf(4))))
        )
        val session = sut.createNewSession(sessionResult)
        assertSession(session, 3, "B1", "A6", "A2", "D", "C1", "E", "F", "G", "K2")
    }

    @Test
    fun `Go to 2nd Program if user do 1st Program with B1 since 2 weeks`(){
        val today = LocalDate.now()
        val twoWeekBefore = today.minus(2, ChronoUnit.WEEKS)
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "B1", series = Series(2)),
                Exercise(name = "A6", series = Series(2)),
                Exercise(name = "A2", series = Series(2)),
                Exercise(name = "D", series = Series(2)),
                Exercise(name = "C1", series = Series(2)),
                Exercise(name = "E", series = Series(2)),
                Exercise(name = "F", series = Series(2)),
                Exercise(name = "G", series = Series(2)),
                Exercise(name = "K2", series = Series(2))),
            levelStartedAt = twoWeekBefore
        )
        val session = sut.createNewSession(sessionResult)
        Assertions.assertEquals(Session.SECOND_PROGRAM, session)
    }

    @Test
    fun `Session result is A2-8 C4-12, next session will add A3 and replace C4 by C5`() {
        val sessionResult = SessionResult(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A2", series = Series(2, listOf(8))),
                Exercise(name = "D", series = Series(2)),
                Exercise(name = "C4", series = Series(2, listOf(12))),
                Exercise(name = "E", series = Series(2)),
                Exercise(name = "F", series = Series(2)),
                Exercise(name = "G", series = Series(2)),
                Exercise(name = "K2", series = Series(2))
            )
        )
        val session = sut.createNewSession(sessionResult)
        assertSession(session, 2, "A3", "A2", "D", "C5", "E", "F", "G", "K2")
    }
}