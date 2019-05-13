package com.whitecards.cadela.viewModel

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
        AuthService.loginFacebookAsync(activity)

        AuthService.user.observe(activity as LifecycleOwner, object : Observer<FirebaseUser?> {
            override fun onChanged(user: FirebaseUser?) {
                user?.let {
                    userName.value = it.displayName
                    startMainActivityAsync(activity)
                }

                isLoading.postValue(View.GONE)
            }
        })
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        AuthService.onActivityResult(requestCode, resultCode, data)
    }

    fun checkIsLogged(activity: Activity) {
        FirebaseAuth.getInstance().currentUser?.let {
            startMainActivityAsync(activity)
        }
    }

    private fun startMainActivityAsync(activity: Activity) {
        val intent = Intent(activity.application, MainActivity::class.java)
        intent.flags = intent.flags or Intent.FLAG_ACTIVITY_NO_HISTORY
        activity.startActivity(intent)
    }
}