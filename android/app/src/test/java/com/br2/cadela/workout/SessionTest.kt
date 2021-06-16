package com.br2.cadela.workout

import com.br2.cadela.workout.datas.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SessionTest {
    @Test
    fun `Session is complete if exercise's repetition contains one or more value more than 0`() {
        val completeSession = Session(
            "any_name", listOf(
                Exercise(
                    "any_name",
                    Series(2, mutableListOf(Repetition(1), Repetition(1))),
                    Rest(0)
                ),
                Exercise(
                    "any_name",
                    Series(2, mutableListOf(Repetition(1), Repetition(1))),
                    Rest(0)
                ),
                Exercise(
                    "any_name",
                    Series(2, mutableListOf(Repetition(1), Repetition(1))),
                    Rest(0)
                )
            )
        )
        val anotherCompleteSession = Session(
            "any_name", listOf(
                Exercise(
                    "any_name",
                    Series(2, mutableListOf(Repetition(1), Repetition(0))),
                    Rest(0)
                ),
                Exercise(
                    "any_name",
                    Series(2, mutableListOf(Repetition(1), Repetition(1))),
                    Rest(0)
                ),
                Exercise(
                    "any_name",
                    Series(2, mutableListOf(Repetition(1), Repetition(1))),
                    Rest(0)
                )
            )
        )
        val incompleteSession = Session(
            "any_name", listOf(
                Exercise(
                    "any_name",
                    Series(2, mutableListOf(Repetition(0), Repetition(0))),
                    Rest(0)
                ),
                Exercise(
                    "any_name",
                    Series(2, mutableListOf(Repetition(1), Repetition(1))),
                    Rest(0)
                ),
                Exercise(
                    "any_name",
                    Series(2, mutableListOf(Repetition(1), Repetition(1))),
                    Rest(0)
                )
            )
        )

        assertTrue(completeSession.isComplete)
        assertTrue(anotherCompleteSession.isComplete)
        assertFalse(incompleteSession.isComplete)
    }

    @Test
    fun `Session is not started if all exercise's series repetitions are at 0`() {
        val startedSession = Session(
            "any_name", listOf(
                Exercise(
                    "any_name",
                    Series(2, mutableListOf(Repetition(1), Repetition(1))),
                    Rest(0)
                ),
                Exercise(
                    "any_name",
                    Series(2, mutableListOf(Repetition(0), Repetition(0))),
                    Rest(0)
                ),
                Exercise(
                    "any_name",
                    Series(2, mutableListOf(Repetition(0), Repetition(0))),
                    Rest(0)
                ),
            )
        )
        val notStartedSession = Session(
            "any_name", listOf(
                Exercise(
                    "any_name",
                    Series(2, mutableListOf(Repetition(0), Repetition(0))),
                    Rest(0)
                ),
                Exercise(
                    "any_name",
                    Series(2, mutableListOf(Repetition(0), Repetition(0))),
                    Rest(0)
                ),
                Exercise(
                    "any_name",
                    Series(2, mutableListOf(Repetition(0), Repetition(0))),
                    Rest(0)
                ),
                Exercise(
                    "any_name",
                    Series(2, mutableListOf(Repetition(0), Repetition(0))),
                    Rest(0)
                )
            )
        )

        assertFalse(startedSession.notStarted)
        assertTrue(notStartedSession.notStarted)
    }

    @Test
    fun `Compute estimated session time`() {
        val session660 = Session(
            "any_name", listOf(
                Exercise(
                    "any_name",
                    Series(1, mutableListOf(Repetition(1), Repetition(1)), Rest(120)),
                    Rest(180)
                ),
                Exercise(
                    "any_name",
                    Series(1, mutableListOf(Repetition(1), Repetition(1)), Rest(120)),
                    Rest(180)
                ),
                Exercise(
                    "any_name",
                    Series(1, mutableListOf(Repetition(1), Repetition(1)), Rest(120)),
                    Rest(180)
                ),
                Exercise(
                    "any_name",
                    Series(1, mutableListOf(Repetition(1), Repetition(1)), Rest(120)),
                    Rest(180)
                ),
            )
        )
        val session960 = Session(
            "any_name", listOf(
                Exercise(
                    "any_name",
                    Series(2, mutableListOf(Repetition(1), Repetition(1)), Rest(120)),
                    Rest(180)
                ),
                Exercise(
                    "any_name",
                    Series(3, mutableListOf(Repetition(0), Repetition(0)), Rest(90)),
                    Rest(120)
                ),
                Exercise(
                    "any_name",
                    Series(4, mutableListOf(Repetition(0), Repetition(0)), Rest(30)),
                    Rest(100)
                ),
            )
        )
        val session1080 = Session(
            "any_name", listOf(
                Exercise(
                    "any_name",
                    Series(2, mutableListOf(Repetition(1), Repetition(1)), Rest(120)),
                    Rest(180)
                ),
                Exercise(
                    "any_name",
                    Series(3, mutableListOf(Repetition(0), Repetition(0)), Rest(90)),
                    Rest(240)
                ),
                Exercise(
                    "any_name",
                    Series(4, mutableListOf(Repetition(0), Repetition(0)), Rest(30)),
                    Rest(100)
                ),
            )
        )

        assertEquals(660, session660.estimatedTimeInSec)
        assertEquals(960, session960.estimatedTimeInSec)
        assertEquals(1080, session1080.estimatedTimeInSec)
    }
}