package com.br2.cadela.workout.datas

val Session.exerciseNames: List<String>
    get() = exercises.map { it.name }

fun Session.changeTargetForExercise(search: String, newTarget: Int): Session {
    val exercises = exercises.toMutableList()
    val index = exercises.indexOfFirst { it.name == search }
    if (index >= 0) {
        val exercise = exercises[index]
        exercises[index] = Exercise(
            exercise.name,
            exercise.series.withTarget(newTarget),
            exercise.restAfter,
            exercise.speed
        )
    }

    return Session(name, exercises, levelStartedAt)
}

fun Session.clearExercisesRepetitions() = Session(
    name,
    exercises.map {
        Exercise(
            it.name,
            Series(
                it.series.count,
                it.series.repetitions.map { Repetition(0, it.target) }.toMutableList(),
                it.series.restAfter
            ),
            it.restAfter,
            it.speed
        )
    }, levelStartedAt
)

fun Session.changeTargetForExerciseAndClearReps(search: String, newTarget: Int) =
    clearExercisesRepetitions().changeTargetForExercise(search, newTarget)

fun Session.replaceExerciseNameAndClearReps(search: String, replaceBy: String) =
    clearExercisesRepetitions().replaceExerciseName(search, replaceBy)

fun Session.updateTargetRepsBasedOnPreviousSession(previousSession: Session?): Session {
    if (previousSession == null) return this
    val newExercises = mutableListOf<Exercise>()
    exercises.forEachIndexed { index, exercise ->
        val previousExercise = previousSession.exercises.find { it.name == exercise.name }
        val haveTargetSet = exercise.series.repetitions.any { it.target != 0 }
        if (exercise.name != "K2" && previousExercise != null && !haveTargetSet) {
            val repetitions = mutableListOf<Repetition>()
            previousExercise.series.repetitions.forEach { repetition ->
                val nextRepetition =
                    if (repetition.done > 0) Repetition(0, repetition.done + 1)
                    else Repetition(0, repetition.target)
                repetitions.add(nextRepetition)
            }

            while (repetitions.size < exercise.series.count) {
                repetitions.add(Repetition(0, 0))
            }

            val newExercise = Exercise(
                exercise.name,
                Series(
                    exercise.series.count,
                    repetitions.subList(0, exercise.series.count),
                    exercise.series.restAfter
                ),
                exercise.restAfter,
                exercise.speed
            )
            newExercises.add(newExercise)
        } else {
            newExercises.add(exercise)
        }
    }

    return Session(name, newExercises, levelStartedAt)
}