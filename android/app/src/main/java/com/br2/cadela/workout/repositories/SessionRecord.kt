package com.br2.cadela.workout.repositories

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.br2.cadela.workout.datas.Session
import java.time.LocalDate

@Entity(tableName = "sessions")
data class SessionRecord(
    val session: Session,
    @PrimaryKey val name: String = session.name,
    val createdAt: LocalDate = LocalDate.now(),
    val endedAt: LocalDate? = null
)
