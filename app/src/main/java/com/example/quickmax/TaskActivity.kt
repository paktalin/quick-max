package com.example.quickmax

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.CountDownTimer
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.quickmax.answers.Answer
import com.example.quickmax.answers.AnswerSet
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
        answerSet = AnswerSet(numDigits, listOf(card_left_top, card_right_top, card_left_bottom, card_right_bottom))
        setUpButtons()
        timer.start()
        startProgressBarAnimation()
    }

    fun reload() {
        val intent = intent
        finish()
        startActivity(intent)
    }

    private fun retrieveExtras() {
        numDigits = intent.getIntExtra("num_digits", 3)
        millisToSolve = 1000 * intent.getIntExtra("sec_to_solve", 4).toLong()
    }

    private fun setUpButtons() {
        for (answer in answerSet) {
            (answer.card.getChildAt(0) as TextView).text = answer.value.toString()
            (answer.card.getChildAt(0) as TextView).setTextColor(color(R.color.transparent_black))
            answer.card.setOnClickListener { processAnswer(answer) }
            answer.card.background.clearColorFilter()
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
        makeButtonsUncheckable()
    }

    private fun setResponseText(answer: Answer) {
        tv_timer.setTextSize(TypedValue.COMPLEX_UNIT_SP, resources.getDimension(R.dimen.response_text_size))
        btn_next.visibility = View.VISIBLE

        if (answer.correct) {
            tv_timer.text = resources.getString(R.string.response_correct)
            answer.card.background.setColorFilter(color(R.color.colorAccent), PorterDuff.Mode.MULTIPLY)
            btn_next.background.setColorFilter(color(R.color.colorAccent), PorterDuff.Mode.MULTIPLY)
            btn_next.setTextColor(color(R.color.transparent_dark_black))
        } else {
            tv_timer.text = resources.getString(R.string.response_wrong)
            answer.card.background.setColorFilter(color(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY)
            btn_next.background.setColorFilter(color(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY)
            btn_next.setTextColor(Color.WHITE)
            (answer.card.getChildAt(0) as TextView).setTextColor(Color.WHITE)
        }
    }

    private fun makeButtonsUncheckable() {
        for (answer in answerSet) {
            answer.card.isClickable = false
        }
    }

    private fun startProgressBarAnimation() {
        val colorFrom = Color.TRANSPARENT
        val colorTo = color(R.color.transparent_red)
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
                tv_timer.text = (millisUntilFinished/1000).toString()
            }

            override fun onFinish() {
                tv_timer.setTextSize(TypedValue.COMPLEX_UNIT_SP, resources.getDimension(R.dimen.response_text_size))
                tv_timer.text = resources.getString(R.string.time_is_over)
                btn_next.background.setColorFilter(color(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY)
                btn_next.setTextColor(Color.WHITE)
                btn_next.visibility = View.VISIBLE
            }
        }
    }

    private fun color(id: Int): Int {
        return ContextCompat.getColor(this, id)
    }
}
