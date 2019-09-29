package com.paktalin.quickmax.task.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.paktalin.quickmax.R
import com.paktalin.quickmax.addButtonNextFragment
import com.paktalin.quickmax.task.model.Answer
import com.paktalin.quickmax.task.model.AnswerSet
import kotlinx.android.synthetic.main.fragment_answers.view.*

class AnswersFragment : Fragment() {
    private lateinit var answerSet: AnswerSet
    private lateinit var mView: View
    private var numDigits: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        numDigits = arguments!!.getInt("num_digits")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_answers, container, false)
        mView = view
        answerSet = AnswerSet(
            numDigits,
            listOf(view.card_left_top, view.card_right_top, view.card_left_bottom, view.card_right_bottom)
        )
        answerSet.forEach { answer ->
            answer.card.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) processAnswer(answer)
            }
            answer.card.initial(context!!, answer.value)
        }

        return view
    }

    private fun processAnswer(answer: Answer) {
        if (answer.correct) {
            answer.card.markCorrect(context!!)
            (activity as TaskActivity).onResponseCorrect()
        } else {
            answer.card.markWrong(context!!)
            (activity as TaskActivity).onResponseWrong()
        }
        answerSet.forEach { answer -> answer.card.disable() }
    }
}