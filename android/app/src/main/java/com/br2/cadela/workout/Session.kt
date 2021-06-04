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
                Exercise(name = "A"),
                Exercise(name = "B"),
                Exercise(name = "C"),
                Exercise(name = "A1")
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
                Exercise(name = "A"),
                Exercise(name = "D"),
                Exercise(name = "C1"),
                Exercise(name = "E"),
                Exercise(name = "F"),
                Exercise(name = "G"),
                Exercise(name = "K2")
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
                Exercise(name = "A"),
                Exercise(name = "D"),
                Exercise(name = "C4"),
                Exercise(name = "E"),
                Exercise(name = "F"),
                Exercise(name = "G"),
                Exercise(name = "K2")
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

        val FIRST_PROGRAM_WITH_C5 = Session(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A"),
                Exercise(name = "D"),
                Exercise(name = "C5"),
                Exercise(name = "E"),
                Exercise(name = "F"),
                Exercise(name = "G"),
                Exercise(name = "K2")
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

        val FIRST_PROGRAM_WITH_C6 = Session(
            name = "1st Program",
            exercises = listOf(
                Exercise(name = "A"),
                Exercise(name = "D"),
                Exercise(name = "C6"),
                Exercise(name = "E"),
                Exercise(name = "F"),
                Exercise(name = "G"),
                Exercise(name = "K2")
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

        val SECOND_PROGRAM = Session(
            name = "2nd Program",
            exercises = listOf(
                Exercise(name = "B"),
                Exercise(name = "A1"),
                Exercise(name = "D"),
                Exercise(name = "C1"),
                Exercise(name = "E"),
                Exercise(name = "F"),
                Exercise(name = "G"),
                Exercise(name = "K2")
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