package com.br2.cadela.authentication.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.br2.cadela.R
import com.br2.cadela.shared.buildPopupToCurrent
import com.br2.cadela.shared.navigateStringResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SigninViewModel(private val signinService: SigninService) : ViewModel() {
    private val _error = MutableLiveData("")
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun signin(email: String, password: String, onSuccess: (() -> Unit)? = null) = viewModelScope.launch(Dispatchers.Main) {
       clearError()
        _loading.value = true
        try {
            withContext(Dispatchers.IO) { signinService.signin(email, password) }
            onSuccess?.invoke()
        } catch (ex: Exception){
            _error.value = ex.message
        }
        _loading.value = false
    }

    fun clearError() {
        _error.value = ""
    }
}
