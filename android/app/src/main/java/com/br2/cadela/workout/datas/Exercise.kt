package com.br2.cadela.workout.datas

data class Exercise(val name: String, val series: Series, val restAfter: Rest?) {
    val speed: ESpeed = ESpeed.NORMAL
}