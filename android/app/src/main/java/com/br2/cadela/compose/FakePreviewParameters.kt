package com.br2.cadela.shared

import android.content.Context
import android.content.res.AssetManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.navigation.NavController
import com.br2.cadela.workout.WorkoutViewModel


class FakeNavControllerPreviewParameter : PreviewParameterProvider<NavController> {
    override val values: Sequence<NavController>
        get() = sequenceOf(NavController(null as Context))
}