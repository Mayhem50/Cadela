package com.br2.cadela.workout

data class Series(val count: Int, val repetitions: MutableList<Int> = MutableList(count) { 0 })
