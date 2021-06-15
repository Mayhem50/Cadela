package com.br2.cadela.workout

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WorkoutViewModel(private val workoutService: WorkoutService) : ViewModel() {
    private val _currentRest = MutableLiveData<Rest?>(null)
    val currentRest: LiveData<Rest?>
        get() = _currentRest

    private val _currentSerieIndex = MutableLiveData(0)
    val currentSerieIndex: LiveData<Int>
        get() = _currentSerieIndex

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

    fun setRepsForCurrentSerie(done: Int) {
        _currentExercise.value?.let {
            it.series.repetitions[_currentSerieIndex.value!!] = Repetition(done)
            _currentSerieIndex.value = _currentSerieIndex.value!! + 1

            if (_currentSerieIndex.value!! < it.series.repetitions.size) {
                updateCurrentRest(it.series.restAfter)
            } else {
                moveToNextExercise(it)
            }
        }
    }

    private fun moveToNextExercise(it: Exercise) {
        _currentSerieIndex.value = 0
        updateCurrentRest(it.restAfter)
        _currentExercise.value = workoutService.moveToNextExercise()
    }

    private fun updateCurrentRest(rest: Rest?){
        _currentRest.value = rest
    }
}