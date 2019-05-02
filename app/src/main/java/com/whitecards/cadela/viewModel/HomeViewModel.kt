package com.whitecards.cadela.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.view.View
import com.whitecards.cadela.services.FirebaseService

class HomeViewModel : ViewModel(){
    val isLoading: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    init {
        isLoading.value = View.VISIBLE

        FirebaseService.init {
            if(it){
                isLoading.value = View.GONE
            }
            else{
                isLoading.value = View.VISIBLE
            }
        }
    }
}