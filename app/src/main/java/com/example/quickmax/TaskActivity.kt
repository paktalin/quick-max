package com.example.quickmax

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.quickmax.answers.AnswerSet
import kotlinx.android.synthetic.main.activity_task.*

class TaskActivity : AppCompatActivity() {

    private lateinit var answerSet: AnswerSet
    private val timeToSolve:Long = 4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        answerSet = AnswerSet(3)
        setUpAnswerButtons()
        timer.start()
        startProgressBarAnimation()
    }

    private fun setUpAnswerButtons() {
        for (answer in answerSet) {
            (findViewById<CardView>(answer.buttonId).getChildAt(0) as TextView).text = answer.value.toString()
            findViewById<CardView>(answer.buttonId).setOnClickListener { processAnswer(answer.correct) }
        }
    }

    private fun processAnswer(correct: Boolean) {
        timer.cancel()
        makeButtonsUncheckable()

        val responseFragment = ResponseFragment.newInstance().also {
                    f -> f.arguments = Bundle().also {
                    b -> b.putBoolean("correct", correct)  } }

        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_layout, responseFragment, "response")
            .commitAllowingStateLoss()
    }

    private fun makeButtonsUncheckable() {
        for (answer in answerSet) {
            findViewById<CardView>(answer.buttonId).isClickable = false
        }
    }

    private fun startProgressBarAnimation() {
        val colorFrom = Color.TRANSPARENT
        val colorTo = ContextCompat.getColor(this, R.color.transparent_red)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = timeToSolve
        colorAnimation.addUpdateListener { animator ->
            layout_gradient.background.setColorFilter(animator.animatedValue as Int,
                android.graphics.PorterDuff.Mode.SRC_ATOP)
        }
        colorAnimation.start()
    }

    fun reload() {
        val intent = intent
        finish()
        startActivity(intent)
    }

    private val timer = object : CountDownTimer(timeToSolve, 1000) {
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
