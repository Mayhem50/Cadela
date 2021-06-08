package com.br2.cadela.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.br2.cadela.R
import com.br2.cadela.authentication.signin.SigninView
import com.br2.cadela.ui.theme.CadelaTheme


@Composable
fun AuthView() {
    Box(
        modifier = Modifier.padding(8.0f.dp).fillMaxHeight().fillMaxWidth().padding(top = 32f.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            Modifier
                .width(80f.dp)
                .height(80f.dp),
            alignment = Alignment.TopCenter
        )

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