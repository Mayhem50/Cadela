package com.br2.cadela.workout

import java.time.LocalDate

data class Session(
    val name: String,
    val exercises: List<Exercise>,
    val levelStartedAt: LocalDate = LocalDate.now()
) {
    val isComplete: Boolean
        get() = exercises.flatMap { it.series.repetitions }.all { it > 0 }


    companion object {
        val FIRST_LEVEL_TEST = Session(
            name = "1st Level Test",
            exercises = listOf(
                Exercise(name = "A", series = Series(1), restAfter = Rest(180)),
                Exercise(name = "B", series = Series(1), restAfter = Rest(180)),
                Exercise(name = "C", series = Series(1), restAfter = Rest(180)),
                Exercise(name = "A1", series = Series(1), restAfter = null)
            )
        )
        val FIRST_PROGRAM = Session(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A1", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "D", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "C1", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "E", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "F", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "G", series = Series(2), restAfter = Rest(120)),
                Exercise(name = "K2", series = Series(2), restAfter = null)
            )
        )

        val FIRST_PROGRAM_WITH_C4 = Session(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A1", series = Series(1), restAfter = Rest(120)),
                Exercise(name = "D", series = Series(1), restAfter = Rest(120)),
                Exercise(name = "C4", series = Series(1), restAfter = Rest(120)),
                Exercise(name = "E", series = Series(1), restAfter = Rest(120)),
                Exercise(name = "F", series = Series(1), restAfter = Rest(120)),
                Exercise(name = "G", series = Series(1), restAfter = Rest(120)),
                Exercise(name = "K2", series = Series(1), restAfter = null)
            )
        )

        val ONLY_B_TEST = Session(
            name = "Only B Test",
            exercises = listOf(
                Exercise(name = "B", series = Series(1), restAfter = null),
            )
        )

        val FIRST_PROGRAM_WITH_B1 = Session(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "B1", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "A6", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "A2", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "D", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "C1", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "E", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "F", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "G", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "K2", series = Series(3), restAfter = Rest(duration = 120))
            )
        )

        val SECOND_PROGRAM = Session(
            name = "2nd Program",
            exercises = listOf(
                Exercise(name = "B", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "A1", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "D", series = Series(1), restAfter = Rest(duration = 120)),
                Exercise(name = "C1", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "E", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "F", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "G", series = Series(3), restAfter = Rest(duration = 120)),
                Exercise(name = "K2", series = Series(3), restAfter = null)
            )
        )

        val SECOND_LEVEL = Session(
            name = "2nd Level",
            exercises = listOf(
                Exercise(name = "B1", series = Series(6), restAfter = Rest(duration = 25)),
                Exercise(name = "A3", series = Series(6), restAfter = Rest(duration = 25)),
                Exercise(name = "A2", series = Series(6), restAfter = Rest(duration = 180)),
                Exercise(name = "C1", series = Series(6), restAfter = Rest(duration = 180)),
                Exercise(name = "E", series = Series(6), restAfter = Rest(duration = 180)),
                Exercise(name = "F", series = Series(4), restAfter = Rest(duration = 180)),
                Exercise(name = "G", series = Series(6), restAfter = Rest(duration = 90)),
                Exercise(name = "H", series = Series(6), restAfter = Rest(duration = 60)),
                Exercise(name = "K2", series = Series(3), restAfter = null)
            )
        )
    }
}