package com.br2.cadela.workout

import androidx.room.Dao

@Dao
interface SessionDao {
    fun getLastSession(): Session?
}
