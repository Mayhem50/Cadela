package com.br2.cadela.workout

data class Series(val count: Int, val repetitions: List<Int> = List(count) { 0 })
