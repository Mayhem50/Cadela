package com.br2.cadela.workout.repositories

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface SessionDao {
    @Query("SELECT * FROM sessions ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLastSession(): SessionRecord?

    @Insert(onConflict = REPLACE)
    suspend fun save(session: SessionRecord)
}
