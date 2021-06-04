package com.br2.cadela.workout

import kotlin.streams.toList

class WorkoutService {
    fun createNewSession(sessionResult: SessionResult? = null): Session {
        return when (sessionResult?.name) {
            "1st Level Test" -> nextSessionAfterFirstLevelTest(sessionResult)
            "1st Program" -> nextSessionAfter1stProgram(sessionResult)
            else -> Session.FIRST_LEVEL_TEST
        }
    }

    private fun nextSessionAfter1stProgram(sessionResult: SessionResult): Session {
        val exercises = sessionResult.exercises.toMutableList()
        val restBetweenExercises = Session.FIRST_PROGRAM.restsBetweenExercises.toMutableList()

        var index = exercises.indexOfFirst { it.name == "C4" }
        if(index >= 0 ){
            val exercise = exercises[index]
            if (exercise.series.repetitions[0] >= 12) {
                exercises[index] = Exercise("C5", Series(exercise.series.count))
            }
        }

        index = exercises.indexOfFirst { it.name == "C5" }
        if(index >= 0 ){
            val exercise = exercises[index]
            if (exercise.series.repetitions[0] >= 12) {
                exercises[index] = Exercise("C6", Series(exercise.series.count))
            }
        }

        index = exercises.indexOfFirst { it.name == "C6" }
        if(index >= 0 ){
            val exercise = exercises[index]
            if (exercise.series.repetitions[0] >= 12) {
                exercises[index] = Exercise("C1", Series(exercise.series.count))
            }
        }

        index = exercises.indexOfFirst { it.name == "A1" }
        if(index >= 0 ){
            val exercise = exercises[index]
            if (exercise.series.repetitions[0] >= 8) {
                exercises[index] = Exercise("A2", Series(exercise.series.count))
            }
        }

        exercises.find { it.name == "A2" }?.let {
            if(it.series.repetitions[0] >= 8) {
                exercises.add(0, Exercise("A3", Series(2)))
                restBetweenExercises.add(0, Rest(120))
            }
        }

        return Session(sessionResult.name, exercises.stream().map {
            Exercise(it.name, Series(it.series.count))
        }.toList(), restBetweenExercises)
    }

    private fun nextSessionAfterFirstLevelTest(sessionResult: SessionResult): Session {
        val repetitionsForB =
            sessionResult.exercises.find { it.name == "B" }?.series?.repetitions?.elementAt(0) ?: 0
        val repetitionsForC =
            sessionResult.exercises.find { it.name == "C" }?.series?.repetitions?.elementAt(0) ?: 0
        return if (repetitionsForB < 4) {
            return if (repetitionsForC > 0) Session.FIRST_PROGRAM else Session.FIRST_PROGRAM_WITH_C4
        } else {
            Session.SECOND_PROGRAM
        }
    }
}