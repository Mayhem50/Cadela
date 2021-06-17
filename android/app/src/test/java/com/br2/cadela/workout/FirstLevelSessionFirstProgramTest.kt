package com.br2.cadela.workout

import com.br2.cadela.workout.datas.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class FirstLevelSessionFirstProgramTest : WorkoutTestBase() {

    private fun assert1stProgramSessionUpdated(
        session: Session,
        exerciseNames: Array<String>
    ) {
        assertEquals("first_program", session.name)
        assertEquals(
            exerciseNames.toList(),
            session.exerciseNames
        )
    }

    @Test
    fun `C4 session result is less than 12, next session will be the same`() {
        val previousSession = Session(
            name = "first_program",
            exercises = listOf(
                Exercise(name = "A1", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "D", series = Series(2), restAfter = Rest(120)),
                Exercise(
                    name = "C4",
                    series = Series(2, MutableList(2) { Repetition(11) }),
                    restAfter = Rest(120)
                ),
                Exercise(name = "E", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "F", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "G", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "K2", series = Series(2), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)

        assert1stProgramSessionUpdated(
            session = session,
            exerciseNames = arrayOf("A1", "D", "C4", "E", "F", "G", "K2")
        )
    }

    @Test
    fun `C4 session result is 12 or more, next session replace C4 by C5`() {
        val previousSession = Session(
            name = "first_program",
            exercises = listOf(
                Exercise(name = "A1", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "D", series = Series(2), restAfter = Rest(120)),
                Exercise(
                    name = "C4",
                    series = Series(2, MutableList(2) { Repetition(12) }),
                    restAfter = Rest(120)
                ),
                Exercise(name = "E", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "F", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "G", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "K2", series = Series(2), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)
        assert1stProgramSessionUpdated(
            session = session,
            exerciseNames = arrayOf("A1", "D", "C5", "E", "F", "G", "K2")
        )
    }

    @Test
    fun `C5 session result is less than 12 next session will be the same`() {
        val previousSession = Session(
            name = "first_program",
            exercises = listOf(
                Exercise(name = "A1", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "D", series = Series(2), restAfter = Rest(120)),
                Exercise(
                    name = "C5",
                    series = Series(2, MutableList(2) { Repetition(11) }),
                    restAfter = Rest(120)
                ),
                Exercise(name = "E", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "F", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "G", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "K2", series = Series(2), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)

        assert1stProgramSessionUpdated(
            session = session,
            exerciseNames = arrayOf("A1", "D", "C5", "E", "F", "G", "K2")
        )
    }

    @Test
    fun `C5 session result is 12 or more, next session replace C5 by C6`() {
        val previousSession = Session(
            name = "first_program",
            exercises = listOf(
                Exercise(name = "A1", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "D", series = Series(2), restAfter = Rest(120)),
                Exercise(
                    name = "C5",
                    series = Series(2, MutableList(2) { Repetition(12) }),
                    restAfter = Rest(120)
                ),
                Exercise(name = "E", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "F", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "G", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "K2", series = Series(2), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)
        assert1stProgramSessionUpdated(
            session = session,
            exerciseNames = arrayOf("A1", "D", "C6", "E", "F", "G", "K2")
        )
    }

    @Test
    fun `C6 session result is less than 12 next session will be the same`() {
        val previousSession = Session(
            name = "first_program",
            exercises = listOf(
                Exercise(name = "A1", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "D", series = Series(2), restAfter = Rest(120)),
                Exercise(
                    name = "C6",
                    series = Series(2, MutableList(2) { Repetition(11) }),
                    restAfter = Rest(120)
                ),
                Exercise(name = "E", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "F", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "G", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "K2", series = Series(2), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)
        assert1stProgramSessionUpdated(
            session = session,
            exerciseNames = arrayOf("A1", "D", "C6", "E", "F", "G", "K2")
        )
    }

    @Test
    fun `C6 session result is 12 or more, next session replace C6 by C1`() {
        val previousSession = Session(
            name = "first_program",
            exercises = listOf(
                Exercise(name = "A1", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "D", series = Series(2), restAfter = Rest(120)),
                Exercise(
                    name = "C6",
                    series = Series(2, MutableList(2) { Repetition(12) }),
                    restAfter = Rest(120)
                ),
                Exercise(name = "E", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "F", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "G", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "K2", series = Series(2), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)
        assert1stProgramSessionUpdated(
            session = session,
            exerciseNames = arrayOf("A1", "D", "C1", "E", "F", "G", "K2")
        )
    }

    @Test
    fun `A1 session result is less than 8, next session will be the same`() {
        val previousSession = Session(
            name = "first_program",
            exercises = listOf(
                Exercise(
                    name = "A1",
                    series = Series(2, MutableList(2) { Repetition(7) }),
                    restAfter = Rest(120)
                ),
                Exercise(name = "D", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "C1", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "E", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "F", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "G", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "K2", series = Series(2), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)

        assert1stProgramSessionUpdated(
            session = session,
            exerciseNames = arrayOf("A1", "D", "C1", "E", "F", "G", "K2")
        )
    }

    @Test
    fun `A1 session result is 8 or more, next session replace A1 by A2`() {
        val previousSession = Session(
            name = "first_program",
            exercises = listOf(
                Exercise(
                    name = "A1",
                    series = Series(2, MutableList(2) { Repetition(8) }),
                    restAfter = Rest(120)
                ),
                Exercise(name = "D", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "C1", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "E", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "F", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "G", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "K2", series = Series(2), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)

        assert1stProgramSessionUpdated(
            session = session,
            exerciseNames = arrayOf("A2", "D", "C1", "E", "F", "G", "K2")
        )
    }

    @Test
    fun `A2 session result is less than 8, next session is the same`() {
        val previousSession = Session(
            name = "first_program",
            exercises = listOf(
                Exercise(
                    name = "A2",
                    series = Series(2, MutableList(2) { Repetition(7) }),
                    restAfter = Rest(120)
                ),
                Exercise(name = "D", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "C1", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "E", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "F", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "G", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "K2", series = Series(2), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)
        assert1stProgramSessionUpdated(
            session = session,
            exerciseNames = arrayOf("A2", "D", "C1", "E", "F", "G", "K2")
        )
    }

    @Test
    fun `A2 session result is 8 or more, next session add A3 before A2`() {
        val previousSession = Session(
            name = "first_program",
            exercises = listOf(
                Exercise(
                    name = "A2",
                    series = Series(2, MutableList(2) { Repetition(8) }),
                    restAfter = Rest(120)
                ),
                Exercise(name = "D", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "C1", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "E", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "F", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "G", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "K2", series = Series(2), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)
        assert1stProgramSessionUpdated(
            session = session,
            exerciseNames = arrayOf("A3", "A2", "D", "C1", "E", "F", "G", "K2")
        )
    }

    @Test
    fun `A3 session result is less than 8, next session will be the same`() {
        val previousSession = Session(
            name = "first_program",
            exercises = listOf(
                Exercise(
                    name = "A3",
                    series = Series(2, MutableList(2) { Repetition(7) }),
                    restAfter = Rest(120)
                ),
                Exercise(name = "A2", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "D", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "C1", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "E", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "F", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "G", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "K2", series = Series(2), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)
        assert1stProgramSessionUpdated(
            session = session,
            exerciseNames = arrayOf("A3", "A2", "D", "C1", "E", "F", "G", "K2")
        )
    }

    @Test
    fun `A3 session result is 8 or more, next session replace A3 by A4`() {
        val previousSession = Session(
            name = "first_program",
            exercises = listOf(
                Exercise(
                    name = "A3",
                    series = Series(2, MutableList(2) { Repetition(8) }),
                    restAfter = Rest(120)
                ),
                Exercise(name = "A2", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "D", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "C1", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "E", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "F", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "G", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "K2", series = Series(2), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)
        assert1stProgramSessionUpdated(
            session = session,
            exerciseNames = arrayOf("A4", "A2", "D", "C1", "E", "F", "G", "K2")
        )
    }

    @Test
    fun `Session result is A2-8 A3-10, next session will replace A3 by A4`() {
        val previousSession = Session(
            name = "first_program",
            exercises = listOf(
                Exercise(
                    name = "A3",
                    series = Series(2, MutableList(2) { Repetition(10) }),
                    restAfter = Rest(120)
                ),
                Exercise(
                    name = "A2",
                    series = Series(2, MutableList(2) { Repetition(8) }),
                    restAfter = Rest(120)
                ),
                Exercise(name = "D", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "C1", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "E", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "F", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "G", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "K2", series = Series(2), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)
        assert1stProgramSessionUpdated(
            session = session,
            exerciseNames = arrayOf("A4", "A2", "D", "C1", "E", "F", "G", "K2")
        )
    }

    @Test
    fun `A4 session result is less than 8, next session will be the same`() {
        val previousSession = Session(
            name = "first_program",
            exercises = listOf(
                Exercise(
                    name = "A4",
                    series = Series(2, MutableList(2) { Repetition(7) }),
                    restAfter = Rest(120)
                ),
                Exercise(name = "A2", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "D", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "C1", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "E", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "F", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "G", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "K2", series = Series(2), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)
        assert1stProgramSessionUpdated(
            session = session,
            exerciseNames = arrayOf("A4", "A2", "D", "C1", "E", "F", "G", "K2")
        )
    }

    @Test
    fun `A4 session result is 8 or more, next session replace A4 by A5`() {
        val previousSession = Session(
            name = "first_program",
            exercises = listOf(
                Exercise(
                    name = "A4",
                    series = Series(2, MutableList(2) { Repetition(8) }),
                    restAfter = Rest(120)
                ),
                Exercise(name = "A2", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "D", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "C1", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "E", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "F", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "G", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "K2", series = Series(2), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)
        assert1stProgramSessionUpdated(
            session = session,
            exerciseNames = arrayOf("A5", "A2", "D", "C1", "E", "F", "G", "K2")
        )
    }

    @Test
    fun `Session result is A2-8 A4-10, next session will replace A4 by A5`() {
        val previousSession = Session(
            name = "first_program",
            exercises = listOf(
                Exercise(
                    name = "A4",
                    series = Series(2, MutableList(2) { Repetition(10) }),
                    restAfter = Rest(120)
                ),
                Exercise(
                    name = "A2",
                    series = Series(2, MutableList(2) { Repetition(8) }),
                    restAfter = Rest(120)
                ),
                Exercise(name = "D", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "C1", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "E", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "F", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "G", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "K2", series = Series(2), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)
        assert1stProgramSessionUpdated(
            session = session,
            exerciseNames = arrayOf("A5", "A2", "D", "C1", "E", "F", "G", "K2")
        )
    }

    @Test
    fun `A5 session result is less than 8, next session will be the same`() {
        val previousSession = Session(
            name = "first_program",
            exercises = listOf(
                Exercise(
                    name = "A5",
                    series = Series(2, MutableList(2) { Repetition(7) }),
                    restAfter = Rest(120)
                ),
                Exercise(name = "A2", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "D", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "C1", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "E", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "F", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "G", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "K2", series = Series(2), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)
        assert1stProgramSessionUpdated(
            session = session,
            exerciseNames = arrayOf("A5", "A2", "D", "C1", "E", "F", "G", "K2")
        )
    }

    @Test
    fun `A5 session result is 8 or more, next session replace A5 by A6`() {
        val previousSession = Session(
            name = "first_program",
            exercises = listOf(
                Exercise(
                    name = "A5",
                    series = Series(2, MutableList(2) { Repetition(8) }),
                    restAfter = Rest(120)
                ),
                Exercise(name = "A2", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "D", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "C1", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "E", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "F", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "G", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "K2", series = Series(2), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)
        assert1stProgramSessionUpdated(
            session = session,
            exerciseNames = arrayOf("A6", "A2", "D", "C1", "E", "F", "G", "K2")
        )
    }

    @Test
    fun `Session result is A2-8 A5-10, next session will replace A5 by A6`() {
        val previousSession = Session(
            name = "first_program",
            exercises = listOf(
                Exercise(
                    name = "A5",
                    series = Series(2, MutableList(2) { Repetition(10) }),
                    restAfter = Rest(120)
                ),
                Exercise(
                    name = "A2",
                    series = Series(2,  MutableList(2) { Repetition(8) }),
                    restAfter = Rest(120)
                ),
                Exercise(name = "D", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "C1", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "E", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "F", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "G", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "K2", series = Series(2), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)
        assertEquals(previousSession.replaceExerciseName("A5", "A6").exerciseNames, session.exerciseNames)
    }

    @Test
    fun `A6 session result is less than 8, next session will be the same`() {
        val previousSession = Session(
            name = "first_program",
            exercises = listOf(
                Exercise(
                    name = "A6",
                    series = Series(2, MutableList(2) { Repetition(7) }),
                    restAfter = Rest(120)
                ),
                Exercise(name = "A2", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "D", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "C1", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "E", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "F", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "G", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "K2", series = Series(2), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)
        assert1stProgramSessionUpdated(
            session = session,
            exerciseNames = arrayOf("A6", "A2", "D", "C1", "E", "F", "G", "K2")
        )
    }

    @Test
    fun `A6 session result is 8 or more, next session is a test on only B`() {
        val previousSession = Session(
            name = "first_program",
            exercises = listOf(
                Exercise(
                    name = "A6",
                    series = Series(2, MutableList(2) { Repetition(8) }),
                    restAfter = Rest(120)
                ),
                Exercise(name = "A2", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "D", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "C1", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "E", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "F", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "G", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "K2", series = Series(2), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)
        assertEquals(Session.ONLY_B_TEST, session)
    }

    @Test
    fun `B session result is less than 5, next session will add B1 to an A6 session`() {
        val previousSession = Session(
            name = Session.ONLY_B_TEST.name,
            exercises = listOf(
                Exercise(
                    name = "B",
                    series = Series(1, MutableList(1) { Repetition(4) }),
                    restAfter = null
                )
            )
        )
        val session = sut.createNewSession(previousSession)
        assertEquals(Session.FIRST_PROGRAM_WITH_B1, session)
    }

    @Test
    fun `B session result is between 5 and 8, next session will be 2nd Program`() {
        val previousSession = Session(
            name = Session.ONLY_B_TEST.name,
            exercises = listOf(
                Exercise(
                    name = "B",
                    series = Series(1, MutableList(1) { Repetition(7) }),
                    restAfter = null
                )
            )
        )
        val session = sut.createNewSession(previousSession)
        assertEquals(Session.SECOND_PROGRAM, session)
    }

    @Test
    fun `B session result is 8 or over, next session will be 2nd Level`() {
        val previousSession = Session(
            name = Session.ONLY_B_TEST.name,
            exercises = listOf(
                Exercise(
                    name = "B",
                    series = Series(1, MutableList(1) { Repetition(8) }),
                    restAfter = null
                )
            )
        )
        val session = sut.createNewSession(previousSession)
        assertEquals(Session.SECOND_LEVEL, session)
    }

    @Test
    fun `Go to 2nd Program if user do first_program with B1 since 2 weeks`() {
        val today = LocalDate.now()
        val twoWeekBefore = today.minus(2, ChronoUnit.WEEKS)
        val previousSession = Session(
            name = Session.FIRST_PROGRAM_WITH_B1.name,
            exercises = Session.FIRST_PROGRAM_WITH_B1.exercises,
            levelStartedAt = twoWeekBefore
        )
        val session = sut.createNewSession(previousSession)
        assertEquals(Session.SECOND_PROGRAM.name, session.name)
        assertEquals(Session.SECOND_PROGRAM.exerciseNames, session.exerciseNames)
    }

    @Test
    fun `Session result is A2-8 C4-12, next session will add A3 and replace C4 by C5`() {
        val previousSession = Session(
            name = "first_program",
            exercises = listOf(
                Exercise(
                    name = "A2",
                    series = Series(2, MutableList(2) { Repetition(8) }),
                    restAfter = Rest(120)
                ),
                Exercise(name = "D", series = Series(2), restAfter = Rest(120)),
                Exercise(
                    name = "C4",
                    series = Series(2, MutableList(2) { Repetition(12) }),
                    restAfter = Rest(120)
                ),
                Exercise(name = "E", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "F", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "G", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "K2", series = Series(2), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)
        assert1stProgramSessionUpdated(
            session = session,
            exerciseNames = arrayOf("A3", "A2", "D", "C5", "E", "F", "G", "K2")
        )
    }
}