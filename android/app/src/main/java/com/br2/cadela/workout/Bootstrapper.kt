package com.br2.cadela.workout

import com.br2.cadela.shared.CadelaDatabase

object WorkoutModule {
    private lateinit var _workoutVm: WorkoutViewModel
    val workoutVm: WorkoutViewModel
        get() = _workoutVm

    fun bootstrap(db: CadelaDatabase) {
        val sessionRepository = SessionRepository(db.sessionDao())
        val workoutService = WorkoutService(sessionRepository)
        _workoutVm = WorkoutViewModel(workoutService)
    }
}