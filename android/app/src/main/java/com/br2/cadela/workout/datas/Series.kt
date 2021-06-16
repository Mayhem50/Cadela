package com.br2.cadela.workout.datas

data class Series(
    val count: Int,
    val repetitions: MutableList<Repetition> = MutableList(count) { Repetition(0) },
    val restAfter: Rest = Rest(120)
) {
    companion object {
        val ESTIMATED_TIME_FOR_ONE_SERIE_IN_SEC = 30
    }
}

fun Series.withRestAfterOf(duration: Int) =
    Series(count = count, repetitions = repetitions, restAfter = Rest(duration))
