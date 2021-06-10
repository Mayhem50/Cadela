package com.br2.cadela.workout

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.br2.cadela.shared.FakeNavControllerPreviewParameter
import com.br2.cadela.ui.theme.CadelaTheme

@Composable
fun WorkoutMainView(mainNavController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "workout_home"){
            composable("workout_home") { WorkoutHomeView(navController) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview(@PreviewParameter(FakeNavControllerPreviewParameter::class) navController: NavController) {
    CadelaTheme {
        WorkoutMainView(navController)
    }
}