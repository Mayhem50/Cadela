package com.br2.cadela.workout

import com.br2.cadela.workout.datas.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FirstLevelSessionSecondProgramTest: WorkoutTestBase() {
    @Test
    fun `B session result is 8 or more on first serie, next session will be 2nd level`() {
        val previousSession = Session(
            name = "second_program",
            exercises = listOf(
                Exercise(name = "B", series = Series(3, mutableListOf(Repetition(8), Repetition(7), Repetition(6))), restAfter = Rest(duration = 120)),
                Exercise(name = "A1", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "D", series = Series(1), restAfter = Rest(duration = 120)),
                Exercise(name = "C1", series = Series(3), restAfter = Rest(duration = 120), speed = ESpeed.FAST),
                Exercise(name = "E", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "F", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "G", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "K2", series = Series(3), restAfter = null)
            )
        )

        val session = sut.createNewSession(previousSession)
        assertEquals(Session.SECOND_LEVEL, session)
    }
}