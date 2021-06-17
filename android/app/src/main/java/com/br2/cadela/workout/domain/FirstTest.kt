package com.br2.cadela.workout.domain

import com.br2.cadela.workout.datas.Repetition
import com.br2.cadela.workout.datas.Session

internal fun WorkoutService.nextSessionAfterFirstLevelTest(previousSession: Session): Session {
    val repetitionsForB =
        previousSession.exercises.find { it.name == "B" }?.series?.repetitions?.elementAt(0)
            ?: Repetition(0)
    val repetitionsForC =
        previousSession.exercises.find { it.name == "C" }?.series?.repetitions?.elementAt(0)
            ?: Repetition(0)
    return if (repetitionsForB.done < 4) {
        if (repetitionsForC.done > 0) Session.FIRST_PROGRAM else Session.FIRST_PROGRAM_WITH_C4
    } else {
        if (repetitionsForC.done > 0) Session.SECOND_PROGRAM else Session.SECOND_PROGRAM_WITH_C4
    }
}