package com.br2.cadela.workout

import com.br2.cadela.workout.datas.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SecondLevelSessionTest : WorkoutTestBase() {
    @Test
    fun `E all series repetitions are 10 or more, next session replace E by E1`() {
        val previousSession = Session(
            name = "level_2",
            exercises = listOf(
                Exercise(
                    name = "B1",
                    series = Series(6),
                    restAfter = Rest(duration = 25),
                    speed = ESpeed.FAST
                ),
                Exercise(
                    name = "A3",
                    series = Series(6),
                    restAfter = Rest(duration = 25),
                    speed = ESpeed.FAST
                ),
                Exercise(
                    name = "A2",
                    series = Series(6),
                    restAfter = Rest(duration = 180),
                    speed = ESpeed.FAST
                ),
                Exercise(
                    name = "C1",
                    series = Series(6),
                    restAfter = Rest(duration = 180),
                    speed = ESpeed.FAST
                ),
                Exercise(
                    name = "E",
                    series = Series(6, MutableList(6) { Repetition(10) }),
                    restAfter = Rest(duration = 180)
                ),
                Exercise(name = "F", series = Series(4), restAfter = Rest(duration = 180)),
                Exercise(name = "G", series = Series(6), restAfter = Rest(duration = 90)),
                Exercise(name = "H", series = Series(6), restAfter = Rest(duration = 60)),
                Exercise(name = "K2", series = Series(3), restAfter = null)
            )
        )

        val session = sut.createNewSession(previousSession)

        assertEquals(previousSession.replaceExerciseNameAndClearReps("E", "E1"), session)
    }

    @Test
    fun `If doing 8 or more on all exercises except G and K2, next session will be level 3`() {
        val previousSession = Session(
            name = "level_2",
            exercises = listOf(
                Exercise(
                    name = "B1",
                    series = Series(6, MutableList(6) { Repetition(8) }),
                    restAfter = Rest(duration = 25),
                    speed = ESpeed.FAST
                ),
                Exercise(
                    name = "A3",
                    series = Series(6, MutableList(6) { Repetition(8) }),
                    restAfter = Rest(duration = 25),
                    speed = ESpeed.FAST
                ),
                Exercise(
                    name = "A2",
                    series = Series(6, MutableList(6) { Repetition(8) }),
                    restAfter = Rest(duration = 180),
                    speed = ESpeed.FAST
                ),
                Exercise(
                    name = "C1",
                    series = Series(6, MutableList(6) { Repetition(8) }),
                    restAfter = Rest(duration = 180),
                    speed = ESpeed.FAST
                ),
                Exercise(
                    name = "E",
                    series = Series(6, MutableList(6) { Repetition(10) }),
                    restAfter = Rest(duration = 180)
                ),
                Exercise(name = "F", series = Series(4, MutableList(4) { Repetition(10) }), restAfter = Rest(duration = 180)),
                Exercise(name = "G", series = Series(6, MutableList(6) { Repetition(10) }), restAfter = Rest(duration = 90)),
                Exercise(name = "H", series = Series(6, MutableList(6) { Repetition(10) }), restAfter = Rest(duration = 60)),
                Exercise(name = "K2", series = Series(3, MutableList(3) { Repetition(10) }), restAfter = null)
            )
        )

        val session = sut.createNewSession(previousSession)

        assertEquals(Session.THIRD_LEVEL, session)
    }

    @Test
    fun `If can not do 5 reps on all C1 serie, next session will target 3 reps on all C1 serie`() {
        val previousSession = Session(
            name = "level_2",
            exercises = listOf(
                Exercise(
                    name = "B1",
                    series = Series(6),
                    restAfter = Rest(duration = 25),
                    speed = ESpeed.FAST
                ),
                Exercise(
                    name = "A3",
                    series = Series(6),
                    restAfter = Rest(duration = 25),
                    speed = ESpeed.FAST
                ),
                Exercise(
                    name = "A2",
                    series = Series(6),
                    restAfter = Rest(duration = 180),
                    speed = ESpeed.FAST
                ),
                Exercise(
                    name = "C1",
                    series = Series(6, MutableList(6) { Repetition(3) }),
                    restAfter = Rest(duration = 180),
                    speed = ESpeed.FAST
                ),
                Exercise(
                    name = "E",
                    series = Series(6),
                    restAfter = Rest(duration = 180)
                ),
                Exercise(name = "F", series = Series(4), restAfter = Rest(duration = 180)),
                Exercise(name = "G", series = Series(6), restAfter = Rest(duration = 90)),
                Exercise(name = "H", series = Series(6), restAfter = Rest(duration = 60)),
                Exercise(name = "K2", series = Series(3), restAfter = null)
            )
        )

        val session = sut.createNewSession(previousSession)

        assertEquals(previousSession.changeTargetForExerciseAndClearReps("C1", 3), session)
    }
}