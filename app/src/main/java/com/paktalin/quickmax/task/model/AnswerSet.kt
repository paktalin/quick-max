package com.paktalin.quickmax.task.model

import com.paktalin.quickmax.task.ui.AnswerCardView

private const val NUM_ANSWERS = 4

class AnswerSet: Iterable<Answer> {
    private lateinit var correctAnswer: Answer

    val answers: MutableList<Answer> = mutableListOf()

    constructor(numDigits: Int, cards: List<AnswerCardView>) {
        val randomNumbers = generateNRandomNumbers(numDigits, NUM_ANSWERS)
        for (i in 0 until NUM_ANSWERS)
            answers.add(Answer(cards[i], randomNumbers.elementAt(i)))
        setCorrectAnswer()
    }

    constructor(answerValues: IntArray, cards: List<AnswerCardView>) {
        for (i in answerValues.indices)
            answers.add(Answer(cards[i], answerValues[i]))
        setCorrectAnswer()
    }

    private fun setCorrectAnswer() {
        val correctAnswerNum = findSecondMax(answers.map { answer -> answer.value })
        answers.forEach { answer ->
            if (answer.value == correctAnswerNum) {
                answer.correct
                correctAnswer = answer
            }
        }
    }

    override fun iterator(): Iterator<Answer> {
        return answers.iterator()
    }

}