package com.br2.cadela.workout.views.run

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.br2.cadela.R
import com.br2.cadela.compose.pagerSwipeAnimation
import com.br2.cadela.shared.buildPopupToCurrent
import com.br2.cadela.ui.theme.CadelaTheme
import com.br2.cadela.ui.theme.Red200
import com.br2.cadela.ui.theme.Red700
import com.br2.cadela.workout.datas.*
import com.br2.cadela.workout.views.WorkoutViewModel
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate

private val PREVIEW_SESSION = Session(
    name = Session.FIRST_LEVEL_TEST.name,
    exercises = listOf(
        Exercise(
            "A",
            Series(
                10, mutableListOf(
                    Repetition(19),
                    Repetition(18),
                    Repetition(17),
                    Repetition(16),
                    Repetition(17),
                    Repetition(14),
                    Repetition(15),
                    Repetition(16),
                    Repetition(13),
                    Repetition(0),
                    Repetition(0),
                    Repetition(0)
                ), Rest(30)
            ),
            Rest(120)
        ),
        Exercise("A1", Series(4), Rest(120)),
        Exercise("A2", Series(10), Rest(120)),
        Exercise("A3", Series(10), Rest(120)),
        Exercise("A4", Series(10), Rest(120)),
        Exercise("A5", Series(10), Rest(120)),
    ),
    levelStartedAt = LocalDate.of(2021, 6, 8)
)

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WorkoutRunView(viewModel: WorkoutViewModel?, navController: NavController?) {
    val scope = rememberCoroutineScope()
    val session by
    viewModel?.currentSession?.observeAsState() ?: remember { mutableStateOf(PREVIEW_SESSION) }
    val pagerState = rememberPagerState(
        pageCount = session?.exercises?.size ?: 0,
        initialPage = session?.currentExerciseIndex ?: 0
    )

    BackPressWarningDialog(viewModel, navController)

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(8.dp)
            .statusBarsPadding()
    ) {
        val (timer, pager, indicator) = createRefs()

        CircularTimer(viewModel = viewModel, modifier = Modifier.constrainAs(ref = timer) {
            top.linkTo(parent.top, 8.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            width = Dimension.fillToConstraints
        })
        HorizontalPager(
            state = pagerState,
            dragEnabled = false,
            modifier = Modifier
                .padding(8.dp)
                .constrainAs(ref = pager) {
                    top.linkTo(timer.bottom, 8.dp)
                    bottom.linkTo(indicator.top, 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }

        ) { page ->
            val exercise = session!!.exercises[page]
            val modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .graphicsLayer {
                    pagerSwipeAnimation(page, this)
                }
            RepetitionCard(viewModel, modifier, exercise) { repetitionDone ->
                viewModel?.setRepsForCurrentSerie(repetitionDone)
                moveToNext(viewModel, navController, scope, pagerState)
            }
        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            activeColor = Red200,
            inactiveColor = Red700,
            modifier = Modifier
                .height(24.dp)
                .constrainAs(ref = indicator) {
                    bottom.linkTo(parent.bottom, 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.wrapContent
                }
        )
    }

}

@Composable
private fun BackPressWarningDialog(viewModel: WorkoutViewModel?, navController: NavController?) {
    val currentSerieIndex by viewModel?.currentSerieIndex?.observeAsState(0) ?: remember {
        mutableStateOf(0)
    }
    var openDialog by remember { mutableStateOf(false) }

    BackHandler(onBack = { openDialog = true })

    if (!openDialog) return

    if (currentSerieIndex == 0)
        BackToHomeDialog({
            viewModel?.endSession()
            navController?.popBackStack()
        }, { openDialog = false })
    else NoStopAllowedDialog { openDialog = false }
}

@Composable
fun NoStopAllowedDialog(dismissRequest: () -> Unit) {
    AlertDialog(
        buttons = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Button(onClick = { dismissRequest() }) { Text(text = stringResource(R.string.ok)) }
            }
        },
        onDismissRequest = { dismissRequest() },
        title = { Text(text = stringResource(R.string.impossible)) },
        text = { Text(text = stringResource(R.string.can_not_stop_running_exercise)) }
    )
}

@Composable
private fun BackToHomeDialog(
    confirmRequest: () -> Unit,
    dismissRequest: () -> Unit
) {
    AlertDialog(
        confirmButton = {
            Button(onClick = { confirmRequest() }) { Text(text = stringResource(R.string.quit)) }
        },
        dismissButton = {
            Button(onClick = { dismissRequest() }) { Text(text = stringResource(R.string.dismiss)) }
        },
        onDismissRequest = { dismissRequest() },
        title = { Text(text = stringResource(R.string.are_you_sure)) },
        text = { Text(text = stringResource(R.string.you_are_about_to_quit)) })
}

@OptIn(ExperimentalPagerApi::class)
private fun moveToNext(
    viewModel: WorkoutViewModel?,
    navController: NavController?,
    scope: CoroutineScope,
    pagerState: PagerState
) {
    viewModel?.startRest {
        viewModel.moveToNext(
            onNextExercise = {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            },
            onSessionEnd = {
                viewModel.endSession()
                navController?.navigate(
                    "workout_home",
                    NavOptions.Builder().buildPopupToCurrent(navController)
                )
            }
        )
    }
}

@Preview(name = "Full Reps", showBackground = true)
@Composable
fun PreviewWorkoutRunCardFullReps() {
    CadelaTheme {
        WorkoutRunView(null, null)
    }
}

//@Preview(name = "Few Reps", showBackground = true)
//@Composable
//fun PreviewWorkoutRunCardFewReps() {
//    CadelaTheme {
//        WorkoutRunView(
//            null,
//            Session(
//                name = Session.FIRST_LEVEL_TEST.name,
//                exercises = listOf(
//                    Exercise(
//                        "A",
//                        Series(
//                            10, mutableListOf(
//                                Repetition(19),
//                                Repetition(18),
//                                Repetition(17),
//                                Repetition(0),
//                                Repetition(0),
//                                Repetition(0)
//                            ), Rest(30)
//                        ),
//                        Rest(120)
//                    ),
//                    Exercise("A1", Series(4), Rest(120)),
//                    Exercise("A2", Series(10), Rest(120)),
//                    Exercise("A3", Series(10), Rest(120)),
//                    Exercise("A4", Series(10), Rest(120)),
//                    Exercise("A5", Series(10), Rest(120)),
//                ),
//                levelStartedAt = LocalDate.of(2021, 6, 8)
//            )
//        )
//    }
//}
