package com.paktalin.quickmax

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.CountDownTimer
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.paktalin.quickmax.answers.Answer
import com.paktalin.quickmax.answers.AnswerSet
import kotlinx.android.synthetic.main.activity_task.*

// TODO save state
class TaskActivity : AppCompatActivity() {

    internal lateinit var answerSet: AnswerSet
    private var millisToSolve: Long = 4000
    private var numDigits: Int = 3
    private lateinit var timer: CountDownTimer
    private lateinit var colorAnimation: ValueAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        btn_back.setOnClickListener { startActivity(Intent(this@TaskActivity, MainActivity::class.java)) }
        retrieveExtras()
        initTimer()
        startNewRound()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
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
        setUpCards()
        timer.start()
        startColorAnimation()
    }

    private fun retrieveExtras() {
        numDigits = intent.getIntExtra("num_digits", 3)
        millisToSolve = 1000 * intent.getIntExtra("sec_to_solve", 4).toLong()
    }

    private fun setUpCards() {
        answerSet.forEach { answer ->
            answer.card.setOnCheckedChangeListener { _, isChecked -> if (isChecked) processAnswer(answer) }
            answer.card.initial(this@TaskActivity, answer.value)
        }
    }

    private fun processAnswer(answer: Answer) {
        timer.cancel()
        colorAnimation.cancel()
        setResponseText(answer)
        answerSet.forEach { answer -> answer.card.disable()}
    }

    private fun setResponseText(answer: Answer) {
        tv_response.setTextSize(TypedValue.COMPLEX_UNIT_SP, resources.getDimension(R.dimen.response_text_size))

        if (answer.correct) {
            answer.card.markCorrect(this@TaskActivity)
            tv_response.text = resources.getString(R.string.response_correct)
            supportFragmentManager.commit(true) {
                add(R.id.main_layout, ButtonNextFragment().apply { arguments = Bundle().apply { putBoolean("correct", true) } })
            }
        } else {
            answer.card.markWrong(this@TaskActivity)
            tv_response.text = resources.getString(R.string.response_wrong)
            supportFragmentManager.commit(true) {
                add(R.id.main_layout, ButtonNextFragment().apply { arguments = Bundle().apply { putBoolean("correct", false) } })
            }
        }
    }

    private fun startColorAnimation() {
        val colorFrom = Color.TRANSPARENT
        val colorTo = color(this, R.color.transparent_red)
        colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
            .apply { duration = millisToSolve }
        colorAnimation.addUpdateListener { animator ->
            layout_gradient.background.setColorFilter(animator.animatedValue as Int,
                PorterDuff.Mode.SRC_ATOP)
        }
        colorAnimation.start()
    }

    private fun initTimer() {
        timer = object : CountDownTimer(millisToSolve, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tv_response.text = (millisUntilFinished/1000).toString()
            }

            override fun onFinish() {
                tv_response.setTextSize(TypedValue.COMPLEX_UNIT_SP, resources.getDimension(R.dimen.response_text_size))
                tv_response.text = resources.getString(R.string.time_is_over)

                supportFragmentManager.commit(true) {
                    add(R.id.main_layout, ButtonNextFragment().apply { arguments = Bundle().apply { putBoolean("correct", false) } })
                }
                answerSet.forEach { answer -> answer.card.disable()}
            }
        }
    }
}
