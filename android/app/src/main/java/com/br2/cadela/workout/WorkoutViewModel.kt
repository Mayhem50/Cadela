package com.br2.cadela.workout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.awt.font.NumericShaper

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
        _currentExercise.value = _currentSession.value?.exercises?.elementAt(0)
    }

    fun moveToNextExercise() {

        val currentExerciseIndex = _currentSession.value?.exercises?.indexOf(_currentExercise.value)
        currentExerciseIndex?.let {
            if(IntRange(0, (_currentSession.value?.exercises?.size ?: 0) - 2).contains(currentExerciseIndex)) {
                _currentExercise.value =
                    _currentSession.value?.exercises?.elementAt(currentExerciseIndex + 1)
            } else {
                _currentExercise.value = null
            }
        }
    }

    fun setRepsForCurrentSerie(done: Int) {
        _currentExercise.value?.let {
            it.series.repetitions[0] = done
        }
    }
}