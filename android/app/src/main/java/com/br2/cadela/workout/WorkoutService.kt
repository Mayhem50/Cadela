package com.br2.cadela.workout

class WorkoutService {
    fun createNewSession(sessionResult: SessionResult? = null): Session {
        return when (sessionResult?.name) {
            "1st Level Test" -> nextSessionAfterFirstLevelTest(sessionResult)
            "1st Program" -> nextSessionAfter1stProgram(sessionResult)
            else -> Session.FIRST_LEVEL_TEST
        }
    }

    private fun nextSessionAfter1stProgram(sessionResult: SessionResult): Session {
        sessionResult.exercises.find { it.name == "C4" }?.let {
            return if (it.series.repetitions[0] < 12) Session.FIRST_PROGRAM_WITH_C4 else Session.FIRST_PROGRAM_WITH_C5
        }
        sessionResult.exercises.find { it.name == "C5" }?.let {
            return if (it.series.repetitions[0] < 12) Session.FIRST_PROGRAM_WITH_C5 else Session.FIRST_PROGRAM_WITH_C6
        }
        sessionResult.exercises.find { it.name == "C6" }?.let {
            return if(it.series.repetitions[0] < 12) Session.FIRST_PROGRAM_WITH_C6 else Session.FIRST_PROGRAM
        }
        return Session.FIRST_PROGRAM_WITH_C4
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