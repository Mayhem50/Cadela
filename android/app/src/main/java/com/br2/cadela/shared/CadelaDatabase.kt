package com.br2.cadela.shared

import androidx.room.Database
import androidx.room.RoomDatabase
import com.br2.cadela.authentication.signin.TokenDao
import com.br2.cadela.authentication.signin.TokenRecord

@Database(entities = [TokenRecord::class], version = 1)
abstract class CadelaDatabase : RoomDatabase() {
    abstract fun tokenDao(): TokenDao
}
