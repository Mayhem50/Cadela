package com.br2.cadela.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.br2.cadela.R
import com.br2.cadela.authentication.signin.SigninView
import com.br2.cadela.shared.FakeNavControllerPreviewParameter
import com.br2.cadela.shared.fillAllAndSmallPadding
import com.br2.cadela.ui.theme.CadelaTheme
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding


@ExperimentalComposeUiApi
@Composable
fun AuthView(navController: NavController) {
    Box(
        modifier = Modifier
            .padding(8.0f.dp)
            .fillMaxHeight()
            .fillMaxWidth()
            .navigationBarsPadding()
            .statusBarsPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        SigninView(navController)
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            Modifier
                .width(180f.dp)
                .height(180f.dp)
                .padding(top = 60f.dp),
            alignment = Alignment.TopCenter
        )
    }
}

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview(@PreviewParameter(FakeNavControllerPreviewParameter::class) navController: NavController) {
    CadelaTheme {
        AuthView(navController)
    }
}