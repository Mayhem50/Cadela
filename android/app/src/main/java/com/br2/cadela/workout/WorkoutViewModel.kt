package com.br2.cadela.workout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkoutViewModel(private val workoutService: WorkoutService) : ViewModel() {
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

    fun moveToNextExercise() {
        _currentExercise.value = workoutService.moveToNextExercise()
    }

    fun setRepsForCurrentSerie(done: Int) {
        _currentExercise.value?.let {
            it.series.repetitions[0] = Repetition(done)
            if (it.series.repetitions.size > 1) {
                restAndMoveToNextSerieRepetition()
            } else {
                restAndMoveToNextExercise(it)
            }
        }
    }

    private fun restAndMoveToNextSerieRepetition() = viewModelScope.launch(Dispatchers.IO) {
        _currentExercise.value?.let {
            workoutService.waitFor(it.series.restAfter.duration)
        }
    }

    private fun restAndMoveToNextExercise(it: Exercise) = viewModelScope.launch(Dispatchers.IO) {
        workoutService.waitFor(it.restAfter?.duration ?: 0)
        withContext(Dispatchers.Main) { moveToNextExercise() }
    }
}