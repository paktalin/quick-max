package com.example.quickmax

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var answerSet: AnswerSet
    private val timeToSolve = 4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        answerSet = AnswerSet(3)
        setUpAnswerButtons()
        timer.start()
    }

    private fun setUpAnswerButtons() {
        for (answer in answerSet) {
            (findViewById<CardView>(answer.buttonId).getChildAt(0) as TextView).text = answer.value.toString()
            findViewById<CardView>(answer.buttonId).setOnClickListener { processAnswer(answer.correct) }
        }
    }

    private fun processAnswer(correct: Boolean) {
        timer.cancel()
        makeRadioButtonsUncheckable()

        val responseFragment = ResponseFragment.newInstance().also {
                    f -> f.arguments = Bundle().also {
                    b -> b.putBoolean("correct", correct)  } }

        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_layout, responseFragment, "response")
            .commitAllowingStateLoss()
    }

    private fun makeRadioButtonsUncheckable() {
        for (answer in answerSet) {
            findViewById<CardView>(answer.buttonId).isClickable = false
        }
    }

    fun reload() {
        val intent = intent
        finish()
        startActivity(intent)
    }

    private val timer = object : CountDownTimer(timeToSolve.toLong(), 100) {
        override fun onTick(millisUntilFinished: Long) {
            val progress = ((timeToSolve - millisUntilFinished).toFloat() / timeToSolve) * 100
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                bar_time_left.setProgress(progress.toInt(), true)
            else
                bar_time_left.progress = progress.toInt()
        }

        override fun onFinish() {
            makeRadioButtonsUncheckable()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_layout, TimeIsOverFragment.newInstance(), "time_is_over")
                .commitAllowingStateLoss()
        }
    }
}
