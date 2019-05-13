package com.whitecards.cadela.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.view.View
import com.whitecards.cadela.data.model.Exercise
import com.whitecards.cadela.data.model.Session
import com.whitecards.cadela.services.FirebaseService

class HomeViewModel : ViewModel(){
    var _isLoading = MutableLiveData<Int>()
    var _sessions = MutableLiveData<ArrayList<Session>>()

    val isLoading: LiveData<Int>
        get() = _isLoading

    val sessions: LiveData<ArrayList<Session>>
        get() = _sessions

    init {
        _isLoading.value = View.VISIBLE

        FirebaseService.init {
            if(it){
                _isLoading.value = View.GONE
                _sessions.value = FirebaseService.sessions
            }
            else{
                _isLoading.value = View.VISIBLE
            }
        }
    }
}