package com.whitecards.cadela.data.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.PropertyName
import com.whitecards.cadela.data.Settings
import com.whitecards.cadela.services.FirebaseService
import com.whitecards.cadela.utils.ModelHelpers
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit

data class ProgramResult(var program: Program, var levelChanged: Boolean)

class Program {
    @set:PropertyName("level")
    @get:PropertyName("level")
    var level: Int = 0
    @set:PropertyName("index")
    @get:PropertyName("index")
    var index: Int = 0

    @set:PropertyName("series")
    @get:PropertyName("series")
    var seriesString: String = ""
        set(value) {
            series = ModelHelpers.stringToListInt(value)
        }

    @set:PropertyName("rest_in")
    @get:PropertyName("rest_in")
    var restInTimesString: String = ""
        set(value) {
            restInTimes = ModelHelpers.stringToListInt(value)
        }

    @set:PropertyName("rest_between_exo")
    @get:PropertyName("rest_between_exo")
    var restBetweenTimeString: String = ""
        set(value) {
            restBetweenTime = ModelHelpers.stringToListInt(value)
        }

    @set:PropertyName("start")
    @get:PropertyName("start")
    var targetRepetitionAtStartString: String = ""
        set(value) {
            targetRepetitionAtStart = ModelHelpers.stringToListInt(value)
        }

    @set:PropertyName("rythm")
    @get:PropertyName("rythm")
    var rythmsString: String = ""
        set(value) {
            rythms = ModelHelpers.stringToListRythm(value)
        }

    @set:PropertyName("exercises")
    @get:PropertyName("exercises")
    var exercisesString: String = ""
        set(value) {
            exercises = ModelHelpers.stringToListExercise(value)
        }

    @set:Exclude
    @get:Exclude
    lateinit var series: ArrayList<Int>

    @set:Exclude
    @get:Exclude
    lateinit var restInTimes: ArrayList<Int>

    @set:Exclude
    @get:Exclude
    lateinit var restBetweenTime: ArrayList<Int>

    @set:Exclude
    @get:Exclude
    lateinit var targetRepetitionAtStart: ArrayList<Int>

    @set:Exclude
    @get:Exclude
    lateinit var rythms: ArrayList<ERythm>

    @set:Exclude
    @get:Exclude
    lateinit var exercises: ArrayList<Exercise>

