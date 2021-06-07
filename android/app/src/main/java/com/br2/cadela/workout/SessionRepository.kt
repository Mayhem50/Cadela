package com.br2.cadela.workout

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class SessionRepository(private val sessionDao: SessionDao) {
    fun getLastSession(): Session? = CoroutineScope(Dispatchers.IO).run {
        sessionDao.getLastSession()?.session
    }

    fun saveSession(session: Session) = CoroutineScope(Dispatchers.IO).run {
        sessionDao.save(SessionRecord(session = session))
    }

}
