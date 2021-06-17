package com.br2.cadela.workout.views

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun WorkoutStatsView(workoutVm: WorkoutViewModel?, navController: NavHostController?) {
    PageWithLogo {
        Box(modifier = it) {
            Text(text = "Coming soon")
        }
    }
}