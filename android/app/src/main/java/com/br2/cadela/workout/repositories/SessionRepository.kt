package com.br2.cadela.workout.repositories

import com.br2.cadela.workout.datas.Session

class SessionRepository(private val sessionDao: SessionDao) {
    suspend fun getLastSession(): Session? {
        return sessionDao.getLastSession()?.session
    }

    suspend fun saveSession(session: Session) {
        sessionDao.save(SessionRecord(session = session))
    }

}
