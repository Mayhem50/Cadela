package com.br2.cadela.workout

import java.time.LocalDate

data class SessionResult(val name: String, val exercises: List<Exercise>, val levelStartedAt: LocalDate = LocalDate.now())