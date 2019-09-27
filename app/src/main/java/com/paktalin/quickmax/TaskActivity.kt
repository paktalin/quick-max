package com.paktalin.quickmax

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.CountDownTimer
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.paktalin.quickmax.answers.Answer
import com.paktalin.quickmax.answers.AnswerSet
import kotlinx.android.synthetic.main.activity_task.*

class TaskActivity : AppCompatActivity() {

    private lateinit var answerSet: AnswerSet
    private var millisToSolve: Long = 4000
    private var numDigits: Int = 3
    private lateinit var timer: CountDownTimer
    private lateinit var colorAnimation: ValueAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        retrieveExtras()
        initTimer()
        startNewRound()
    }

    private fun startNewRound() {
        answerSet = AnswerSet(
            numDigits,
            listOf(card_left_top, card_right_top, card_left_bottom, card_right_bottom)
        )
        setUpCards()
        timer.start()
        startProgressBarAnimation()
    }

    private fun retrieveExtras() {
        numDigits = intent.getIntExtra("num_digits", 3)
        millisToSolve = 1000 * intent.getIntExtra("sec_to_solve", 4).toLong()
    }

    private fun setUpCards() {
        answerSet.forEach {answer ->
            answer.card.setOnClickListener { answer.card.isChecked = true }
            answer.card.setOnCheckedChangeListener { _, isChecked -> if (isChecked) processAnswer(answer) }
            getTextView(answer.card).text = answer.value.toString()
            answer.card.clean(this@TaskActivity)
        }
        btn_back.setOnClickListener { startActivity(Intent(this@TaskActivity, MainActivity::class.java)) }
        btn_next.apply {
            setOnClickListener { startNewRound() }
            visibility = View.INVISIBLE
        }
    }

    private fun processAnswer(answer: Answer) {
        timer.cancel()
        colorAnimation.cancel()
        setResponseText(answer)
        disableCards()
    }

    private fun setResponseText(answer: Answer) {
        tv_response.setTextSize(TypedValue.COMPLEX_UNIT_SP, resources.getDimension(R.dimen.response_text_size))
        btn_next.visibility = View.VISIBLE

        if (answer.correct) {
            answer.card.markCorrect(this@TaskActivity)
            tv_response.text = resources.getString(R.string.response_correct)
            btn_next.backgroundTintList = ContextCompat.getColorStateList(this,
                R.color.colorAccent
            )
            btn_next.setTextColor(
                color(
                    this,
                    R.color.transparent_dark_black
                )
            )
        } else {
            answer.card.markWrong(this@TaskActivity)
            tv_response.text = resources.getString(R.string.response_wrong)
            btn_next.backgroundTintList = ContextCompat.getColorStateList(this,
                R.color.colorPrimary
            )
            btn_next.setTextColor(Color.WHITE)
        }
    }

    private fun disableCards() {
        answerSet.forEach { answer -> answer.card.disable()}
    }

    private fun startProgressBarAnimation() {
        val colorFrom = Color.TRANSPARENT
        val colorTo =
            color(this, R.color.transparent_red)
        colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = millisToSolve
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
                btn_next.background.setColorFilter(
                    color(
                        this@TaskActivity,
                        R.color.colorPrimary
                    ), PorterDuff.Mode.MULTIPLY)
                btn_next.setTextColor(Color.WHITE)
                btn_next.visibility = View.VISIBLE
                disableCards()
            }
        }
    }
}