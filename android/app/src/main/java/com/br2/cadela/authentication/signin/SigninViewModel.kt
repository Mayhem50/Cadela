package com.br2.cadela.authentication.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SigninViewModel(private val signinService: SigninService): ViewModel() {
    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    fun signin(email: String, password: String) = viewModelScope.launch(Dispatchers.Main) {
        _loading.value = true
        withContext(Dispatchers.IO) { signinService.signin(email, password) }
        _loading.value = false
    }
}
