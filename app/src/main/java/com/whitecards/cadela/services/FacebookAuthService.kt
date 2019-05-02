package com.whitecards.cadela.services

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.facebook.*
import com.facebook.login.*
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.*
import java.util.concurrent.CyclicBarrier


fun AuthService.initFacebook(){
    callbackManager = CallbackManager.Factory.create()

    LoginManager.getInstance().registerCallback(
        callbackManager, object: FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult){
                handleResult(loginResult.accessToken)
            }

            override fun onCancel(){
                Log.d("DISMISS", "Cancel")
            }
            override fun onError(error: FacebookException){
                Log.d("ERROR", error.toString())
            }
        }
    )
}

private fun AuthService.handleResult(accessToken: AccessToken?) {
    accessToken?.let {
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnSuccessListener {
            user.value = it.user
            token.value = it.user.uid
        }
        return
    }
}

fun AuthService.onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
    callbackManager.onActivityResult(requestCode, resultCode, data)
}

fun AuthService.loginFacebookAsync(activity: Activity) {
    LoginManager.getInstance().logInWithReadPermissions(activity, listOf("email", "public_profile"))
}