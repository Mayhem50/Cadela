package com.whitecards.cadela.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.whitecards.cadela.services.FirebaseService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class HomeViewModel : ViewModel(){
    var isLoading = MutableLiveData<Boolean>()

    init {
        isLoading.value = true
        init()
    }

    private fun init() = GlobalScope.async{
        val result = FirebaseService.initAsync().await()
        isLoading.postValue(result)
    }
}