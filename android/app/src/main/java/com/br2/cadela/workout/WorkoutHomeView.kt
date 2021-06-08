package com.br2.cadela.workout

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.navigation.NavController
import com.br2.cadela.shared.FakeNavControllerPreviewParameter
import com.br2.cadela.ui.theme.CadelaTheme

@Composable
fun WorkoutView(navController: NavController) {
    Column {
        Text(text = "session.name")
        Text(text = "session.startedAt")
    }
}

@Preview(showBackground = true)
@Composable
fun preview(@PreviewParameter(FakeNavControllerPreviewParameter::class) navController: NavController) {
    CadelaTheme {
        WorkoutView(navController)
    }
}