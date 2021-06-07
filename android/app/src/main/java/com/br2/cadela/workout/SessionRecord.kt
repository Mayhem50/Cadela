package com.br2.cadela.workout

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "sessions", primaryKeys = ["id"])
data class SessionRecord(
    @PrimaryKey(autoGenerate = true) val id: String = "",
    val session: Session,
    val createdAt: LocalDate = LocalDate.now(),
    val endedAt: LocalDate = LocalDate.now()
)
