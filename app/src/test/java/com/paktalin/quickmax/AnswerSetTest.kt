package com.paktalin.quickmax

import com.paktalin.quickmax.answers.AnswerSet
import com.paktalin.quickmax.answers.findSecondMax
import org.junit.Test

import org.junit.Assert.*

class AnswerSetTest {

    private val numOptions = 4
    private val numDigits = 3
    private val numberSet = AnswerSet(numOptions, numDigits)

    @Test
    fun test_constructor() {
        assertEquals(numOptions, numberSet.answers.size)
        numberSet.answers.forEach { n -> n in (100..999) }
    }

    @Test
    fun isCorrect() {
        val secondMax = findSecondMax(numberSet.answers)
        assertTrue(numberSet.isCorrect(secondMax))
    }
}