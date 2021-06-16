package com.br2.cadela.shared

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.br2.cadela.authentication.signin.TokenDao
import com.br2.cadela.authentication.signin.TokenRecord
import com.br2.cadela.workout.repositories.SessionDao
import com.br2.cadela.workout.repositories.SessionRecord
import com.br2.cadela.workout.repositories.WorkoutTypeConverter

@Database(
    entities = [
        TokenRecord::class,
        SessionRecord::class
    ],
    version = 1
)
@TypeConverters(WorkoutTypeConverter::class)
abstract class CadelaDatabase : RoomDatabase() {
    abstract fun tokenDao(): TokenDao
    abstract fun sessionDao(): SessionDao
}
