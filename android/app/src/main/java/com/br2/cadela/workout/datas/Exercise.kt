package com.br2.cadela.workout.datas

data class Exercise(
    val name: String,
    val series: Series,
    val restAfter: Rest?,
    val speed: ESpeed = ESpeed.NORMAL
) {
    companion object {
        val A = Exercise(name = "A", Series(1), null)
        val A1 = Exercise(name = "A1", Series(1), null)
        val A2 = Exercise(name = "A2", Series(1), null)
        val A3 = Exercise(name = "A3", Series(1), null)
        val A4 = Exercise(name = "A4", Series(1), null)
        val A5 = Exercise(name = "A5", Series(1), null)
        val A6 = Exercise(name = "A6", Series(1), null)
        val A7 = Exercise(name = "A7", Series(1), null)
        val A8 = Exercise(name = "A8", Series(1), null)
        val A9 = Exercise(name = "A9", Series(1), null)
        val A10 = Exercise(name = "A10", Series(1), null)
        val A11 = Exercise(name = "A11", Series(1), null)
        val A12 = Exercise(name = "A12", Series(1), null)


        val B = Exercise(name = "B", Series(1), null)
        val B1 = Exercise(name = "B1", Series(1), null)
        val B2 = Exercise(name = "B2", Series(1), null)

        val C = Exercise(name = "C", Series(1), null)
        val C1 = Exercise(name = "C1", Series(1), null)
        val C2 = Exercise(name = "C2", Series(1), null)
        val C3 = Exercise(name = "C3", Series(1), null)
        val C4 = Exercise(name = "C4", Series(1), null)
        val C5 = Exercise(name = "C5", Series(1), null)
        val C6 = Exercise(name = "C6", Series(1), null)
        val C7 = Exercise(name = "C7", Series(1), null)
        val C8 = Exercise(name = "C8", Series(1), null)
        val C9 = Exercise(name = "C9", Series(1), null)

        val D = Exercise(name = "D", Series(1), null)

        val E = Exercise(name = "E", Series(1), null)
        val E1 = Exercise(name = "E1", Series(1), null)
        val E2 = Exercise(name = "E2", Series(1), null)
        val E3 = Exercise(name = "E3", Series(1), null)
        val E4 = Exercise(name = "E4", Series(1), null)
        val E5 = Exercise(name = "E5", Series(1), null)
        val E6 = Exercise(name = "E6", Series(1), null)
        val E7 = Exercise(name = "E7", Series(1), null)

        val F = Exercise(name = "F", Series(1), null)
        val F1 = Exercise(name = "F1", Series(1), null)
        val F2 = Exercise(name = "F2", Series(1), null)
        val F3 = Exercise(name = "F3", Series(1), null)

        val G = Exercise(name = "G", Series(1), null)

        val H = Exercise(name = "H", Series(1), null)

        val I = Exercise(name = "I", Series(1), null)
        val I1 = Exercise(name = "I1", Series(1), null)
        val I2 = Exercise(name = "I2", Series(1), null)
        val I3 = Exercise(name = "I3", Series(1), null)
        val I4 = Exercise(name = "I4", Series(1), null)
        val I5 = Exercise(name = "I5", Series(1), null)
        val I6 = Exercise(name = "I6", Series(1), null)
        val I7 = Exercise(name = "I7", Series(1), null)
        val I8 = Exercise(name = "I8", Series(1), null)

        val J = Exercise(name = "J", Series(1), null)
        val J1 = Exercise(name = "J1", Series(1), null)
        val J2 = Exercise(name = "J2", Series(1), null)
        val J3 = Exercise(name = "J3", Series(1), null)

        val K = Exercise(name = "K", Series(1), null)
        val K1 = Exercise(name = "K1", Series(1), null)
        val K2 = Exercise(name = "K2", Series(1), null)
        val K3 = Exercise(name = "K3", Series(1), null)

        val L = Exercise(name = "L", Series(1), null)

        val M = Exercise(name = "M", Series(1), null)
        val M1 = Exercise(name = "M1", Series(1), null)

        val N = Exercise(name = "N", Series(1), null)
        val N1 = Exercise(name = "N1", Series(1), null)

        val O = Exercise(name = "O", Series(1), null)

        val P = Exercise(name = "P", Series(1), null)

        val Q = Exercise(name = "Q", Series(1), null)
        val Q1 = Exercise(name = "Q1", Series(1), null)
        val Q2 = Exercise(name = "Q2", Series(1), null)

        val R = Exercise(name = "R", Series(1), null)

        val S = Exercise(name = "S", Series(1), null)

        val T = Exercise(name = "T", Series(1), null)

        val U = Exercise(name = "U", Series(1), null)

        val V = Exercise(name = "V", Series(1), null)

        val W = Exercise(name = "W", Series(1), null)

        val X = Exercise(name = "X", Series(1), null)

        val Y = Exercise(name = "Y", Series(1), null)

        val Z = Exercise(name = "Z", Series(1), null)
    }
}

fun Exercise.withSlowSpeed() = Exercise(name, series, restAfter, ESpeed.SLOW)
fun Exercise.withNormalSpeed() = Exercise(name, series, restAfter, ESpeed.NORMAL)
fun Exercise.withFastSpeed() = Exercise(name, series, restAfter, ESpeed.FAST)

fun Exercise.withRestOf(rest: Rest) = Exercise(name, series, rest, speed)

fun Exercise.withSeries(series: Series) =
    Exercise(name, series, restAfter)

fun MutableList<Exercise>.replaceExercise(search: String, replaceBy: String, threshold: Int) {
    val index = indexOfFirst { it.name == search }
    if (index >= 0) {
        val exercise = elementAt(index)
        if (exercise.series.repetitions[0].done >= threshold) {
            this[index] =
                Exercise(
                    replaceBy,
                    Series(exercise.series.count),
                    exercise.restAfter,
                    exercise.speed
                )
        }
    }
}

fun MutableList<Exercise>.changeTarget(search: String, target: Int, threshold: Int) {
    val index = indexOfFirst { it.name == search }
    if (index >= 0) {
        val exercise = elementAt(index)
        if (exercise.series.meanRepsDone < threshold) {
            this[index] =
                Exercise(
                    exercise.name,
                    exercise.series.newTarget(target),
                    exercise.restAfter,
                    exercise.speed
                )
        }
    }
}

fun MutableList<Exercise>.addRepetitionsOn(name: String, seriesCount: Int) {
    val index = indexOfFirst { it.name == name }
    if (index >= 0) {
        val exercise = this[index]
        this[index] = Exercise(
            exercise.name,
            Series(count = seriesCount, restAfter = exercise.series.restAfter),
            exercise.restAfter,
            exercise.speed
        )
    }
}
