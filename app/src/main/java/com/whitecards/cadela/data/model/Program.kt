package com.whitecards.cadela.data.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.PropertyName
import com.whitecards.cadela.data.Settings
import com.whitecards.cadela.services.FirebaseService
import com.whitecards.cadela.utils.ModelHelpers

data class ProgramResult(val program: Program, val levelChanged: Boolean)

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
        fun getProgramFfromPreviousSession(session: Session?): ProgramResult {
            var levelChanged = false

            session?.let {
                TODO("add program computation")
            }

            return ProgramResult(
                FirebaseService.programs.find { program -> program.level == Settings.actualLevel }!!,
                false
            )
        }
    }
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