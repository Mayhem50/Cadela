package com.br2.cadela.workout

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SessionDao {
    @Query("SELECT * FROM sessions ORDER BY createdAt DESC LIMIT 1")
    fun getLastSession(): SessionRecord?

    @Insert
    fun save(session: SessionRecord)
}
