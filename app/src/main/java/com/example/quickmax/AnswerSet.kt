package com.example.quickmax


class AnswerSet(numDigits: Int): Iterable<Answer> {

    private val buttonIds = listOf(R.id.btn_left_top, R.id.btn_right_top, R.id.btn_left_bottom, R.id.btn_right_bottom)
    private val numAnswers = 4
    private lateinit var correctAnswer: Answer

    val answers: MutableList<Answer> = mutableListOf()
    init {
        val randomNumbers = generateNRandomNumbers(numDigits, numAnswers)
        for (i in 0 until numAnswers)
            answers.add(Answer(buttonIds[i], randomNumbers.elementAt(i)))

        val correctAnswerNum = findSecondMax(answers.map { answer -> answer.value })
        answers.forEach { n ->
            n.correct = n.value == correctAnswerNum
            if (n.correct)
                correctAnswer = n
        }
    }

    override fun iterator(): Iterator<Answer> {
        return answers.iterator()
    }

}