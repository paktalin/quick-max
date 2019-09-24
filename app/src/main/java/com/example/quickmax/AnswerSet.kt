package com.example.quickmax


class AnswerSet(numDigits: Int): Iterable<Answer> {

    private val buttonIds =
        listOf(R.id.btn_left_top, R.id.btn_right_top, R.id.btn_left_bottom, R.id.btn_right_bottom)
    private val numOptions = 4

    val numbers: MutableList<Answer> =
        MutableList(numOptions) { i -> Answer(buttonIds[i], generateRandom(numDigits)) }

    init {
        val correctAnswer = findSecondMax(numbers.map { n -> n.value })
        numbers.forEach { n ->
            n.correct = n.value == correctAnswer
        }
    }

    override fun iterator(): Iterator<Answer> {
        return numbers.iterator()
    }
}