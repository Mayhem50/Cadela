package com.whitecards.cadela.ui.main.tabs.home

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whitecards.cadela.ui.workout.WorkoutActivity
import com.whitecards.cadela.data.model.Session
import com.whitecards.cadela.services.FirebaseService
import kotlinx.coroutines.*
import java.time.format.DateTimeFormatter

class HomeViewModel : ViewModel() {
    private var _isLoading = MutableLiveData<Boolean>()
    private var _actualLevel = MutableLiveData<Int>()
    private var _programStartingDate = MutableLiveData<String>()

    private var _session = MutableLiveData<Session>()

    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val session: LiveData<Session>
        get() = _session

    val actualLevel: LiveData<Int>
        get() = _actualLevel

    val programStartingDate: LiveData<String>
        get() = _programStartingDate

    init {
        _isLoading.value = true
        GlobalScope.launch {
            val result = FirebaseService.initAsync().await()
            update(result)
        }
    }

    private fun update(success: Boolean) {
        if (success) {
            _isLoading.postValue(false)
            val lastSession = FirebaseService.sessions.lastOrNull()
            val session = Session.createFromPrevious(lastSession)
            _session.postValue(session)

            val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
            _programStartingDate.postValue(session.dateOfProgramBegining.format(formatter))

            _actualLevel.postValue(session.program.level)
        } else {
            _isLoading.postValue(false)
        }
    }

    fun startWorkout(view: View)
    {
        val activity = view.context as Activity
        val intent  = Intent(activity, WorkoutActivity::class.java)
        activity.startActivity(intent)
    }
}