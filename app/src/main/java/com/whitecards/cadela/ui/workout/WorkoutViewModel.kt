package com.whitecards.cadela.ui.workout

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.load.engine.Resource
import com.whitecards.cadela.R
import com.whitecards.cadela.data.model.Exercise
import com.whitecards.cadela.data.model.Session
import com.whitecards.cadela.services.FirebaseService
import java.util.*

class WorkoutViewModel : ViewModel() {
    private var _currentExercise: Exercise
    private var _currentSession: Session = Session.createFromPrevious(FirebaseService.sessions.lastOrNull())
    private var _currentExerciseIndex = 0
    private var _restInTime = 0

    private val _timeLeft = MutableLiveData<String>()
    private val _percentageTimeLeft = MutableLiveData<Int>()
    private val _buttonText = MutableLiveData<String>()
    private val _isLastExercise = MutableLiveData<Boolean>()
    private val _buttonEnable = MutableLiveData<Boolean>()

    private val _exercises: ArrayList<Exercise>
        get() {return _currentSession.program.exercises }

    private val _restInTimes: ArrayList<Int>
        get() {return _currentSession.program.restInTimes }

    private val _restBetweenTimes: ArrayList<Int>
        get() {return _currentSession.program.restBetweenTimes }

    val percentageTimeLeft: LiveData<Int>
        get() { return _percentageTimeLeft }

    val timeLeft: LiveData<String>
        get() { return _timeLeft }

    val buttonText: LiveData<String>
        get() { return _buttonText }

    val isLastExercise: LiveData<Boolean>
        get() { return _isLastExercise }

    val buttonEnable: LiveData<Boolean>
        get() { return _buttonEnable }

    init {
        _currentExercise = _exercises[_currentExerciseIndex]
        _percentageTimeLeft.value = 100
        _timeLeft.value = "25s"
        _isLastExercise.value = false
    }

    fun onButtonClick(view: View) {
        //saveSession()

        _currentExercise = _exercises[_currentExerciseIndex]
        _restInTime = _restInTimes[_currentExerciseIndex]

        var animator = ValueAnimator.ofInt(0, 10000)
        animator.duration = _restInTime.toLong() * 1000
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener {
            val animatedValue = it.animatedValue as Int
            _percentageTimeLeft.postValue(10000 - animatedValue)
            val secondPassed = _restInTime * (1f - animatedValue / 10000f)
            val timeStr = "${secondPassed.toInt()}s"
            _timeLeft.postValue(timeStr)
        }

        animator.addListener(object: Animator.AnimatorListener{
            override fun onAnimationStart(animation: Animator?) {
                _buttonEnable.postValue(false)
            }

            override fun onAnimationEnd(animation: Animator?) {
                _buttonEnable.postValue(true)
                _currentExerciseIndex++

                if(_currentExerciseIndex >= _exercises.size){
                    goBackHome(view.context as Activity)
                }

                if(_currentExerciseIndex == _exercises.size - 1){
                    _isLastExercise.postValue(true)
                }

                setNextExercise()
            }

            override fun onAnimationRepeat(animation: Animator?) {
                //TODO("not implemented")
            }

            override fun onAnimationCancel(animation: Animator?) {
                //TODO("not implemented")
            }
        })

        animator.start()
    }

    private fun saveSession() {
        //TODO("not implemented")
    }

    private fun setNextExercise(){

    }

    private fun goBackHome(activity: Activity){
        activity.finish()
    }
}