package com.example.quickmax.answers

import com.example.quickmax.MyMaterialCard

class AnswerSet(numDigits: Int, cards: List<MyMaterialCard>): Iterable<Answer> {
    private val numAnswers = 4
    private lateinit var correctAnswer: Answer

    val answers: MutableList<Answer> = mutableListOf()
    init {
        val randomNumbers =
            generateNRandomNumbers(numDigits, numAnswers)
        for (i in 0 until numAnswers)
            answers.add(
                Answer(
                    cards[i],
                    randomNumbers.elementAt(i)
                )
            )

        val correctAnswerNum =
            findSecondMax(answers.map { answer -> answer.value })
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