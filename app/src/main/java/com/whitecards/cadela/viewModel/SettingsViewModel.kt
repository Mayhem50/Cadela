package com.whitecards.cadela.viewModel

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.whitecards.cadela.data.Settings
import com.whitecards.cadela.ui.login.LoginActivity

class SettingsViewModel : ViewModel(){
    var actualLevel: MutableLiveData<Int> = MutableLiveData<Int>()
    var alarmTime: MutableLiveData<Int> = MutableLiveData<Int>()
    var applyChangeButtonVisibility: MutableLiveData<Int> = MutableLiveData<Int>()

    init{
        actualLevel.value = Settings.actualLevel
        alarmTime.value = Settings.alarmTime
        applyChangeButtonVisibility.value = View.GONE
    }

    private fun updateApplyButtonVisibility(){
        if(actualLevel.value != Settings.actualLevel || alarmTime.value != Settings.alarmTime){
            applyChangeButtonVisibility.postValue(View.VISIBLE)
        }
        else{
            applyChangeButtonVisibility.postValue(View.GONE)
        }
    }

    fun onMoreLevelClick(view: View){
        actualLevel.value = (actualLevel.value!!.plus(1))
        updateApplyButtonVisibility()
    }

    fun onLessLevelClick(view: View){
        actualLevel.value = (actualLevel.value!!.minus(1))
        updateApplyButtonVisibility()
    }

    fun onMoreAlarmRepetitionClick(view: View){
        alarmTime.value = (alarmTime.value!!.plus(1))
        updateApplyButtonVisibility()
    }

    fun onLessAlarmRepetitionClick(view: View){
        alarmTime.value = (alarmTime.value!!.minus(1))
        updateApplyButtonVisibility()
    }

    fun onLogout(view: View){
        FirebaseAuth.getInstance().signOut()

        val activity = view.context as Activity
        var intent = Intent(activity, LoginActivity::class.java)
        intent.flags = intent.flags or Intent.FLAG_ACTIVITY_NO_HISTORY
        activity.startActivity(intent)
    }

    fun onApplyChange(view: View){
        applyChangeButtonVisibility.postValue(View.GONE)
        Settings.actualLevel = actualLevel.value!!
        Settings.alarmTime = alarmTime.value!!
    }
}