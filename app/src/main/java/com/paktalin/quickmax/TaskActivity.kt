package com.paktalin.quickmax

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.paktalin.quickmax.answers.Answer
import com.paktalin.quickmax.answers.AnswerSet
import kotlinx.android.synthetic.main.activity_task.*

// TODO save state
class TaskActivity : AppCompatActivity() {

    private lateinit var answerSet: AnswerSet
    private var millisToSolve: Long = 4000
    private var numDigits: Int = 3

    private lateinit var timerFragment: TimerFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        btn_back.setOnClickListener {
            startActivity(
                Intent(
                    this@TaskActivity,
                    MainActivity::class.java
                )
            )
        }

        if (savedInstanceState != null)
            timerFragment = supportFragmentManager.getFragment(savedInstanceState, "timer_fragment") as TimerFragment
        else {
            timerFragment = TimerFragment().apply {
                arguments = Bundle().apply { putLong("millis_to_solve", millisToSolve) }
            }
            retrieveExtras()
            startNewRound()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        supportFragmentManager.putFragment(outState, "timer_fragment", timerFragment!!)
        // TODO save selection
        // TODO save animation state
        // TODO save timer
        // TODO save button next state
        // TODO save
    }

    internal fun startNewRound() {
        answerSet = AnswerSet(
            numDigits,
            listOf(card_left_top, card_right_top, card_left_bottom, card_right_bottom)
        )
        supportFragmentManager.commit(true) {
            replace(R.id.fragment_timer, timerFragment, "timer_fragment")
        }
        setUpCards()
        removeButtonNextFragment(supportFragmentManager)
    }

    private fun retrieveExtras() {
        numDigits = intent.getIntExtra("num_digits", 3)
        millisToSolve = 1000 * intent.getIntExtra("sec_to_solve", 4).toLong()
    }

    private fun setUpCards() {
        answerSet.forEach { answer ->
            answer.card.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) processAnswer(answer)
            }
            answer.card.initial(this@TaskActivity, answer.value)
        }
    }

    private fun processAnswer(answer: Answer) {
        if (answer.correct) {
            answer.card.markCorrect(this@TaskActivity)
            timerFragment.cancel(State.CORRECT)
            addButtonNextFragment(supportFragmentManager, true)
        } else {
            answer.card.markWrong(this@TaskActivity)
            timerFragment.cancel(State.WRONG)
            addButtonNextFragment(supportFragmentManager, false)
        }
        answerSet.forEach { answer -> answer.card.disable() }
    }

    fun onTimeOver() {
        addButtonNextFragment(supportFragmentManager, false)
//        answerSet.forEach { answer -> answer.card.disable() }
    }
}
