package com.br2.cadela.workout

import com.br2.cadela.workout.datas.*
import com.br2.cadela.workout.repositories.SessionRecord
import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDate

class FirstLevelTestSessionTest : WorkoutTestBase() {
    @Test
    fun `The first session is a Test session that contains exercise A, B, C, A1 and rests between are 3min`() {
        val session = sut.createNewSession()

        Assertions.assertEquals(Session.FIRST_LEVEL_TEST, session)
    }

    @Test
    fun `B session result is under 4, next session wil be First Program`() {
        val previousSession = Session(
            name = "first_level_test",
            exercises = listOf(
                Exercise("B", Series(1, mutableListOf(Repetition(3))), restAfter = Rest(120)),
                Exercise("C", Series(1, mutableListOf(Repetition(1))), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)
        Assertions.assertEquals(Session.FIRST_PROGRAM, session)
    }

    @Test
    fun `B session result is under 4 and C is 0, next session wil be First Program with C4 instead of C1`() {
        val previousSession = Session(
            name = "first_level_test",
            exercises = listOf(
                Exercise("B", Series(1, mutableListOf(Repetition(3))), restAfter = Rest(120)),
                Exercise("C", Series(1, mutableListOf(Repetition(0))), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)
        Assertions.assertEquals(Session.FIRST_PROGRAM_WITH_C4, session)
    }


    @Test
    fun `B session result is 4 or more and C is 0, next session wil be First Program with C4 instead of C1`() {
        val previousSession = Session(
            name = "first_level_test",
            exercises = listOf(
                Exercise("B", Series(1, mutableListOf(Repetition(4))), restAfter = Rest(120)),
                Exercise("C", Series(1, mutableListOf(Repetition(0))), restAfter = null)
            )
        )
        val session = sut.createNewSession(previousSession)
        Assertions.assertEquals(Session.SECOND_PROGRAM_WITH_C4, session)
    }

    @Test
    fun `B session result is 4 or more, next session wil be Second Program`() {
        val previousSession = Session(
            name = "first_level_test",
            exercises = listOf(
                Exercise("B", Series(1, mutableListOf(Repetition(4))), restAfter = Rest(120)),
                Exercise("C", Series(1, mutableListOf(Repetition(1))), restAfter = null)
            ))

        val session = sut.createNewSession(previousSession)
        Assertions.assertEquals(Session.SECOND_PROGRAM, session)
    }

    @Test
    fun `If next session is on same level levelStartedAt stays the same`() = runBlocking {
        val firstProgramOfFirstLevel = Session(
            name = Session.FIRST_PROGRAM.name,
            exercises = Session.FIRST_PROGRAM.exercises.map {
                Exercise(
                    it.name,
                    Series(it.series.count, it.series.repetitions.map { Repetition(3) }.toMutableList()),
                    it.restAfter
                )
            },
            levelStartedAt = LocalDate.of(2021, 5, 30))

        coEvery { sessionDao.getLastSession() } returns SessionRecord(
            session = firstProgramOfFirstLevel
        )

        val secondProgramOfFirstLevel = sut.startNewSession()
        Assertions.assertEquals(firstProgramOfFirstLevel.levelStartedAt, secondProgramOfFirstLevel.levelStartedAt)
    }
}