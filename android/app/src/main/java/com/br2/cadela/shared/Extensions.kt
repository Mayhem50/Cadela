package com.br2.cadela.shared

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.br2.cadela.R
import java.time.Duration

fun NavController.navigateStringResource(@StringRes resId: Int, navOptions: NavOptions? = null) {
    navigate(context.getString(resId), navOptions)
}

fun NavOptions.Builder.buildPopupToCurrent(navController: NavController) =
    setPopUpTo(navController.currentDestination!!.id, true)
        .build()

fun Modifier.fillAllAndSmallPadding() = padding(8.0f.dp)
    .fillMaxWidth()
    .fillMaxHeight()

fun Duration.toFormattedString(): String {
    return toString().substring(2)
        .replace(Regex("(\\d[HMS])(?!$)"), "$1 ")
        .replace(Regex("(\\d)M"), "$1MIN")
        .toLowerCase(Locale.current)
}

@Composable
@ReadOnlyComposable
fun stringResourceByName(
    name: String
): String {
    val id = LocalContext.current.resources.getIdentifier(
        name,
        "string",
        stringResource(id = R.string.package_name)
    )
    return stringResource(id = id)
}
