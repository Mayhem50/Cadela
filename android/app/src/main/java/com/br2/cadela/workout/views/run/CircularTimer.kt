package com.br2.cadela.workout.views.run

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.br2.cadela.workout.views.WorkoutViewModel

@Composable
fun CircularTimer(viewModel: WorkoutViewModel?, modifier: Modifier) {
    val progress = viewModel?.restProgress?.observeAsState(initial = 0f)
    val timeString = viewModel?.timeDisplay?.observeAsState()

    Box(modifier = modifier.wrapContentSize(), contentAlignment = Alignment.Center) {
        Text(
            text = timeString?.value ?: "--:--",
            modifier = Modifier.fillMaxWidth(0.65f),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                letterSpacing = 1.2f.sp
            )
        )
        CircularProgressIndicator(
            progress = 1.0f,
            strokeWidth = 6.dp,
            color = Color.LightGray,
            modifier = Modifier
                .fillMaxWidth(.65f)
                .height(240.dp)
        )
        CircularProgressIndicator(
            progress = progress?.value ?: .5f, strokeWidth = 6.dp,
            modifier = Modifier
                .fillMaxWidth(.65f)
                .height(240.dp)
        )
    }
}