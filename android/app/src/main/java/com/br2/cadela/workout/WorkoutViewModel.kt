package com.br2.cadela.workout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WorkoutViewModel(private val workoutService: WorkoutService) : ViewModel() {
    private var _currentSession = MutableLiveData<Session?>()
    val currentSession: LiveData<Session?>
        get() = _currentSession

    fun startSession() = viewModelScope.launch(Dispatchers.IO) {
        val session = workoutService.startNewSession()
        _currentSession.value = session
    }

    fun endSession() = viewModelScope.launch(Dispatchers.IO) {
        _currentSession.value?.let {
            workoutService.endSession(it)
            _currentSession.value = null
        }
    }

    fun pauseSession() = viewModelScope.launch(Dispatchers.IO) {
        _currentSession.value?.let {
            workoutService.pauseSession(it)
        }
    }
}