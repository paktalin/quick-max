package com.example.quickmax

import org.junit.Test

import org.junit.Assert.*

class AnswerSetTest {

    private val numOptions = 4
    private val numDigits = 3
    private val numberSet = AnswerSet(numOptions, numDigits)

    @Test
    fun test_constructor() {
        assertEquals(numOptions, numberSet.numbers.size)
        numberSet.numbers.forEach { n -> n in (100..999) }
    }

    @Test
    fun isCorrect() {
        val secondMax = findSecondMax(numberSet.numbers)
        assertTrue(numberSet.isCorrect(secondMax))
    }
}