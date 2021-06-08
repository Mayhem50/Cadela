package com.br2.cadela.workout

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "sessions")
data class SessionRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val session: Session,
    val createdAt: LocalDate = LocalDate.now(),
    val endedAt: LocalDate = LocalDate.now()
)
