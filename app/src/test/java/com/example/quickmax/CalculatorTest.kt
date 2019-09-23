package com.example.quickmax

import org.junit.Test

import org.junit.Assert.*

class CalculatorTest {

    @Test
    fun test_generate_random_with_3_digits() {
        val random = generateRandom(3)
        assertTrue(random >= 100)
        assertTrue(random <= 999)
    }

    @Test
    fun test_generate_random_with_5_digits() {
        val random = generateRandom(5)
        assertTrue(random >= 10000)
        assertTrue(random <= 99999)
    }

    @Test
    fun test_find_second_max() {
        val numbers = mutableListOf(333, 543, 876, 243)
        assertEquals(543, findSecondMax(numbers))
    }
}