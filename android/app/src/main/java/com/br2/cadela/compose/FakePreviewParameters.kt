package com.br2.cadela.shared

import android.content.Context
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.navigation.NavController


class FakeNavControllerPreviewParameter : PreviewParameterProvider<NavController> {
    override val values: Sequence<NavController>
        get() = sequenceOf(NavController(null as Context))
}