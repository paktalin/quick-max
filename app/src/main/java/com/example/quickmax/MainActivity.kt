package com.example.quickmax

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seek_bar.setOnSeekbarChangeListener {
            n -> seek_bar_value.text = resources.getString(R.string.time_to_solve, n.toString())
        }
        seek_bar.setMinStartValue(4f).apply()

        card_2_digits.setOnClickListener(cardOnClickListener)
        card_3_digits.setOnClickListener(cardOnClickListener)
        card_4_digits.setOnClickListener(cardOnClickListener)
    }

    private val cardOnClickListener = View.OnClickListener { card ->
        val numDigits = ((card as CardView).getChildAt(0) as TextView).text.toString().toInt()
        val secToSolve = seek_bar.selectedMinValue.toInt()

        val intent = Intent(this, TaskActivity::class.java).also { i ->
            i.putExtra("num_digits", numDigits)
            i.putExtra("sec_to_solve", secToSolve)
        }
        startActivity(intent)
    }
}