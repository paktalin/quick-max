package com.example.quickmax

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var answerSet: AnswerSet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        answerSet = AnswerSet(3)
        setUpAnswerButtons()
        timer.start()
    }

    private fun setUpAnswerButtons() {
        for (answer in answerSet) {
            findViewById<Button>(answer.buttonId).text = answer.value.toString()
            findViewById<Button>(answer.buttonId).setOnClickListener { processAnswer(answer.correct) }
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
            findViewById<Button>(answer.buttonId).isClickable = false
        }
    }

    fun reload() {
        val intent = intent
        finish()
        startActivity(intent)
    }

    private val timer = object : CountDownTimer(4000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            tv_time_left.text = (millisUntilFinished / 1000).toString()
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
