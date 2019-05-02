package com.whitecards.cadela.viewModel

import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.whitecards.cadela.ui.main.MainActivity
import com.whitecards.cadela.services.AuthService
import com.whitecards.cadela.services.loginFacebookAsync
import com.whitecards.cadela.services.onActivityResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LoginViewModel : ViewModel(){

    val isLoading: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val userName: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    init{
        isLoading.value = View.GONE
        AuthService.init()
    }

    fun onFacebookClick(view: View) {
        val activity = view.context as Activity
        loginFacebookAsync(activity)
    }

    private fun loginFacebookAsync(activity: Activity) = GlobalScope.launch {
        isLoading.postValue(View.VISIBLE)
        val success = AuthService.loginFacebookAsync(activity).await()

        isLoading.postValue(View.GONE)

        if(success){
            userName.postValue(AuthService.user!!.displayName)
            startMainActivityAsync(activity)
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        AuthService.onActivityResult(requestCode, resultCode, data)
    }

    fun checkIsLogged(activity: Activity){
        FirebaseAuth.getInstance().currentUser?.let {
            startMainActivityAsync(activity)
        }
    }

    private fun startMainActivityAsync(activity: Activity){
        val intent = Intent(activity.application, MainActivity::class.java)
        intent.flags = intent.flags or Intent.FLAG_ACTIVITY_NO_HISTORY
        activity.startActivity(intent)
    }
}