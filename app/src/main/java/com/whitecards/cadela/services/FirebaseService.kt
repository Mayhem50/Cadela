package com.whitecards.cadela.services

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.whitecards.cadela.data.model.Exercise
import com.whitecards.cadela.data.model.Program
import com.whitecards.cadela.data.model.Session
import kotlinx.coroutines.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.CyclicBarrier

object FirebaseService {
    var exercises = ArrayList<Exercise>()
    var programs = ArrayList<Program>()
    var sessions = ArrayList<Session>()

    var isInit: Boolean = false

    private val _database = FirebaseDatabase.getInstance().getReference("")

    init {
        isInit = false
    }

    fun init(callback: (success: Boolean) -> Unit) {
        if(isInit) return

        _database.child("exercises").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children) {
                    exercises.add(child.getValue(Exercise::class.java)!!)
                }

                _database.child("programs").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (child in snapshot.children) {
                            programs.add(child.getValue(Program::class.java)!!)
                        }

                        populateProgramWithRealExercises()

                        _database.child("sessions").child(AuthService.token.value!!)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (child in snapshot.children) {
                                        sessions.add(child.getValue(Session::class.java)!!)
                                    }

                                    isInit = true
                                    callback.invoke(isInit)
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    callback.invoke(isInit)
                                }
                            })
                    }

                    override fun onCancelled(error: DatabaseError) {
                        callback.invoke(isInit)
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                callback.invoke(isInit)
            }
        })
    }

    private fun populateProgramWithRealExercises() {
        for (program in programs) {
            for (i in program.exercises.indices) {
                val exercise = program.exercises[i]
                val real = exercises.find { e -> e.name == exercise.name }!!
                program.exercises[i] = real
            }
        }
    }
}