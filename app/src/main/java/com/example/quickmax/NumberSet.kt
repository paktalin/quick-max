package com.example.quickmax


class NumberSet(numOptions: Int, numDigits: Int): Iterable<Int> {

    val numbers: MutableList<Int> = MutableList(numOptions) { generateRandom(numDigits) }
    private val correct: Int = findSecondMax(numbers)

    override fun iterator(): Iterator<Int> {
        return numbers.iterator()
    }

    fun isCorrect(number: Int) : Boolean {
        return number == correct
    }
}