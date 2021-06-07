package com.br2.cadela.workout

data class Session(
    val name: String,
    val exercises: List<Exercise>,
    val restsBetweenExercises: List<Rest>
) {
    companion object {
        val FIRST_LEVEL_TEST = Session(
            name = "1st Level Test",
            exercises = listOf(
                Exercise(name = "A", series = Series(1)),
                Exercise(name = "B", series = Series(1)),
                Exercise(name = "C", series = Series(1)),
                Exercise(name = "A1", series = Series(1))
            ),
            restsBetweenExercises = listOf(
                Rest(duration = 180),
                Rest(duration = 180),
                Rest(duration = 180)
            )
        )
        val FIRST_PROGRAM = Session(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A1", series = Series(2)),
                Exercise(name = "D", series = Series(2)),
                Exercise(name = "C1", series = Series(2)),
                Exercise(name = "E", series = Series(2)),
                Exercise(name = "F", series = Series(2)),
                Exercise(name = "G", series = Series(2)),
                Exercise(name = "K2", series = Series(2))
            ),
            restsBetweenExercises = listOf(
                Rest(duration = 120),
                Rest(duration = 120),
                Rest(duration = 120),
                Rest(duration = 120),
                Rest(duration = 120),
                Rest(duration = 120)
            )
        )

        val FIRST_PROGRAM_WITH_C4 = Session(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A1", series = Series(1)),
                Exercise(name = "D", series = Series(1)),
                Exercise(name = "C4", series = Series(1)),
                Exercise(name = "E", series = Series(1)),
                Exercise(name = "F", series = Series(1)),
                Exercise(name = "G", series = Series(1)),
                Exercise(name = "K2", series = Series(1))
            ),
            restsBetweenExercises = listOf(
                Rest(duration = 120),
                Rest(duration = 120),
                Rest(duration = 120),
                Rest(duration = 120),
                Rest(duration = 120),
                Rest(duration = 120)
            )
        )

        val ONLY_B_TEST = Session(
            name = "Only B Test",
            exercises = listOf(
                Exercise(name = "B", series = Series(1)),
            ),
            restsBetweenExercises = listOf()
        )

        val FIRST_PROGRAM_WITH_B1 = Session(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "B1", series = Series(3)),
                Exercise(name = "A6", series = Series(3)),
                Exercise(name = "A2", series = Series(3)),
                Exercise(name = "D", series = Series(3)),
                Exercise(name = "C1", series = Series(3)),
                Exercise(name = "E", series = Series(3)),
                Exercise(name = "F", series = Series(3)),
                Exercise(name = "G", series = Series(3)),
                Exercise(name = "K2", series = Series(3))
            ),
            restsBetweenExercises = listOf(
                Rest(duration = 120),
                Rest(duration = 120),
                Rest(duration = 120),
                Rest(duration = 120),
                Rest(duration = 120),
                Rest(duration = 120),
                Rest(duration = 120),
                Rest(duration = 120)
            )
        )

        val SECOND_PROGRAM = Session(
            name = "2nd Program",
            exercises = listOf(
                Exercise(name = "B", series = Series(3)),
                Exercise(name = "A1", series = Series(3)),
                Exercise(name = "D", series = Series(1)),
                Exercise(name = "C1", series = Series(3)),
                Exercise(name = "E", series = Series(3)),
                Exercise(name = "F", series = Series(3)),
                Exercise(name = "G", series = Series(3)),
                Exercise(name = "K2", series = Series(3))
            ),
            restsBetweenExercises = listOf(
                Rest(duration = 120),
                Rest(duration = 120),
                Rest(duration = 120),
                Rest(duration = 120),
                Rest(duration = 120),
                Rest(duration = 120),
                Rest(duration = 120)
            )
        )
    }
}