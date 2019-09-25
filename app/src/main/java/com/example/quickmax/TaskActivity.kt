package com.example.quickmax

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.TypedValue
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.quickmax.answers.Answer
import com.example.quickmax.answers.AnswerSet
import kotlinx.android.synthetic.main.activity_task.*

class TaskActivity : AppCompatActivity() {

    private lateinit var answerSet: AnswerSet
    private var millisToSolve: Long = 4000
    private lateinit var timer: CountDownTimer
    private lateinit var colorAnimation: ValueAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        val numDigits = intent.getIntExtra("num_digits", 3)
        millisToSolve = 1000 * intent.getIntExtra("sec_to_solve", 4).toLong()
        initTimer()
        answerSet = AnswerSet(numDigits, listOf(card_left_top, card_right_top, card_left_bottom, card_right_bottom))
        setUpAnswerButtons()
        timer.start()
        startProgressBarAnimation()
        btn_back.setOnClickListener { startActivity(Intent(this@TaskActivity, MainActivity::class.java)) }
    }

    fun reload() {
        val intent = intent
        finish()
        startActivity(intent)
    }

    private fun setUpAnswerButtons() {
        for (answer in answerSet) {
            (answer.card.getChildAt(0) as TextView).text = answer.value.toString()
            answer.card.setOnClickListener { processAnswer(answer) }
        }
    }

    private fun processAnswer(answer: Answer) {
        timer.cancel()
        colorAnimation.cancel()
        setResponseText(answer)
        makeButtonsUncheckable()
        startResponseFragment(answer)
    }

    private fun setResponseText(answer: Answer) {
        tv_timer.setTextSize(TypedValue.COMPLEX_UNIT_SP, resources.getDimension(R.dimen.response_text_size))
        if (answer.correct) {
            tv_timer.text = resources.getString(R.string.response_correct)
            answer.card.setCardBackgroundColor(resources.getColor(R.color.colorAccent))
        } else {
            tv_timer.text = resources.getString(R.string.response_wrong)
            answer.card.setCardBackgroundColor(resources.getColor(R.color.colorPrimary))
            (answer.card.getChildAt(0) as TextView).setTextColor(Color.WHITE)
        }
    }

    private fun startResponseFragment(answer: Answer) {
        val responseFragment = ResponseFragment.newInstance().also {
                f -> f.arguments = Bundle().also {
                b -> b.putBoolean("correct", answer.correct)  } }

        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_layout, responseFragment, "response")
            .commitAllowingStateLoss()
    }

    private fun makeButtonsUncheckable() {
        for (answer in answerSet) {
            answer.card.isClickable = false
        }
    }

    private fun startProgressBarAnimation() {
        val colorFrom = Color.TRANSPARENT
        val colorTo = ContextCompat.getColor(this, R.color.transparent_red)
        colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = millisToSolve
        colorAnimation.addUpdateListener { animator ->
            layout_gradient.background.setColorFilter(animator.animatedValue as Int,
                android.graphics.PorterDuff.Mode.SRC_ATOP)
        }
        colorAnimation.start()
    }

    private fun initTimer() {
        timer = object : CountDownTimer(millisToSolve, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tv_timer.text = (millisUntilFinished/1000).toString()
            }

            override fun onFinish() {
                cv_task.elevation = 0f
                makeButtonsUncheckable()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_layout, TimeIsOverFragment.newInstance(), "time_is_over")
                    .commitAllowingStateLoss()
            }
        }
    }
}
