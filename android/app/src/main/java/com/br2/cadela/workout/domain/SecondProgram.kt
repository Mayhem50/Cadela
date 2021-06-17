package com.br2.cadela.workout.domain

import com.br2.cadela.workout.datas.Session
import com.br2.cadela.workout.datas.addRepetitionsOn
import com.br2.cadela.workout.datas.replaceExercise

internal fun WorkoutService.nextSessionAfter2ndProgram(previousSession: Session): Session {
    if (sessionIsStartedSince(previousSession, THREE_WEEKS)) {
        return Session.SECOND_LEVEL
    }
    if (previousSession.exercises[0].series.repetitions[0].done >= 8) return Session.SECOND_LEVEL

    val exercises = previousSession.exercises.toMutableList()
    exercises.replaceExercise("A1", "A2", 8)
    exercises.replaceExercise("C1", "C3", 10)
    exercises.replaceExercise("E", "E1", 15)

    replaceCxExerciseForBeginner(exercises)
    exercises.addRepetitionsOn("C1", 3)

    return buildSession(previousSession, exercises)
}