package com.br2.cadela.authentication.signin

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.br2.cadela.R
import com.br2.cadela.authentication.AuthenticationModule
import com.br2.cadela.shared.buildPopupToCurrent
import com.br2.cadela.shared.fillAllAndSmallPadding
import com.br2.cadela.shared.navigateStringResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@Composable
fun SigninView(navController: NavController) {
    val vm = AuthenticationModule.signinVm
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loading by vm.loading.observeAsState(false)
    val error by vm.error.observeAsState("")
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val passwordFocusRequester = FocusRequester()


    Scaffold(scaffoldState = scaffoldState) {
        fun signin() {
            keyboardController?.hide()
            vm.signin(email, password) {
                navController.navigateStringResource(
                    R.string.nav_workout_home,
                    NavOptions.Builder().buildPopupToCurrent(navController)
                )
            }
        }

        ShowSnackBarIfNeeded(error, scope, scaffoldState) { vm.clearError() }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillAllAndSmallPadding()
        ) {
            OutlinedTextField(
                keyboardActions = KeyboardActions(onNext = { passwordFocusRequester.requestFocus() }),
                label = { Text(stringResource(id = R.string.email)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.padding(8.0f.dp)
            )
            OutlinedTextField(
                keyboardActions = KeyboardActions(onDone = { signin() }),
                label = { Text(stringResource(id = R.string.password)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = PasswordVisualTransformation(),
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .padding(8.0f.dp)
                    .focusRequester(passwordFocusRequester),
            )
            ButtonWithLoader(loading) {
                signin()
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
private fun ShowSnackBarIfNeeded(
    error: String,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    action: () -> Unit
) {
    if (error.isNotEmpty()) {
        scope.launch {
            scaffoldState.snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short,
                actionLabel = null,
            )
            action()
        }
    }
}

@Composable
private fun ButtonWithLoader(loading: Boolean, action: () -> Unit) {
    Box(modifier = Modifier.height(124f.dp)) {
        if (!loading) Button(
            onClick = {
                action()
            },
            modifier = Modifier.padding(8.0f.dp),
        ) {
            Text(text = stringResource(id = R.string.signin).capitalize(Locale.current))
        }
        else CircularProgressIndicator(
            modifier = Modifier.padding(8.0f.dp)
        )
    }
}
