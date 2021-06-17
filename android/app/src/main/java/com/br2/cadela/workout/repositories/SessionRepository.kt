package com.br2.cadela.workout.repositories

import com.br2.cadela.workout.datas.Session
import java.time.LocalDate

class SessionRepository(private val sessionDao: SessionDao) {
    suspend fun getLastSession(): Session? {
        return sessionDao.getLastSession()?.session
    }

    suspend fun saveSession(session: Session, endDate: LocalDate?) {
        sessionDao.save(SessionRecord(session = session, endedAt = endDate))
    }

}
