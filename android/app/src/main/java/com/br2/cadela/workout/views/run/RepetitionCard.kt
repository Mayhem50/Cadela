package com.br2.cadela.workout.views.run

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutBaseScope
import androidx.constraintlayout.compose.ConstraintLayoutScope
import androidx.constraintlayout.compose.Dimension
import com.br2.cadela.shared.stringResourceByName
import com.br2.cadela.ui.theme.CadelaTheme
import com.br2.cadela.workout.datas.Exercise
import com.br2.cadela.workout.datas.Repetition
import com.br2.cadela.workout.datas.Series
import com.br2.cadela.workout.views.WorkoutViewModel

private val DEFAULT_REPETITION_DONE = 10

@Composable
fun RepetitionCard(
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
            val (title, repetitions, button, input, speedIndicator) = createRefs()
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

            Row(modifier = Modifier.constrainAs(ref = speedIndicator) {
                top.linkTo(title.top)
                bottom.linkTo(title.bottom)
                end.linkTo(input.end)
                start.linkTo(input.start)
            }) {
                Icon(imageVector = Icons.Filled.Speed, contentDescription = null)
                Text(
                    text = stringResourceByName(name = "speed_${exercise.speed.text}").capitalize(
                        Locale.current
                    ),
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            RepetitionList(
                viewModel = viewModel,
                repetitions = exercise.series.repetitions,
                modifier = Modifier.constrainAs(ref = repetitions) {
                    top.linkTo(title.bottom, 16.dp)
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
private fun RepetitionInput(
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
                shadowElevation = 5f
            }
            .background(if(isSystemInDarkTheme()) Color.DarkGray else Color.White)
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
                    color = if(isSystemInDarkTheme()) Color.White else Color.Black
                )
            )
            Icon(
                imageVector = Icons.Filled.FitnessCenter,
                contentDescription = null,
                tint = if(isSystemInDarkTheme()) Color.White else Color.Black,
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
private fun RepetitionList(
    viewModel: WorkoutViewModel?,
    repetitions: List<Repetition>,
    modifier: Modifier
) {
    val currentIndex by
    viewModel?.currentSerieIndex?.observeAsState() ?: remember { mutableStateOf(5) }
    ConstraintLayout(
        modifier = modifier.padding(top = 16.dp)
    ) {
        val guideline1 = createGuidelineFromStart(.05f)
        val guideline2 = createGuidelineFromStart(.25f)
        val guideline3 = createGuidelineFromStart(.55f)
        val guideline4 = createGuidelineFromStart(.85f)

        val columnModifiers = columnModifiers(guideline1, guideline2, guideline3, guideline4)

        RepetitionListHeader(columnModifiers = columnModifiers)

        repetitions.mapIndexed { index, it ->
            RepetitionRow(
                currentIndex, index, it,
                columnModifiers(
                    guideline1 = guideline1,
                    guideline2 = guideline2,
                    guideline3 = guideline3,
                    guideline4 = guideline4,
                    index = index + 1
                )
            )
        }
    }
}

@Composable
private fun ConstraintLayoutScope.columnModifiers(
    guideline1: ConstraintLayoutBaseScope.VerticalAnchor,
    guideline2: ConstraintLayoutBaseScope.VerticalAnchor,
    guideline3: ConstraintLayoutBaseScope.VerticalAnchor,
    guideline4: ConstraintLayoutBaseScope.VerticalAnchor,
    index: Int = 0
): ColumnModifiers {
    val (col1, col2, col3, col4) = createRefs()
    val rowOffset = 32.dp * index
    val horizontalGuideline = createGuidelineFromTop(rowOffset)
    return ColumnModifiers(
        Modifier.constrainAs(ref = col1) {
            centerAround(horizontalGuideline)
            centerAround(guideline1)
        },
        Modifier.Companion.constrainAs(ref = col2) {
            centerAround(horizontalGuideline)
            centerAround(guideline2)
        },
        Modifier.Companion.constrainAs(ref = col3) {
            centerAround(horizontalGuideline)
            centerAround(guideline3)
        },
        Modifier.Companion.constrainAs(ref = col4) {
            centerAround(horizontalGuideline)
            centerAround(guideline4)
        })
}

@Composable
fun RepetitionListHeader(columnModifiers: ColumnModifiers) {
    val style= TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
    Box(modifier = columnModifiers.col1)
    Text(text = "Series", textAlign = TextAlign.Center, style = style, modifier = columnModifiers.col2)
    Text(text = "Done", textAlign = TextAlign.Center, style = style, modifier = columnModifiers.col3)
    Text(text = "Target", textAlign = TextAlign.Center, style = style, modifier = columnModifiers.col4)
}

@Composable
private fun RepetitionRow(
    currentIndex: Int?,
    index: Int,
    repetition: Repetition,
    columnModifiers: ColumnModifiers
) {

    RepetitionRowIcon(
        currentIndex = currentIndex ?: 0,
        index = index,
        modifier = columnModifiers.col1
    )
    Text(text = "${index + 1}", textAlign = TextAlign.Center, modifier = columnModifiers.col2)
    Text(
        text = if (repetition.done == 0) "-" else "${repetition.done}",
        textAlign = TextAlign.Center,
        modifier = columnModifiers.col3
    )
    Text(
        text = if (repetition.target == 0) "MAX" else "${repetition.target}",
        textAlign = TextAlign.Center,
        modifier = columnModifiers.col4
    )


}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun RepetitionRowIcon(currentIndex: Int, index: Int, modifier: Modifier) {
    fun isCurrentIndex() = currentIndex == index
    fun isBeforeCurrentIndex() =
        currentIndex != index && index < currentIndex

    Box(modifier = modifier.height(20.dp)) {
        AnimatedVisibility(
            visible = isCurrentIndex(),
            enter = fadeIn(),
            exit = fadeOut()
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
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null
                )
            }
        }
    }
}

data class ColumnModifiers(
    val col1: Modifier,
    val col2: Modifier,
    val col3: Modifier,
    val col4: Modifier
)

@Preview
@Composable
fun PreviewExerciseCard() {
    CadelaTheme {
        RepetitionCard(
            viewModel = null,
            modifier = Modifier,
            exercise = Exercise("A", Series(7), null)
        ) {

        }
    }
}