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
    private var isReady: Boolean = false

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

        if (savedInstanceState != null)
            restoreState(savedInstanceState)
        else
            answerSet = AnswerSet(numDigits, mView)
        isReady = true
        startNewRound()
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray("answers", answerSet.answers.map { answer -> answer.value }.toIntArray())
    }

    fun startNewRound() {
        if (isReady) {
            answerSet.forEach { answer ->
                answer.card
                    .apply { setOnCheckedChangeListener { _, isChecked -> if (isChecked) processAnswer(answer) } }
                    .apply { initial(context!!, answer.value) }
            }
        }
    }

    private fun restoreState(savedInstanceState: Bundle) {
        val restoredAnswers = savedInstanceState.getIntArray("answers")!!
        answerSet = AnswerSet(restoredAnswers, mView)
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