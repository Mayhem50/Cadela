package com.br2.cadela.shared

import androidx.annotation.StringRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions

fun NavController.navigateStringResource(@StringRes resId: Int, navOptions: NavOptions? = null) {
    navigate(context.getString(resId), navOptions)
}

fun NavOptions.Builder.buildPopupToCurrent(navController: NavController) =
    setPopUpTo(navController.currentDestination!!.id, true)
        .build()