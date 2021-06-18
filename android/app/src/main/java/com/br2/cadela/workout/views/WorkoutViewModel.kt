package com.br2.cadela.workout.views

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.br2.cadela.workout.datas.Exercise
import com.br2.cadela.workout.datas.Repetition
import com.br2.cadela.workout.datas.Session
import com.br2.cadela.workout.domain.WorkoutService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

class WorkoutViewModel(
    private val workoutService: WorkoutService,
    private val _countDownFactory: CountdownTimerFactory = CountdownTimerFactory()
) : ViewModel() {
    companion object {
        const val EMPTY_TIME_STRING = "--:--"
    }

    private var _previousExercise: Exercise? = null
    private var _countDownTimer: CountDownTimer? = null

    private val _isResting = MutableLiveData(false)
    val isResting: LiveData<Boolean>
        get() = _isResting

    private val _timeDisplay = MutableLiveData(EMPTY_TIME_STRING)
    val timeDisplay: LiveData<String>
        get() = _timeDisplay

    private val _restProgress = MutableLiveData(0f)
    val restProgress: LiveData<Float>
        get() = _restProgress

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
        withContext(Dispatchers.Main) {
            _currentSession.value = session
            _isResting.value = false
            _countDownTimer?.cancel()
            _restProgress.value = 0f
            _timeDisplay.value = EMPTY_TIME_STRING
            _currentExercise.value = session.exercises[session.currentExerciseIndex]
            _currentSerieIndex.value = 0
        }
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
            it.series.repetitions[it.currentRepetitionIndex] = Repetition(done)
        }
    }

    fun moveToNext(onNextSerie: () -> Unit, onNextExercise: () -> Unit, onSessionEnd: () -> Unit) {
        _currentExercise.value?.let {
            _currentSerieIndex.value = _currentSerieIndex.value!! + 1
            when {
                _currentSerieIndex.value!! < it.series.count -> {
                    onNextSerie()
                }
                !isLastExercise() -> {
                    moveToNextExercise(it)
                    onNextExercise()
                }
                else -> onSessionEnd()
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    fun startRest(callback: (() -> Unit)? = null) {
        if (_isResting.value == true) {
            return
        }

        _countDownTimer?.let {
            it.cancel()
            _countDownTimer = null
        }

        val restDuration = getRestTimeMs()

        _isResting.value = true
        _countDownTimer =
            _countDownFactory.createCountDownTimer(restDuration, { millisUntilFinished ->
                _restProgress.value = 1f - millisUntilFinished.toFloat() / restDuration.toFloat()
                val duration = Duration.milliseconds(millisUntilFinished)
                duration.toComponents { minutes, seconds, _ ->
                    _timeDisplay.value = String.format("%02d:%02d", minutes, seconds)
                }
            }, {
                _restProgress.value = 1.0f
                _isResting.value = false
                _timeDisplay.value = EMPTY_TIME_STRING
                callback?.invoke()
            })

        _countDownTimer?.start()
    }

    private fun getRestTimeMs(): Int {
        return (_previousExercise?.let {
            (if (_currentSerieIndex.value == 0) it.restAfter else _currentExercise.value!!.series.restAfter)?.duration
        } ?: _currentExercise.value?.series?.restAfter?.duration ?: 0) * 1000
    }

    private fun isLastExercise() =
        _currentExercise.value == _currentSession.value?.exercises?.last()

    private fun moveToNextExercise(it: Exercise) {
        _previousExercise = _currentExercise.value
        _currentSerieIndex.value = 0
        _currentExercise.value = workoutService.moveToNextExercise()
    }
}

class CountdownTimerFactory {
    fun createCountDownTimer(
        restDurationMs: Number,
        onTick: (millisUntilFinished: Long) -> Unit,
        onFinish: () -> Unit
    ): CountDownTimer {
        return object : CountDownTimer(restDurationMs.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                onTick(millisUntilFinished)
            }

            override fun onFinish() {
                onFinish()
            }
        }
    }
}