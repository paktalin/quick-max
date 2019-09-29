package com.paktalin.quickmax.task.model

import com.paktalin.quickmax.task.ui.AnswerCardView

class Answer(val card: AnswerCardView, val value: Int) {
    var correct: Boolean = false
}