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
        val restBetweenExercises = MutableList(exercises.size - 1){ Rest(120) }

        changeExercise("C4", "C5", 12, exercises)
        changeExercise("C5", "C6", 12, exercises)
        changeExercise("C6", "C1", 12, exercises)

        changeExercise("A1", "A2", 8, exercises)
        changeExercise("A3", "A4", 8, exercises)

        exercises.find { it.name == "A2" }?.let {
            if(it.series.repetitions[0] >= 8 && exercises.find { it.name == "A4" } == null) {
                exercises.add(0, Exercise("A3", Series(2)))
                restBetweenExercises.add(0, Rest(120))
            }
        }

        return Session(sessionResult.name, exercises.stream().map {
            Exercise(it.name, Series(it.series.count))
        }.toList(), restBetweenExercises)
    }

    private fun changeExercise(
        searchName: String,
        replaceBy: String,
        threshold: Int,
        exercises: MutableList<Exercise>
    ) {
        val index = exercises.indexOfFirst { it.name == searchName }
        if (index >= 0) {
            val exercise = exercises[index]
            if (exercise.series.repetitions[0] >= threshold) {
                exercises[index] = Exercise(replaceBy, Series(exercise.series.count))
            }
        }
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