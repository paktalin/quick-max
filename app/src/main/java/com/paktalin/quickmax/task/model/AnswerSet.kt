package com.paktalin.quickmax.task.model

import android.view.View
import com.paktalin.quickmax.task.ui.AnswerCardView
import kotlinx.android.synthetic.main.fragment_answers.view.*

private const val NUM_ANSWERS = 4

class AnswerSet: Iterable<Answer> {

    val answers: MutableList<Answer> = mutableListOf()

    constructor(numDigits: Int, view: View) {
        val cards = cardsFromView(view)
        val randomNumbers = generateNRandomNumbers(numDigits, NUM_ANSWERS)
        for (i in 0 until NUM_ANSWERS)
            answers.add(Answer(cards[i], randomNumbers.elementAt(i)))
        setCorrectAnswer()
    }

    constructor(answerValues: IntArray, view: View) {
        val cards = cardsFromView(view)
        for (i in answerValues.indices)
            answers.add(Answer(cards[i], answerValues[i]))
        setCorrectAnswer()
    }

    private fun setCorrectAnswer() {
        val correctAnswerNum = findSecondMax(answers.map { answer -> answer.value })
        answers.forEach { answer ->
            answer.correct = answer.value == correctAnswerNum
        }
    }

    override fun iterator(): Iterator<Answer> {
        return answers.iterator()
    }

    private fun cardsFromView(view: View): List<AnswerCardView> {
        return listOf(
            view.card_left_top,
            view.card_right_top,
            view.card_left_bottom,
            view.card_right_bottom
        )
    }

}