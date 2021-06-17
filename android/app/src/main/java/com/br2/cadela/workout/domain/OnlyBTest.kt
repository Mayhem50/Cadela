package com.br2.cadela.workout.domain

import com.br2.cadela.workout.datas.Session

internal fun WorkoutService.nextSessionAfterOnlyBTest(previousSession: Session): Session {
    if (previousSession.exercises[0].series.repetitions[0].done >= 8) {
        return Session.SECOND_LEVEL
    }
    if (previousSession.exercises[0].series.repetitions[0].done > 5) {
        return Session.SECOND_PROGRAM
    }
    return Session.FIRST_PROGRAM_WITH_B1
}