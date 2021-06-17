package com.br2.cadela.workout.domain

import com.br2.cadela.workout.datas.Session
import com.br2.cadela.workout.datas.changeTarget
import com.br2.cadela.workout.datas.replaceExercise

internal fun WorkoutService.nextSessionAfterLevel2(previousSession: Session): Session {
    if (canGoToLevel3(previousSession)) {
        return Session.THIRD_LEVEL
    }

    val exercises = previousSession.exercises.toMutableList()

    exercises.replaceExercise("E", "E1", 10)
    exercises.changeTarget("C1", 3, 5)
    return buildSession(previousSession, exercises)
}

private fun canGoToLevel3(session: Session): Boolean{
    val exercises = session.exercises
    val reps = exercises.filter { it.name != "K2" && it.name != "G" }.flatMap { it.series.repetitions }
    val meanReps = reps.sumOf { it.done } / reps.size
    return meanReps >= 8
}