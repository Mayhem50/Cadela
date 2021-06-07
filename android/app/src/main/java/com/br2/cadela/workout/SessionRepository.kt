package com.br2.cadela.workout

class SessionRepository(private val sessionDao: SessionDao) {
    fun getLastSession(): Session? {
        return sessionDao.getLastSession()
    }

}
