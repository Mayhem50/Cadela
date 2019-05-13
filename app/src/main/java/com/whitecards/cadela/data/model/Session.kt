package com.whitecards.cadela.data.model

import com.google.firebase.database.PropertyName
import com.whitecards.cadela.utils.ModelHelpers
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class Session {
    @PropertyName("is_pending")
    var isPrending: Boolean = false

    @get:PropertyName("series_repetition")
    @set:PropertyName("series_repetition")
    var seriesRepetitionsString: String = ""
        set(value) {
            seriesRepetitions = ModelHelpers.stringToListInt(value)
        }

    @get:PropertyName("date_program_start")
    @set:PropertyName("date_program_start")
    var dateOfProgramBeginingString: String = ""
        set(value) {
            dateOfProgramBegining = tryParseLocalDateTime(value)
        }

    @get:PropertyName("creation_date")
    @set:PropertyName("creation_date")
    var dateOfCreationString: String = ""
        set(value) {
            dateOfCreation = tryParseLocalDateTime(value)
        }

    @PropertyName("program")
    lateinit var program: Program

    var meanSeriesRepetitions: ArrayList<Int> = ArrayList()
    var seriesRepetitions: ArrayList<Int> = ArrayList()
    var dateOfProgramBegining: LocalDateTime = LocalDateTime.now()
    var dateOfCreation: LocalDateTime = LocalDateTime.now()

    companion object {
        fun createFromPrevious(session: Session?): Session {
            val (program, levelChanged) = Program.getProgramFromPreviousSession(session)
            var result = Session().apply {
                isPrending = false
                dateOfCreation = LocalDateTime.now()
                dateOfProgramBegining = LocalDateTime.now()
                this.program = program
            }

            session?.let {
                if (session.isPrending) return session
                if (!levelChanged) {
                    result.dateOfProgramBegining = it.dateOfProgramBegining
                }

                for (i in it.program.targetRepetitionAtStart.indices) {
                    result.program.targetRepetitionAtStart[i] = it.meanSeriesRepetitions[i] + 1
                }

                return result
            }

            return result
        }
    }
}

private fun tryParseLocalDateTime(string: String): LocalDateTime {
    return Instant.ofEpochSecond(string.toLong()).atZone(ZoneId.systemDefault()).toLocalDateTime()
}