package com.br2.cadela.workout

data class Series(val count: Int, val repetitions: MutableList<Int> = MutableList(count) { 0 }, val restAfter: Rest = Rest(120)){
    companion object {
        val ESTIMATED_TIME_FOR_ONE_SERIE_IN_SEC = 30
    }
}
