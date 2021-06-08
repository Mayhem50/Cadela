package com.br2.cadela.workout

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SessionDao {
    @Query("SELECT * FROM sessions ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLastSession(): SessionRecord?

    @Insert
    suspend fun save(session: SessionRecord)
}
