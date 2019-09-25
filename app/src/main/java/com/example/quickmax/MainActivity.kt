package com.example.quickmax

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity() {

    private var secToSolve: Int = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        secToSolve = retrieveSecondsToSolve()
        seek_bar.setMinStartValue(secToSolve.toFloat()).apply()

        seek_bar.setOnSeekbarChangeListener {
            n -> seek_bar_value.text = resources.getString(R.string.time_to_solve, n.toString())
            secToSolve = n.toInt()
        }

        card_2_digits.setOnClickListener(cardOnClickListener)
        card_3_digits.setOnClickListener(cardOnClickListener)
        card_4_digits.setOnClickListener(cardOnClickListener)
    }

    private val cardOnClickListener = View.OnClickListener { card ->
        val numDigits = ((card as MaterialCardView).getChildAt(0) as TextView).text.toString().toInt()

        saveSelectedValues()

        val intent = Intent(this, TaskActivity::class.java).also { i ->
            i.putExtra("num_digits", numDigits)
            i.putExtra("sec_to_solve", secToSolve)
        }
        startActivity(intent)
    }

    private fun saveSelectedValues() {
        val editor = getSharedPreferences("my_prefs", Context.MODE_PRIVATE).edit()
        editor.putInt("sec_to_solve", secToSolve)
        editor.apply()
    }

    private fun retrieveSecondsToSolve(): Int {
        val prefs = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        return prefs.getInt("sec_to_solve", 4)
    }
}