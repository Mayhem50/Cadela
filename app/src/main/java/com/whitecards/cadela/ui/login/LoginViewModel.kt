package com.whitecards.cadela.ui.login

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.whitecards.cadela.services.AuthService
import com.whitecards.cadela.services.loginFacebookAsync
import com.whitecards.cadela.services.onActivityResult
import com.whitecards.cadela.ui.main.MainActivity


class LoginViewModel : ViewModel() {

    val isLoading: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val userName: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    init {
        isLoading.value = View.GONE
        AuthService.init()

    }

    fun onFacebookClick(view: View) {
        val activity = view.context as Activity
        loginFacebookAsync(activity)
    }

    private fun loginFacebookAsync(activity: Activity) {
        isLoading.postValue(View.VISIBLE)
        with(AuthService) {
            loginFacebookAsync(activity)

            user.observe(activity as LifecycleOwner,
                Observer { user ->
                    user?.let {
                        userName.value = it.displayName
                        startMainActivity(activity)
                    }

                    isLoading.postValue(View.GONE)
                })
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        AuthService.onActivityResult(requestCode, resultCode, data)
    }

    fun checkIsLogged(activity: Activity) {
        FirebaseAuth.getInstance().currentUser?.let {
            startMainActivity(activity)
        }
    }

    private fun startMainActivity(activity: Activity) {
        val intent = Intent(activity.application, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intent)
    }
}