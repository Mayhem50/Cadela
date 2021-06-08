package com.br2.cadela.workout

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.br2.cadela.ui.theme.CadelaTheme

@Composable
fun WorkoutView() {
    Column {
        Text(text = "session.name")
        Text(text = "session.startedAt")
    }
}

@Preview(showBackground = true)
@Composable
fun preview() {
    CadelaTheme {
        WorkoutView()
    }
}