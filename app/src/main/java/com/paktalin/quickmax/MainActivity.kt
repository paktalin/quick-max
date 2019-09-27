package com.paktalin.quickmax

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var secToSolve: Int = 4
    private var numDigits: Int = 3
    private lateinit var cards: Map<Int, MaterialCardView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // restore data
        if (savedInstanceState == null) retrieveSharedPrefs()
        else restoreInstanceState(savedInstanceState)

        // set up cards
        cards = mapOf(2 to card_2_digits, 3 to card_3_digits, 4 to card_4_digits)
        cards.values.forEach { card ->
            card.setOnClickListener { card.isChecked = true }
            card.setOnCheckedChangeListener { card, isChecked -> updateCards(card, isChecked) }
        }
        cards.getValue(numDigits).isChecked = true

        // set up seek bar
        seek_bar.apply {
            setMinStartValue(secToSolve.toFloat()).apply()
            setOnSeekbarChangeListener { n ->
                seek_bar_value.text = resources.getString(R.string.time_to_solve, n.toString())
                secToSolve = n.toInt()
            }
        }

        // set up button play
        btn_play.setOnClickListener { startTaskActivity() }
    }

    private fun restoreInstanceState(savedInstanceState: Bundle) {
        secToSolve = savedInstanceState.getInt("sec_to_solve")
        numDigits = savedInstanceState.getInt("num_digits")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("num_digits", numDigits)
        outState.putInt("sec_to_solve", secToSolve)
    }

    private fun updateCards(card: MaterialCardView, isChecked: Boolean) {
        if (isChecked && card != cards.getValue(numDigits)) {
            numDigits = getTextView(card).text.toString().toInt()
            cards.values.forEach { c ->
                if (c != card) c.isChecked = false
            }
        }
    }

    private fun startTaskActivity() {
        val intent = Intent(this, TaskActivity::class.java)
            .apply { putExtra("num_digits", numDigits) }
            .apply { putExtra("sec_to_solve", secToSolve) }
        startActivity(intent)
    }

    override fun onStop() {
        super.onStop()
        // save to shared preferences
        getSharedPreferences("my_prefs", Context.MODE_PRIVATE).edit()
            .apply { putInt("sec_to_solve", secToSolve) }
            .apply { putInt("num_digits", numDigits) }
            .apply()
    }

    private fun retrieveSharedPrefs() {
        val prefs = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        secToSolve = prefs.getInt("sec_to_solve", 4)
        numDigits = prefs.getInt("num_digits", 3)
    }
}