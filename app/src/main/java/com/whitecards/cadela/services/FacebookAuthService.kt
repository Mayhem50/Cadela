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
                waiter.await()
            }
            override fun onError(error: FacebookException){
                Log.d("ERROR", error.toString())
                waiter.await()
            }
        }
    )
}

private fun AuthService.handleResult(accessToken: AccessToken?) {
    accessToken?.let {
        val credential = FacebookAuthProvider.getCredential(accessToken.token)
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnSuccessListener {
            user = it.user
            token = it.user.uid
            waiter.await()
        }
        return
    }
    waiter.await()
}

fun AuthService.onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
    callbackManager.onActivityResult(requestCode, resultCode, data)
}

fun AuthService.loginFacebookAsync(activity: Activity) : Deferred<Boolean> = GlobalScope.async {
    user = null
    waiter = CyclicBarrier(2)

    withContext(Dispatchers.Default){ LoginManager.getInstance().logInWithReadPermissions(activity, listOf("email", "public_profile")) }
    withContext(Dispatchers.Default){waiter.await()}

    user != null
}