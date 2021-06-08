package com.br2.cadela.workout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.navigation.NavController
import com.br2.cadela.shared.FakeNavControllerPreviewParameter
import com.br2.cadela.ui.theme.CadelaTheme

@Composable
fun WorkoutHomeView(navController: NavController) {
    val session = WorkoutModule.workoutVm.currentSession.observeAsState()

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        session.value?.let {
            
        } ?: CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
fun previewHome(@PreviewParameter(FakeNavControllerPreviewParameter::class) navController: NavController) {
    CadelaTheme {
        WorkoutHomeView(navController)
    }
}