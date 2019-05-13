package com.whitecards.cadela.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whitecards.cadela.data.model.Session
import com.whitecards.cadela.services.FirebaseService
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

        FirebaseService.init {
            if (it) {
                _isLoading.value = false
                val lastSession = FirebaseService.sessions.lastOrNull()
                val session = Session.createFromPrevious(lastSession)
                _session.value = session

                val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
                _programStartingDate.value = session.dateOfProgramBegining.format(formatter)

                _actualLevel.value = session.program.level
            } else {
                _isLoading.value = false
            }
        }
    }
}