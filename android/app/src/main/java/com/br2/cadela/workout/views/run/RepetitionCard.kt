package com.br2.cadela.workout.views.run

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.br2.cadela.shared.stringResourceByName
import com.br2.cadela.workout.datas.Exercise
import com.br2.cadela.workout.datas.Repetition
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
private fun RepetitionList(
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