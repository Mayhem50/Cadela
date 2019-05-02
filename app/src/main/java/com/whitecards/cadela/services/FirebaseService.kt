package com.whitecards.cadela.services

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.whitecards.cadela.data.model.Exercise
import com.whitecards.cadela.data.model.Program
import com.whitecards.cadela.data.model.Session
import kotlinx.coroutines.*
import java.util.concurrent.CountDownLatch

object FirebaseService {
    var exercises = ArrayList<Exercise>()
    var programs = ArrayList<Program>()
    var sessions = ArrayList<Session>()

    var errorFound = false

    private var _exerciceWaiter = CountDownLatch(1)
    private var _programWaiter = CountDownLatch(1)
    private var _sessionWaiter = CountDownLatch(1)

    private val _database = FirebaseDatabase.getInstance().getReference("")

    var isLaunch = false

    fun initAsync(): Deferred<Boolean> = GlobalScope.async {

        if(!isLaunch) {
            isLaunch = true
            exercises.clear()
            programs.clear()
            sessions.clear()

            withContext(Dispatchers.Default) {
                _database.child("exercises").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        exercises.clear()
                        for (child in snapshot.children) {
                            exercises.add(child.getValue(Exercise::class.java)!!)
                        }
                        _exerciceWaiter.countDown()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        exercises.clear()
                        errorFound = true
                        _exerciceWaiter.countDown()
                    }
                })

                _database.child("programs").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        _exerciceWaiter.await()

                        programs.clear()
                        if (!errorFound && exercises.size != 0) {
                            for (child in snapshot.children) {
                                programs.add(child.getValue(Program::class.java)!!)
                            }

                            populateProgramWithRealExercises()
                        }
                        _programWaiter.countDown()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        programs.clear()
                        errorFound = true
                        _programWaiter.countDown()
                    }
                })

                _database.child("sessions").child(AuthService.token!!)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            _programWaiter.await()

                            if (!errorFound && programs.size != 0) {
                                for (child in snapshot.children) {
                                    sessions.add(child.getValue(Session::class.java)!!)
                                }
                            }
                            _sessionWaiter.countDown()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            errorFound = true
                            _sessionWaiter.countDown()
                        }
                    })
            }


            withContext(Dispatchers.Default) { _sessionWaiter.await() }
        }

            !errorFound && programs.size != 0 && exercises.size != 0
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