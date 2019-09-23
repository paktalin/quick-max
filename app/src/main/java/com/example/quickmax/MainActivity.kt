package com.example.quickmax

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import android.os.CountDownTimer

class MainActivity : AppCompatActivity() {

    var numberSet = NumberSet(4, 3)
    private var toAcceptAnswer = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addRadioButtons()
        setTimer()
    }

    private fun setTimer() {
        object : CountDownTimer(3000, 500) {

            override fun onTick(millisUntilFinished: Long) {
                tv_time_left.text = (millisUntilFinished / 1000.0).toString()
            }

            override fun onFinish() {
                tv_time_left.text = "OVER"
            }
        }.start()
    }

    private fun addRadioButtons() {
        for (n in numberSet) {
            val radioButton = RadioButton(this)
            radioButton.id = View.generateViewId()
            radioButton.text = n.toString()
            radioButton.setOnClickListener { processAnswer(n) }
            radio_group.addView(radioButton)
        }
    }

    private fun processAnswer(answer: Int) {
        if (!toAcceptAnswer)
            return
        toAcceptAnswer = false
        val responseFragment:Fragment = if (numberSet.isCorrect(answer)) {
            ResponseCorrectFragment.newInstance()
        } else
            ResponseWrongFragment.newInstance()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_layout, responseFragment)
            .commit()
    }
}
