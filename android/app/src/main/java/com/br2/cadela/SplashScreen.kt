package com.br2.cadela

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.br2.cadela.shared.buildPopupToCurrent
import com.br2.cadela.shared.navigateStringResource
import com.br2.cadela.ui.theme.Red200
import com.br2.cadela.ui.theme.Red500
import kotlinx.coroutines.*

@Composable
fun SplashScreen(navController: NavController) {
    val infinitelyAnimatedFloat = animatedLoop(800, 0.65f, 1f)
    Box(
        modifier = Modifier.background(
            brush = Brush.linearGradient(
                listOf(
                    Red500,
                    Red200
                ), start = Offset.Zero, end = Offset(1f, 1f)
            )
        )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_without_gradient),
                contentDescription = null,
                Modifier
                    .width(120f.dp)
                    .height(120f.dp),
                alignment = Alignment.Center,
                alpha = infinitelyAnimatedFloat.value
            )
            Text(
                text = stringResource(id = R.string.app_name).toUpperCase(Locale.current),
                fontSize = 28f.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(10f.dp)
            )
        }
    }

    waitBeforeNavigate(1000, navController)
}

fun waitBeforeNavigate(timeMs: Long, navController: NavController) =
    CoroutineScope(Dispatchers.IO).launch {
        delay(timeMs)
        withContext(Dispatchers.Main) {
            navController.navigateStringResource(
                R.string.nav_workout_home,
                NavOptions.Builder().buildPopupToCurrent(navController)
            )
        }
    }