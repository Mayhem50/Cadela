package com.br2.cadela.workout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkoutViewModel(private val workoutService: WorkoutService) : ViewModel() {
    private var _currentSerieIndex = 0

    private val _currentExercise = MutableLiveData<Exercise?>()
    val currentExercise: LiveData<Exercise?>
        get() = _currentExercise

    private var _currentSession = MutableLiveData<Session?>()
    val currentSession: LiveData<Session?>
        get() = _currentSession

    fun startSession() = viewModelScope.launch(Dispatchers.IO) {
        val session = workoutService.startNewSession()
        withContext(Dispatchers.Main) { _currentSession.value = session }
    }

    fun endSession() = viewModelScope.launch(Dispatchers.IO) {
        _currentSession.value?.let {
            workoutService.endSession(it)
            withContext(Dispatchers.Main) {
                _currentSession.value = null
                _currentExercise.value = null
            }
        }
    }

    fun pauseSession() = viewModelScope.launch(Dispatchers.IO) {
        _currentSession.value?.let {
            workoutService.pauseSession(it)
        }
    }

    fun runSession() {
        _currentExercise.value = workoutService.runSession()
    }

    fun setRepsForCurrentSerie(done: Int) = viewModelScope.launch(Dispatchers.IO) {
        _currentExercise.value?.let {
            it.series.repetitions[_currentSerieIndex] = Repetition(done)
            _currentSerieIndex += 1
            if (_currentSerieIndex < it.series.repetitions.size) {
                waitBetweenSeries()
            } else {
                restAndMoveToNextExercise(it)
            }
        }
    }

    private suspend fun waitBetweenSeries() {
        _currentExercise.value?.let {
            workoutService.waitFor(it.series.restAfter.duration)
        }
    }

    private suspend fun restAndMoveToNextExercise(it: Exercise) {
        workoutService.waitFor(it.restAfter?.duration ?: 0)
        withContext(Dispatchers.Main) {
            _currentExercise.value = workoutService.moveToNextExercise()
            _currentSerieIndex = 0
        }
    }
}