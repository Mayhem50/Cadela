package com.whitecards.cadela.services

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.whitecards.cadela.data.model.Exercise
import com.whitecards.cadela.data.model.Program
import com.whitecards.cadela.data.model.Session
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object FirebaseService {
    var exercises = ArrayList<Exercise>()
    var programs = ArrayList<Program>()
    var sessions = ArrayList<Session>()

    var isInit: Boolean = false

    private val _database = FirebaseDatabase.getInstance().getReference("")

    init {
        isInit = false
    }

    fun initAsync(): Deferred<Boolean> = GlobalScope.async {

            if(readExercisesAsync())
                if(readProgramsAsync())
                    return@async readSessionsAsync()

        false
    }

    private suspend fun readExercisesAsync(): Boolean =
        suspendCoroutine {
            _database.child("exercises").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (child in snapshot.children) {
                        exercises.add(child.getValue(Exercise::class.java)!!)
                    }
                    it.resume(true)
                }

                override fun onCancelled(p0: DatabaseError) {
                    it.resume(false)
                }
            })
        }

    private suspend fun readProgramsAsync(): Boolean =
        suspendCoroutine {
            _database.child("programs").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (child in snapshot.children) {
                        programs.add(child.getValue(Program::class.java)!!)
                    }

                    populateProgramWithRealExercises()
                    it.resume(true)
                }

                override fun onCancelled(p0: DatabaseError) {
                    it.resume(false)
                }
            })
        }

    private suspend fun readSessionsAsync(): Boolean =
        suspendCoroutine {
            _database.child("sessions").child(AuthService.token.value!!)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (child in snapshot.children) {
                            sessions.add(child.getValue(Session::class.java)!!)
                        }

                        isInit = true
                        it.resume(true)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        it.resume(false)
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