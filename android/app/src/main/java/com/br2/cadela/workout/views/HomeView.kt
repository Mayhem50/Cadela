package com.br2.cadela.workout.views

import android.text.format.DateUtils
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.br2.cadela.R
import com.br2.cadela.shared.stringResourceByName
import com.br2.cadela.shared.toFormattedString
import com.br2.cadela.ui.theme.CadelaTheme
import com.br2.cadela.workout.datas.*
import java.time.Duration
import java.time.LocalDate

@Composable
fun WorkoutHomeView(viewModel: WorkoutViewModel?, navController: NavController?) {
    val session = viewModel?.currentSession?.observeAsState()
    viewModel?.startSession()
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        session?.value?.let {
            HomeView(it, viewModel, navController)
        } ?: CircularProgressIndicator(color = Color.White)
    }

}

@Composable
private fun HomeView(session: Session, viewModel: WorkoutViewModel?, navController: NavController?) {
    Card(
        elevation = 3f.dp, modifier = Modifier
            .padding(16f.dp)
    ) {
        Column(
            Modifier
                .padding(16f.dp)
                .fillMaxWidth()
        ) {
            Header(session)

            session.exercises.mapIndexed { index, exercise ->
                ExerciseComponent(index, exercise)
            }

            Footer(session, viewModel, navController)
        }
    }
}

@Composable
private fun Footer(session: Session, viewModel: WorkoutViewModel?, navController: NavController?) {
    Text(
        text = getStartedSince(session),
        textAlign = TextAlign.End,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12f.dp)
    )
    Button(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 24f.dp), onClick = {
        viewModel?.runSession()
        navController?.navigate("workout_run")
    }) {
        Text(text = stringResource(R.string.start_session))
    }
}

@Composable
private fun ExerciseComponent(index: Int, exercise: Exercise) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 4f.dp)) {
            Text(
                text = stringResource(R.string.exercise_index, index + 1),
                style = TextStyle(
                    fontSize = 18f.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(1f / 9f)
        ) {
            Text(
                text = exercise.name,
                style = TextStyle(
                    fontSize = 18f.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                exercise.series.repetitions.map {
                    val text =
                        if (it.done == 0) stringResource(R.string.empty_serie) else it.done.toString()
                    val paddingHorizontal = if (it.done == 0) 5f.dp else 2f.dp
                    Text(
                        text = text,
                        modifier = Modifier.width(20f.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun Header(session: Session) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16f.dp)
    ) {
        Text(
            text =
            stringResourceByName(name = session.name),
            fontSize = 28f.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8f.dp)
        )

        Text(
            text = stringResource(
                R.string.session_duration,
                Duration.ofSeconds(session.estimatedTimeInSec).toFormattedString()
            ),
            fontSize = 16f.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8f.dp)
        )
    }
}

@Composable
private fun getStartedSince(session: Session): String {
    return if (session.notStarted) stringResource(R.string.session_not_started)
    else stringResource(
        R.string.session_started, DateUtils.getRelativeTimeSpanString(
            session.levelStartedAt.toEpochDay(),
            LocalDate.now().toEpochDay(),
            DateUtils.DAY_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_ALL
        ).toString().lowercase()
    )
}

@Preview(name = "Card", showBackground = true)
@Composable
fun PreviewCard() {
    CadelaTheme {
        HomeView(
            Session(
                name = Session.FIRST_LEVEL_TEST.name,
                exercises = listOf(
                    Exercise(
                        "A",
                        Series(
                            10,
                            mutableListOf(
                                Repetition(19),
                                Repetition(18),
                                Repetition(17),
                                Repetition(16),
                                Repetition(17),
                                Repetition(14),
                                Repetition(15),
                                Repetition(16),
                                Repetition(13),
                                Repetition(12),
                                Repetition(11),
                                Repetition(10),
                            ),
                            0,
                            Rest(30)
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
            ), null, null
        )
    }
}

@Preview(name = "Home", showBackground = true)
@Composable
fun PreviewHome() {
    CadelaTheme {
        WorkoutHomeView(null, null)
    }
}