package com.br2.cadela.workout.domain

import com.br2.cadela.workout.datas.*

internal fun WorkoutService.nextSessionAfter1stProgram(previousSession: Session): Session {
    if (sessionIsStartedSince(previousSession, TWO_WEEKS)) {
        return Session.SECOND_PROGRAM
    }
    val exercises = previousSession.exercises.toMutableList()

    replaceCxExerciseForBeginner(exercises)

    exercises.replaceExercise("A1", "A2", 8)
    exercises.replaceExercise("A3", "A4", 8)
    exercises.replaceExercise("A4", "A5", 8)
    exercises.replaceExercise("A5", "A6", 8)

    exercises.find { it.name == "A2" }?.let {
        if (shouldReplaceA2(it, exercises)) {
            exercises.add(0, Exercise("A3", Series(2), Rest(120)))
        }
    }

    exercises.find { it.name == "A6" }?.let {
        if (it.series.repetitions[0].done >= 8) {
            return Session.ONLY_B_TEST
        }
    }

    return buildSession(previousSession, exercises)
}

private fun shouldReplaceA2(
    exercise: Exercise,
    exercises: List<Exercise>
) = exercise.series.repetitions[0].done >= 8 && exercises.find {
    listOf("A4", "A5", "A6").contains(
        it.name
    )
} == null