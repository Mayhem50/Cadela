package com.br2.cadela.workout.domain

import com.br2.cadela.workout.datas.*
import java.time.LocalDate
import java.time.temporal.ChronoUnit

internal const val TWO_WEEKS = 2
internal const val THREE_WEEKS = 2

internal fun sessionIsStartedSince(previousSession: Session, weekCount: Int) =
    ChronoUnit.WEEKS.between(previousSession.levelStartedAt, LocalDate.now()) >= weekCount

internal fun replaceCxExerciseForBeginner(exercises: MutableList<Exercise>) {
    exercises.replaceExercise("C4", "C5", 12)
    exercises.replaceExercise("C5", "C6", 12)
    exercises.replaceExercise("C6", "C1", 12)
}

internal fun buildSession(
    previousSession: Session,
    exercises: MutableList<Exercise>
) = Session(previousSession.name, exercises).clearExercisesRepetitions()

internal fun stillOnSameLevel(
    previousSession: Session?,
    nextSession: Session
) = previousSession?.name == nextSession.name ||
        (stillOnFirstLevel(previousSession, nextSession))

internal fun stillOnFirstLevel(
    previousSession: Session?,
    nextSession: Session
) =
    previousSession?.name == Session.FIRST_PROGRAM.name && nextSession.name == Session.SECOND_PROGRAM.name

