package com.br2.cadela.workout.repositories

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.br2.cadela.workout.datas.Session
import java.time.LocalDate

@Entity(tableName = "sessions")
data class SessionRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val session: Session,
    val createdAt: LocalDate = LocalDate.now(),
    val endedAt: LocalDate = LocalDate.now()
)
