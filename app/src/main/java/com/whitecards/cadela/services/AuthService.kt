package com.whitecards.cadela.services

import com.facebook.CallbackManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.concurrent.CyclicBarrier

object AuthService {
    lateinit var callbackManager: CallbackManager
    lateinit var waiter: CyclicBarrier
    var user: FirebaseUser? = null
    var token: String? = null

    fun init() {
        initFacebook()
        user = FirebaseAuth.getInstance().currentUser
        user?.let {
            token = it.uid
        }
    }

}