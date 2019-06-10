package com.whitecards.cadela.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.concurrent.schedule

class WorkoutViewModel : ViewModel() {
    private val _timer = Timer(false)

    private val _percentageTimeLeft = MutableLiveData<Float>()

    val percentageTimeLeft: LiveData<Float>
        get() {
            return _percentageTimeLeft
        }

    init {


    }

    fun startExercice() {

    }

    fun startTimer() {
        _timer.schedule(100) {
            
        }
    }
}