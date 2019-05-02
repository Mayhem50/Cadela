package com.whitecards.cadela.services

import android.arch.lifecycle.MutableLiveData
import com.facebook.CallbackManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.concurrent.CyclicBarrier

object AuthService {
    lateinit var callbackManager: CallbackManager
    var user: MutableLiveData<FirebaseUser?> = MutableLiveData()
    var token: MutableLiveData<String?> = MutableLiveData()

    fun init() {
        initFacebook()
        user.value = FirebaseAuth.getInstance().currentUser
        user.value?.let {
            token.value = it.uid
        }
    }

}