package com.br2.cadela.workout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.br2.cadela.compose.pagerSwipeAnimation
import com.br2.cadela.ui.theme.CadelaTheme
import com.br2.cadela.ui.theme.Red200
import com.br2.cadela.ui.theme.Red700
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate

private val DEFAULT_REPETITION_DONE = 10

private val PREVIEW_SESSION = Session(
    name = Session.FIRST_LEVEL_TEST.name,
    exercises = listOf(
        Exercise(
            "A",
            Series(
                DEFAULT_REPETITION_DONE, mutableListOf(
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
        Exercise("A2", Series(DEFAULT_REPETITION_DONE), Rest(120)),
        Exercise("A3", Series(DEFAULT_REPETITION_DONE), Rest(120)),
        Exercise("A4", Series(DEFAULT_REPETITION_DONE), Rest(120)),
        Exercise("A5", Series(DEFAULT_REPETITION_DONE), Rest(120)),
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

@Composable
private fun RepetitionCard(
    viewModel: WorkoutViewModel?,
    modifier: Modifier,
    exercise: Exercise,
    callback: (repetitionDone: Int) -> Unit
) {
    Card(
        modifier = modifier
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            val (title, repetitions, button, input) = createRefs()
            val verticalGuideline = createGuidelineFromStart(.65f)
            var repetitionDone = DEFAULT_REPETITION_DONE
            val disabled by viewModel?.isResting?.observeAsState(initial = true)
                ?: remember { mutableStateOf(true) }

            Text(
                text = exercise.name,
                style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .constrainAs(ref = title) {
                        top.linkTo(parent.top, 8.dp)
                        start.linkTo(parent.start, 8.dp)
                        end.linkTo(parent.end, 8.dp)
                    }
                    .wrapContentHeight()
            )

            RepetitionList(
                viewModel = viewModel,
                repetitions = exercise.series.repetitions,
                modifier = Modifier.constrainAs(ref = repetitions) {
                    top.linkTo(title.bottom, 8.dp)
                    bottom.linkTo(button.top, 8.dp)
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(verticalGuideline, 8.dp)

                    this.height = Dimension.fillToConstraints
                    this.width = Dimension.fillToConstraints
                })

            Button(
                enabled = !disabled,
                onClick = { callback(repetitionDone) },
                modifier = Modifier.constrainAs(ref = button) {
                    bottom.linkTo(parent.bottom, 8.dp)
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                }) {
                Text(text = "Done")
            }

            RepetitionInput(
                enabled = !disabled,
                modifier = Modifier.constrainAs(ref = input) {
                    start.linkTo(verticalGuideline, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)

                }) { value -> repetitionDone = value }
        }
    }
}

@Composable
fun RepetitionInput(
    enabled: Boolean,
    modifier: Modifier,
    updateAction: (value: Int) -> Unit
) {
    var value by remember { mutableStateOf(DEFAULT_REPETITION_DONE) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .graphicsLayer {
                shape = RoundedCornerShape(corner = CornerSize(4.dp))
                clip = true
            }
            .background(Color.White)
            .padding(8.dp)) {
        Button(
            enabled = enabled,
            onClick = {
                value += 1
                updateAction(value)
            }, modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                text = "$value",
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            )
            Icon(
                imageVector = Icons.Filled.FitnessCenter,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(20.dp)
            )
        }

        Button(
            enabled = enabled,
            onClick = {
                value -= 1
                updateAction(value)
            }, modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Icon(imageVector = Icons.Filled.Remove, contentDescription = null)
        }
    }
}

@Composable
fun RepetitionList(
    viewModel: WorkoutViewModel?,
    repetitions: List<Repetition>,
    modifier: Modifier
) {
    val currentIndex by
    viewModel?.currentSerieIndex?.observeAsState() ?: remember { mutableStateOf(5) }
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier
    ) {
        repetitions.mapIndexed { index, it ->
            RepetitionRow(currentIndex, index, it)
        }
    }
}

@Composable
private fun RepetitionRow(
    currentIndex: Int?,
    index: Int,
    repetition: Repetition
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        RepetitionRowIcon(
            currentIndex = currentIndex ?: 0,
            index = index,
            modifier = Modifier.weight(0.5f)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Rep.${index + 1}")
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (repetition.done == 0) "-" else "${repetition.done}",
                textAlign = TextAlign.Center
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun RepetitionRowIcon(currentIndex: Int, index: Int, modifier: Modifier) {
    fun isCurrentIndex() = currentIndex == index
    fun isBeforeCurrentIndex() =
        currentIndex != index && index < currentIndex

    AnimatedVisibility(
        visible = isCurrentIndex(),
        modifier = modifier
    ) {
        Column {
            Icon(
                imageVector = Icons.Filled.ArrowRight,
                contentDescription = null
            )
        }
    }
    AnimatedVisibility(
        visible = isBeforeCurrentIndex(),
        modifier = modifier
    ) {
        Column {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null
            )
        }
    }
    AnimatedVisibility(
        visible = !(isBeforeCurrentIndex() || isCurrentIndex()),
        modifier = modifier
    ) {
        Box {}
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

@Composable
fun CircularTimer(viewModel: WorkoutViewModel?, modifier: Modifier) {
    val progress = viewModel?.restProgress?.observeAsState(initial = 0f)
    val timeString = viewModel?.timeDisplay?.observeAsState()

    Box(modifier = modifier.wrapContentSize(), contentAlignment = Alignment.Center) {
        Text(
            text = timeString?.value ?: "--:--",
            modifier = Modifier.fillMaxWidth(0.65f),
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                letterSpacing = 1.2f.sp
            )
        )
        CircularProgressIndicator(
            progress = 1.0f,
            strokeWidth = 6.dp,
            color = Color.LightGray,
            modifier = Modifier
                .fillMaxWidth(.65f)
                .height(240.dp)
        )
        CircularProgressIndicator(
            progress = progress?.value ?: .5f, strokeWidth = 6.dp,
            modifier = Modifier
                .fillMaxWidth(.65f)
                .height(240.dp)
        )
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
