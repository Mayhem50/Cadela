package com.br2.cadela.workout

import com.br2.cadela.workout.datas.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FirstLevelSessionSecondProgramTest : WorkoutTestBase() {
    @Test
    fun `B session result is 8 or more on first serie, next session will be 2nd level`() {
        val previousSession = Session(
            name = "second_program",
            exercises = listOf(
                Exercise(
                    name = "B",
                    series = Series(3, mutableListOf(Repetition(8), Repetition(7), Repetition(6))),
                    restAfter = Rest(duration = 120)
                ),
                Exercise(name = "A1", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "D", series = Series(1), restAfter = Rest(duration = 120)),
                Exercise(
                    name = "C1",
                    series = Series(3),
                    restAfter = Rest(duration = 120),
                    speed = ESpeed.FAST
                ),
                Exercise(name = "E", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "F", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "G", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "K2", series = Series(3), restAfter = null)
            )
        )

        val session = sut.createNewSession(previousSession)
        assertEquals(Session.SECOND_LEVEL, session)
    }

    @Test
    fun `B session result is less than 8 on first serie, next session will be the same`() {
        val previousSession = Session(
            name = "second_program",
            exercises = listOf(
                Exercise(
                    name = "B",
                    series = Series(3, mutableListOf(Repetition(7), Repetition(7), Repetition(6))),
                    restAfter = Rest(duration = 120)
                ),
                Exercise(name = "A1", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "D", series = Series(1), restAfter = Rest(duration = 120)),
                Exercise(
                    name = "C1",
                    series = Series(3),
                    restAfter = Rest(duration = 120),
                    speed = ESpeed.FAST
                ),
                Exercise(name = "E", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "F", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "G", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "K2", series = Series(3), restAfter = null)
            )
        )

        val session = sut.createNewSession(previousSession)
        assertEquals(Session.SECOND_PROGRAM, session)
    }

    @Test
    fun `A1 session result is 8 or more, replace A1 by A2`() {
        val previousSession = Session(
            name = "second_program",
            exercises = listOf(
                Exercise(name = "B", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(
                    name = "A1",
                    series = Series(3, mutableListOf(Repetition(8), Repetition(7), Repetition(6))),
                    restAfter = Rest(duration = 120)
                ),
                Exercise(name = "D", series = Series(1), restAfter = Rest(duration = 120)),
                Exercise(
                    name = "C1",
                    series = Series(3),
                    restAfter = Rest(duration = 120),
                    speed = ESpeed.FAST
                ),
                Exercise(name = "E", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "F", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "G", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "K2", series = Series(3), restAfter = null)
            )
        )

        val session = sut.createNewSession(previousSession)
        assertEquals(Session.SECOND_PROGRAM_WITH_A2, session)
    }

    @Test
    fun `C4 session result is less than 12, next session will be the same`() {
        val previousSession = Session(
            name = "second_program",
            exercises = listOf(
                Exercise(name = "B", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "A1", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "D", series = Series(1), restAfter = Rest(duration = 120)),
                Exercise(
                    name = "C4",
                    series = Series(1, mutableListOf(Repetition(11))),
                    restAfter = Rest(duration = 120),
                    speed = ESpeed.FAST
                ),
                Exercise(name = "E", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "F", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "G", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "K2", series = Series(3), restAfter = null)
            )
        )

        val session = sut.createNewSession(previousSession)

        assertEquals(previousSession.clearExercisesRepetitions(), session)
    }

    @Test
    fun `C4 session result is 12 or more, next session replace C4 by C5`() {
        val previousSession = Session(
            name = "second_program",
            exercises = listOf(
                Exercise(name = "B", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "A1", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "D", series = Series(1), restAfter = Rest(duration = 120)),
                Exercise(
                    name = "C4",
                    series = Series(1, mutableListOf(Repetition(12))),
                    restAfter = Rest(duration = 120),
                    speed = ESpeed.FAST
                ),
                Exercise(name = "E", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "F", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "G", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "K2", series = Series(3), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)

        assertEquals(previousSession.replaceExerciseNameAndClearReps("C4", "C5"), session)
    }

    @Test
    fun `C5 session result is less than 12 next session will be the same`() {
        val previousSession = Session(
            name = "second_program",
            exercises = listOf(
                Exercise(name = "B", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "A1", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "D", series = Series(1), restAfter = Rest(duration = 120)),
                Exercise(
                    name = "C5",
                    series = Series(1, mutableListOf(Repetition(11))),
                    restAfter = Rest(duration = 120),
                    speed = ESpeed.FAST
                ),
                Exercise(name = "E", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "F", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "G", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "K2", series = Series(3), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)

        assertEquals(previousSession.clearExercisesRepetitions(), session)
    }

    @Test
    fun `C5 session result is 12 or more, next session replace C5 by C6`() {
        val previousSession = Session(
            name = "second_program",
            exercises = listOf(
                Exercise(name = "B", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "A1", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "D", series = Series(1), restAfter = Rest(duration = 120)),
                Exercise(
                    name = "C5",
                    series = Series(1, mutableListOf(Repetition(12))),
                    restAfter = Rest(duration = 120),
                    speed = ESpeed.FAST
                ),
                Exercise(name = "E", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "F", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "G", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "K2", series = Series(3), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)

        assertEquals(previousSession.replaceExerciseNameAndClearReps("C5", "C6"), session)
    }

    @Test
    fun `C6 session result is less than 12 next session will be the same`() {
        val previousSession = Session(
            name = "second_program",
            exercises = listOf(
                Exercise(name = "B", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "A1", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "D", series = Series(1), restAfter = Rest(duration = 120)),
                Exercise(
                    name = "C6",
                    series = Series(1, mutableListOf(Repetition(11))),
                    restAfter = Rest(duration = 120),
                    speed = ESpeed.FAST
                ),
                Exercise(name = "E", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "F", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "G", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "K2", series = Series(3), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)

        assertEquals(previousSession.clearExercisesRepetitions(), session)
    }

    @Test
    fun `C6 session result is 12 or more, next session replace C6 by C1`() {
        val previousSession = Session(
            name = "second_program",
            exercises = listOf(
                Exercise(name = "B", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "A1", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "D", series = Series(1), restAfter = Rest(duration = 120)),
                Exercise(
                    name = "C6",
                    series = Series(1, mutableListOf(Repetition(12))),
                    restAfter = Rest(duration = 120),
                    speed = ESpeed.FAST
                ),
                Exercise(name = "E", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "F", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "G", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "K2", series = Series(3), restAfter = null)
            )
        )
        val expected = Session(
            name = "second_program",
            exercises = listOf(
                Exercise(name = "B", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "A1", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "D", series = Series(1), restAfter = Rest(duration = 120)),
                Exercise(
                    name = "C1",
                    series = Series(3),
                    restAfter = Rest(duration = 120),
                    speed = ESpeed.FAST
                ),
                Exercise(name = "E", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "F", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "G", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "K2", series = Series(3), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)

        assertEquals(expected, session)
    }

    @Test
    fun `C1 first serie result is 10 or more, next session replace C1 by C3`() {
        val previousSession = Session(
            name = "second_program",
            exercises = listOf(
                Exercise(name = "B", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "A1", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "D", series = Series(1), restAfter = Rest(duration = 120)),
                Exercise(
                    name = "C1",
                    series = Series(3, mutableListOf(Repetition(10), Repetition(9), Repetition(9))),
                    restAfter = Rest(duration = 120),
                    speed = ESpeed.FAST
                ),
                Exercise(name = "E", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "F", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "G", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "K2", series = Series(3), restAfter = null)
            )
        )

        val session = sut.createNewSession(previousSession)
        assertEquals(previousSession.replaceExerciseNameAndClearReps("C1", "C3"), session)
    }

    @Test
    fun `C1 first serie result is under 10, next session will be the same`() {
        val previousSession = Session(
            name = "second_program",
            exercises = listOf(
                Exercise(name = "B", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "A1", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "D", series = Series(1), restAfter = Rest(duration = 120)),
                Exercise(
                    name = "C1",
                    series = Series(
                        3,
                        mutableListOf(Repetition(9), Repetition(9), Repetition(9))
                    ),
                    restAfter = Rest(duration = 120),
                    speed = ESpeed.FAST
                ),
                Exercise(name = "E", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "F", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "G", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "K2", series = Series(3), restAfter = null)
            )
        )

        val session = sut.createNewSession(previousSession)
        assertEquals(previousSession.clearExercisesRepetitions(), session)
    }

    @Test
    fun `E first serie result is 15 or more, next session replace E by E1`() {
        val previousSession = Session(
            name = "second_program",
            exercises = listOf(
                Exercise(name = "B", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "A1", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "D", series = Series(1), restAfter = Rest(duration = 120)),
                Exercise(
                    name = "C1",
                    series = Series(3),
                    restAfter = Rest(duration = 120),
                    speed = ESpeed.FAST
                ),
                Exercise(name = "E", series = Series(3, mutableListOf(Repetition(15))), restAfter = Rest(duration = 120)),
                Exercise(name = "F", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "G", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "K2", series = Series(3), restAfter = null)
            )
        )

        val session = sut.createNewSession(previousSession)
        assertEquals(previousSession.replaceExerciseNameAndClearReps("E", "E1"), session)
    }
}

