package com.br2.cadela.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.layout.ScaleFactor
import androidx.compose.ui.layout.lerp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerScope
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import kotlin.math.absoluteValue

@ExperimentalPagerApi
fun PagerScope.pagerSwipeAnimation(
    page: Int,
    graphicsLayerScope: GraphicsLayerScope
) {
    val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

    // We animate the scaleX + scaleY, between 85% and 100%
    lerp(
        start = ScaleFactor(0.85f, 0.85f),
        stop = ScaleFactor(1f, 1f),
        fraction = 1f - pageOffset.coerceIn(0f, 1f)
    ).also { scale ->
        graphicsLayerScope.scaleX = scale.scaleX
        graphicsLayerScope.scaleY = scale.scaleY
    }

    // We animate the alpha, between 50% and 100%
    graphicsLayerScope.alpha = lerp(
        start = ScaleFactor(0.5f, 0.5f),
        stop = ScaleFactor(1f, 1f),
        fraction = 1f - pageOffset.coerceIn(0f, 1f)
    ).scaleX
}