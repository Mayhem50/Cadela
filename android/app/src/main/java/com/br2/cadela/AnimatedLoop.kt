package com.br2.cadela

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

@Composable
fun animatedLoop(
    durationMillis: Int, initialValue: Float, targetValue: Float, easing: Easing = LinearEasing
): State<Float> {
    val infiniteTransition = rememberInfiniteTransition()
    return infiniteTransition.animateFloat(
        initialValue = initialValue,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = easing),
            repeatMode = RepeatMode.Reverse
        )
    )
}