package com.paktalin.quickmax.task.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.paktalin.quickmax.MainActivity
import com.paktalin.quickmax.R
import com.paktalin.quickmax.addButtonNextFragment
import com.paktalin.quickmax.removeButtonNextFragment
import com.paktalin.quickmax.task.model.AnswerSet
import kotlinx.android.synthetic.main.activity_task.*

// TODO save state
class TaskActivity : AppCompatActivity() {

    private var millisToSolve: Long = 4000
    private var numDigits: Int = 3

    private lateinit var timerFragment: TimerFragment
    private lateinit var answersFragment: AnswersFragment

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

        if (savedInstanceState != null) {
            restoreState(savedInstanceState)
        } else {
            retrieveExtras()
            timerFragment = TimerFragment()
                .apply { arguments = Bundle().apply { putLong("millis_to_solve", millisToSolve) } }
                .apply { supportFragmentManager.commit(true) { add(R.id.container_timer, this@apply, "timer_fragment")} }

            answersFragment = AnswersFragment().apply {
                arguments = Bundle().apply { putInt("num_digits", numDigits) }
            }
            supportFragmentManager.commit(true) {
                replace(R.id.container_answers, answersFragment, "answers_fragment")
            }
            startNewRound()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        supportFragmentManager.putFragment(outState, "timer_fragment", timerFragment)
        supportFragmentManager.putFragment(outState, "answers_fragment", answersFragment)
    }

    internal fun startNewRound() {
        timerFragment.startNewRound()
        answersFragment.startNewRound()
        removeButtonNextFragment(supportFragmentManager)
    }

    private fun restoreState(savedInstanceState: Bundle) {
        timerFragment = supportFragmentManager.getFragment(
            savedInstanceState,
            "timer_fragment"
        ) as TimerFragment
        answersFragment = supportFragmentManager.getFragment(savedInstanceState, "answers_fragment") as AnswersFragment
    }

    private fun retrieveExtras() {
        numDigits = intent.getIntExtra("num_digits", 3)
        millisToSolve = 1000 * intent.getIntExtra("sec_to_solve", 4).toLong()
    }

    fun onResponseWrong() {
        timerFragment.cancel(State.WRONG)
        addButtonNextFragment(supportFragmentManager, false)
    }

    fun onResponseCorrect() {
        timerFragment.cancel(State.CORRECT)
        addButtonNextFragment(supportFragmentManager, true)
    }

    fun onTimeOver() {
        addButtonNextFragment(supportFragmentManager, false)
        answersFragment.disableCards()
    }
}
