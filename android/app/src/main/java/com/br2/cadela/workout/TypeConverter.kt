package com.br2.cadela.workout

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate

class WorkoutTypeConverter {
    @TypeConverter
    fun toLocaleDate(str: String?) = str?.let {
        LocalDate.parse(str)
    }

    @TypeConverter
    fun fromLocalDate(date: LocalDate?) = date?.let {
        date.toString()
    }

    @TypeConverter
    fun toSession(str: String?): Session? = str?.let {
        val gson = Gson()
        val type = object: TypeToken<Session>() {}.type
        gson.fromJson(str, type)
    }

    @TypeConverter
    fun fromSession(session: Session?) = session?.let {
        val gson = Gson()
        val type = object: TypeToken<Session>() {}.type
        gson.toJson(session, type)
    }
}