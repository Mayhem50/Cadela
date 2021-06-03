package com.br2.cadela.authentication.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.runtime.*
import com.br2.cadela.authentication.AuthenticationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SigninView() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(
            Dp(8.0f)
        )
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.padding(Dp(8.0f))
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.padding(Dp(8.0f))
        )
        Button(onClick = { callService(email, password) },
                modifier = Modifier.padding(Dp(8.0f))) {
            Text(text = "Signin")
        }
    }
}

fun callService(email: String, password: String) = CoroutineScope(Dispatchers.IO).launch {
    AuthenticationServices.SigninService.signin(email, password)
}
