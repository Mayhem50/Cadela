package com.br2.cadela.workout.datas

data class Series(
    val count: Int,
    val repetitions: MutableList<Repetition> = MutableList(count) { Repetition(0) },
    val target: Int = 0,
    val restAfter: Rest = Rest(120)
) {
    companion object {
        val ESTIMATED_TIME_FOR_ONE_SERIE_IN_SEC = 30
    }
}

val Series.meanRepsDone: Int
    get() = repetitions.sumOf { it.done } / repetitions.size

fun Series.withRestAfterOf(duration: Int) =
    Series(count = count, repetitions = repetitions, target = target, restAfter = Rest(duration))

fun Series.newTarget(newTarget: Int) =
    Series(count = count, repetitions = repetitions, target = newTarget, restAfter = restAfter)
