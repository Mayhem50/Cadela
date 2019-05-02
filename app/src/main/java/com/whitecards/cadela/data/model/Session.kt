package com.whitecards.cadela.data.model

import com.google.firebase.database.PropertyName
import com.whitecards.cadela.utils.ModelHelpers
import java.text.DateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class Session{
    @PropertyName("is_pending") var isPrending: Boolean = false

    @get:PropertyName("series_repetition")
    @set:PropertyName("series_repetition")
    var seriesRepetitionsString: String = ""
        set(value){
            seriesRepetitions = ModelHelpers.stringToListInt(value)
        }

    @get:PropertyName("date_program_start")
    @set:PropertyName("date_program_start")
    var dateOfProgramBeginingString: String = ""
        set(value){
            var formatter = DateFormat.getDateInstance(DateFormat.FULL, Locale.FRANCE)
            try {
                dateOfProgramBegining = LocalDateTime.parse(value, formatter)
            }
        }

    @get:PropertyName("creation_date")
    @set:PropertyName("creation_date")
    var dateOfCreationString: String = ""
        set(value){
            dateOfProgramBegining = LocalDateTime.parse(value)
        }

    @PropertyName("program") lateinit var program: Program

    var meanSeriesRepetitions: ArrayList<Int> = ArrayList()
    var seriesRepetitions: ArrayList<Int> = ArrayList()
    var dateOfProgramBegining: LocalDateTime = LocalDateTime.now()
    var dateOfCreation: LocalDateTime = LocalDateTime.now()

    companion object{
        fun createFromPrevious(session: Session?): Session{
            val (program, levelchanged) = Program.getProgramFfromPreviousSession(session)
            var result = Session().apply {
                isPrending = false
                dateOfCreation = LocalDateTime.now()
                dateOfProgramBegining = LocalDateTime.now()
            }

            session?.let {
                if(session.isPrending) return session
                if(!levelchanged){
                    result.dateOfProgramBegining = it.dateOfProgramBegining
                }

                for (i in it.program.targetRepetitionAtStart.indices){
                    result.program.targetRepetitionAtStart[i] = it.meanSeriesRepetitions[i] + 1
                }

                return result
            }

            return result
        }
    }
}