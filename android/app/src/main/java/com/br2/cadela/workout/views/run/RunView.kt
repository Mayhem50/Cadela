package com.br2.cadela.workout.views.run

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.br2.cadela.compose.pagerSwipeAnimation
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
fun WorkoutRunView(viewModel: WorkoutViewModel?) {
    val scope = rememberCoroutineScope()
    val session =
        viewModel?.currentSession?.observeAsState() ?: remember { mutableStateOf(PREVIEW_SESSION) }
    val pagerState = rememberPagerState(pageCount = session?.value?.exercises?.size ?: 0)

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
            val exercise = session.value!!.exercises[page]
            val modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .graphicsLayer {
                    pagerSwipeAnimation(page, this)
                }
            RepetitionCard(viewModel, modifier, exercise) { repetitionDone ->
                viewModel?.setRepsForCurrentSerie(repetitionDone)
                moveToNext(viewModel, scope, pagerState)
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

@OptIn(ExperimentalPagerApi::class)
private fun moveToNext(
    viewModel: WorkoutViewModel?,
    scope: CoroutineScope,
    pagerState: PagerState
) {
    viewModel?.startRest {
        scope.launch {
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
    }
}

@Preview(name = "Full Reps", showBackground = true)
@Composable
fun PreviewWorkoutRunCardFullReps() {
    CadelaTheme {
        WorkoutRunView(
            null,
        )
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
