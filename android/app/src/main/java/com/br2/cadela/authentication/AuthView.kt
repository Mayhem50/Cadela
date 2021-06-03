package com.br2.cadela.authentication

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.br2.cadela.ui.theme.CadelaTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.br2.cadela.authentication.signin.SigninView


@Composable
fun AuthView() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(Dp(8.0f)).fillMaxHeight().fillMaxWidth()
    ) {
        SigninView()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CadelaTheme {
        AuthView()
    }
}