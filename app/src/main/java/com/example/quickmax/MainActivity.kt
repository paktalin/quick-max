package com.example.quickmax

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var answerSet: AnswerSet
    private val timeToSolve:Long = 4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        val colorFrom = ContextCompat.getColor(this, R.color.gradient_dark)
        val colorTo = ContextCompat.getColor(this, R.color.red)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = timeToSolve
        colorAnimation.addUpdateListener { animator ->
            progress_bar.progressDrawable.setColorFilter(animator.animatedValue as Int,
                android.graphics.PorterDuff.Mode.SRC_ATOP)
        }
        colorAnimation.start()
    }

    fun reload() {
        val intent = intent
        finish()
        startActivity(intent)
    }

    private val timer = object : CountDownTimer(timeToSolve, 100) {
        override fun onTick(millisUntilFinished: Long) {
            val progress = ((timeToSolve - millisUntilFinished).toFloat() / timeToSolve) * 100
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                progress_bar.setProgress(progress.toInt(), true)
            else
                progress_bar.progress = progress.toInt()
        }

        override fun onFinish() {
            makeButtonsUncheckable()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_layout, TimeIsOverFragment.newInstance(), "time_is_over")
                .commitAllowingStateLoss()
        }
    }
}
