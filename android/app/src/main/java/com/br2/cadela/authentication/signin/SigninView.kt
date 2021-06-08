package com.br2.cadela.authentication.signin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.br2.cadela.R
import com.br2.cadela.authentication.AuthenticationModule

@Composable
fun SigninView() {
    val vm = AuthenticationModule.signinVm
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loading by vm.loading.observeAsState(false)

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.0f.dp)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        OutlinedTextField(
            label = { Text(stringResource(id = R.string.email)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.padding(8.0f.dp)
        )
        OutlinedTextField(
            label = { Text(stringResource(id = R.string.password)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation(),
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.padding(8.0f.dp),
        )
        Box(modifier = Modifier.height(124f.dp)) {
            if(!loading) Button(
                onClick = { vm.signin(email, password) },
                modifier = Modifier.padding(8.0f.dp),
            ) {
                Text(text = stringResource(id = R.string.signin).capitalize(Locale.current))
            }
            else CircularProgressIndicator(
                modifier = Modifier.padding(8.0f.dp)
            )
        }
    }
}
