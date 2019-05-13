package com.whitecards.cadela.utils

import com.whitecards.cadela.data.model.ERythm
import com.whitecards.cadela.data.model.Exercise
import java.util.*

object ModelHelpers {
    fun stringToListInt(data: String): ArrayList<Int> {
        var result = ArrayList<Int>()
        var items = data.split("/")
        for (item in items) {
            result.add(item.toInt())
        }

        return result
    }

    fun stringToListExercise(data: String): ArrayList<Exercise> {
        var result = ArrayList<Exercise>()
        var items = data.split("/")
        for (item in items) {
            result.add(Exercise().apply { name = item })
        }

        return result
    }

    fun stringToListRythm(data: String): ArrayList<ERythm> {
        var result = ArrayList<ERythm>()
        var items = data.split("/")
        for (item in items) {
            if (item == "R") result.add(ERythm.Quick)
            if (item == "S") result.add(ERythm.Slow)
            else result.add(ERythm.Normal)
        }

        return result
    }
}