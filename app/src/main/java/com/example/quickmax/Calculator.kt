package com.example.quickmax

import kotlin.math.pow

fun generateRandom(numDigits: Int): Int {
    val start = 10.0.pow(numDigits-1).toInt()
    val end = 10.0.pow(numDigits).toInt() - 1
    return (start..end).shuffled().first()
}

fun findSecondMax(numbers: List<Int>): Int {
    var firstMax = 0
    var secondMax = 0

    numbers.forEach { n ->
        if (n > firstMax) {
            secondMax = firstMax; firstMax = n
        } else if (n > secondMax) {
            secondMax = n
        }
    }
    return secondMax
}