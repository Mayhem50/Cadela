package com.br2.cadela.workout.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import com.br2.cadela.R
import com.br2.cadela.ui.theme.Red200

@Composable
inline fun PageWithLogo(crossinline content: @Composable (modifier: Modifier) -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val (logo, content) = createRefs()
        val centerHorizontalGuideline = createGuidelineFromTop(.5f)
        val centerVerticalGuideline = createGuidelineFromStart(.5f)

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            Modifier
                .width(60.dp)
                .height(60.dp)
                .constrainAs(ref = logo) {
                    top.linkTo(parent.top, 32.dp)
                    centerAround(centerVerticalGuideline)
                },
            alignment = Alignment.TopCenter
        )
        val modifier = Modifier.constrainAs(ref = content){
            centerAround(centerHorizontalGuideline)
            centerAround(centerVerticalGuideline)
        }

        content(modifier)
    }
}
