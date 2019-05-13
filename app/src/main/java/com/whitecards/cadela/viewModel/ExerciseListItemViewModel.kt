package com.whitecards.cadela.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.whitecards.cadela.data.model.Program

class ExerciseListItemViewModel(private val program: Program, private val exerciseIndex: Int) : ViewModel() {
    private var _exerciseName = MutableLiveData<String>()
    private var _targetRepetition = MutableLiveData<Int>()

    val name: LiveData<String>
        get() = _exerciseName

    val targetRepetition: LiveData<Int>
        get() = _targetRepetition

    init {
        _exerciseName.value = program.exercises[exerciseIndex].name
        _targetRepetition.value = program.targetRepetitionAtStart[exerciseIndex]
    }
}