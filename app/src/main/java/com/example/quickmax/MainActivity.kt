package com.example.quickmax

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var numberSet = NumberSet(4, 3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addRadioButtons()
        timer.start()
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
        timer.cancel()
        makeRadioButtonsUncheckable()

        val responseFragment: Fragment = if (numberSet.isCorrect(answer)) {
            ResponseCorrectFragment.newInstance()
        } else
            ResponseWrongFragment.newInstance()

        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_layout, responseFragment, "response")
            .commitAllowingStateLoss()
    }

    private fun makeRadioButtonsUncheckable() {
        for (i in 0 until radio_group.childCount) {
            radio_group[i].isClickable = false
        }
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