    companion object {
        fun getProgramFromPreviousSession(session: Session?): ProgramResult {
            session?.let {
                return when (session.program.level) {
                    1 -> adaptForLevel1(session)
                    2 -> adaptForLevel2(session)
                    3 -> adaptForLevel3(session)
                    else -> ProgramResult(session.program, false)
                }
            }

            return ProgramResult(
                FirebaseService.programs.find { program -> program.level == Settings.actualLevel }!!,
                false
            )
        }

        private fun adaptForLevel3(session: Session): ProgramResult {
            var result = ProgramResult(session.program, false)
            var offset = 0

            for (i in session.program.exercises.indices) {
                var seriesCount = session.program.series[i]
                var repetitions = session.seriesRepetitions.subList(offset, seriesCount + offset)
                var exercise = session.program.exercises[i]
                var exercises = FirebaseService.exercises

                var meanRepetitions = computeMeanRepetitionForExercice(repetitions)
                session.meanSeriesRepetitions.add(meanRepetitions)

                if (exercise.name == "A3" && meanRepetitions > 8) {
                    result.program.exercises.add(0, exercises.find { e -> e.name == "A4" }!!)
                } else if (exercise.name == "A4" && meanRepetitions > 8) {
                    result.program.exercises.add(0, exercises.find { e -> e.name == "A5" }!!)
                } else if (exercise.name == "A5" && meanRepetitions > 8) {
                    result.program.exercises.add(0, exercises.find { e -> e.name == "A6" }!!)
                }

                if (exercise.name == "B2" && meanRepetitions > 10) {
                    result.program = FirebaseService.programs.find { p -> p.level == session.program.level + 1 }!!

                    // Start Level 4 with the A.x exercise of Level 3
                    result.program.exercises[1] = session.program.exercises[1]
                    return result
                }

                offset += seriesCount
            }

            return result
        }

        private fun adaptForLevel2(session: Session): ProgramResult {
            var result = ProgramResult(session.program, false)
            var offset = 0

            var meanProgramRepetitions = 0
            var exerciseCount = 0

            for (i in session.program.exercises.indices) {
                var seriesCount = session.program.series[i]
                var repetitions = session.seriesRepetitions.subList(offset, seriesCount + offset)
                var exercise = session.program.exercises[i]
                var exercises = FirebaseService.exercises

                var meanRepetition = computeMeanRepetitionForExercice(repetitions)

                if (!exercise.name.contains("G") && !exercise.name.contains("K")) {
                    meanProgramRepetitions += meanRepetition
                    exerciseCount++
                }

                session.meanSeriesRepetitions.add(meanRepetition)

                if (exercise.name == "A2" && meanRepetition < 5) {
                    result.program.exercises[i] = exercises.find { e -> e.name == "A1" }!!
                } else if (exercise.name == "A1" && meanRepetition > 8) {
                    result.program.exercises.add(0, exercises.find { e -> e.name == "A3" }!!)
                }

                if (exercise.name == "E" && meanRepetition > 10) {
                    result.program.exercises[i] = exercises.find { e -> e.name == "E1" }!!
                }

                offset += seriesCount
            }

            if (meanProgramRepetitions / exerciseCount > 8) {
                result.program = FirebaseService.programs.find { p -> p.level == session.program.level + 1 }!!
                result.levelChanged = true
                return result
            }

            return result
        }

        private fun adaptForLevel1(session: Session): ProgramResult {
            var result = ProgramResult(session.program, false)

            var offset = 0

            // After 2 weeks add one series
            if (TimeUnit.DAYS.convert(
                    LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - session.dateOfProgramBegining.toEpochSecond(
                        ZoneOffset.UTC
                    ), TimeUnit.SECONDS
                ) / 7 >= 2 && session.program.index == 0
            ) {
                for (i in result.program.series.indices) {
                    result.program.series[i] = 3
                }
                return result
            }

            if (TimeUnit.DAYS.convert(
                    LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - session.dateOfProgramBegining.toEpochSecond(
                        ZoneOffset.UTC
                    ), TimeUnit.SECONDS
                ) / 7 >= 3 && session.program.index == 1
            ) {
                result.program = FirebaseService.programs.find { p -> p.level == session.program.level + 1 }!!
                result.levelChanged = true
                return result
            }

            for (i in session.program.exercises.indices) {
                var seriesCount = session.program.series[i]
                var repetitions = session.seriesRepetitions.subList(offset, seriesCount + offset)
                var exercise = session.program.exercises[i]
                var exercises = FirebaseService.exercises

                var meanRepetitions = computeMeanRepetitionForExercice(repetitions)
                session.meanSeriesRepetitions.add(meanRepetitions)

                if (exercise.name == "A1" && meanRepetitions > 8 && session.program.index == 0) {
                    result.program.exercises[i] = exercises.find { e -> e.name == "A2" }!!
                } else if (exercise.name == "A2" && meanRepetitions > 8 && session.program.index == 0) {
                    result.program.exercises.add(0, exercises.find { e -> e.name == "A3" }!!)
                } else if (exercise.name == "A3" && meanRepetitions > 8 && session.program.index == 0) {
                    result.program.exercises[i] = exercises.find { e -> e.name == "A4" }!!
                } else if (exercise.name == "A4" && meanRepetitions > 8 && session.program.index == 0) {
                    result.program.exercises[i] = exercises.find { e -> e.name == "A5" }!!
                } else if (exercise.name == "A5" && meanRepetitions > 8 && session.program.index == 0) {
                    result.program.exercises[i] = exercises.find { e -> e.name == "A6" }!!
                } else if (exercise.name == "A6" && meanRepetitions > 8 && session.program.index == 0) {
                    result.program = test
                    return result
                }

                if (exercise.name == "A1" && meanRepetitions > 8 && session.program.index == 1) {
                    result.program.exercises[i] = exercises.find { e -> e.name == "A2" }!!
                }
                if (exercise.name == "C1" && meanRepetitions > 8 && session.program.index == 1) {
                    result.program.exercises[i] = exercises.find { e -> e.name == "C3" }!!
                }
                if (exercise.name == "B" && meanRepetitions > 8 && session.program.index == 1) {
                    result.program = FirebaseService.programs.find { p -> p.level == session.program.level + 1 }!!
                    return result
                }

                offset += seriesCount
            }

            return result
        }
    }
}

private fun computeMeanRepetitionForExercice(repetitions: List<Int>): Int {
    var sum = 0
    for (repetition in repetitions) {
        sum += repetition
    }

    return sum / repetitions.size
}

val test = Program(
).apply {
    index = 999
    level = 999
    series = arrayListOf(1, 1, 1, 1)
    restInTimes = arrayListOf(0, 0, 0)
    restBetweenTime = arrayListOf(180, 180, 180)
    targetRepetitionAtStart = arrayListOf(0, 0, 0, 0)
    rythms = arrayListOf(ERythm.Normal, ERythm.Normal, ERythm.Normal, ERythm.Normal)
    exercises = arrayListOf(
        FirebaseService.exercises.find { exercise -> exercise.name == "A" }!!,
        FirebaseService.exercises.find { exercise -> exercise.name == "B" }!!,
        FirebaseService.exercises.find { exercise -> exercise.name == "C" }!!,
        FirebaseService.exercises.find { exercise -> exercise.name == "A1" }!!
    )
}