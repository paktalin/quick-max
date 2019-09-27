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

        if (savedInstanceState == null) retrieveSharedPrefs()
        else restoreInstanceState(savedInstanceState)

        cards = mapOf(2 to card_2_digits, 3 to card_3_digits, 4 to card_4_digits)
        cards.values.forEach { card ->
            card.setOnClickListener { card.isChecked = true }
            card.setOnCheckedChangeListener(checkedChangeListener)
        }
        cards.getValue(numDigits).isChecked = true

        seek_bar.apply {
            setMinStartValue(secToSolve.toFloat()).apply()
            setOnSeekbarChangeListener { n ->
                seek_bar_value.text = resources.getString(R.string.time_to_solve, n.toString())
                secToSolve = n.toInt()
            }
        }

        btn_play.setOnClickListener(playClickListener)
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

    private val checkedChangeListener = MaterialCardView.OnCheckedChangeListener { card, isChecked ->
        if (isChecked && card != cards.getValue(numDigits)) {
            numDigits = getTextView(card).text.toString().toInt()
            cards.values.forEach { c ->
                if (c != card) c.isChecked = false
            }
        }
    }

    private val playClickListener = View.OnClickListener {
        val intent = Intent(this, TaskActivity::class.java).also { i ->
            i.putExtra("num_digits", numDigits)
            i.putExtra("sec_to_solve", secToSolve)
        }
        startActivity(intent)
    }

    override fun onStop() {
        super.onStop()
        saveSelectedValues()
    }

    private fun saveSelectedValues() {
        val editor = getSharedPreferences("my_prefs", Context.MODE_PRIVATE).edit()
        editor.putInt("sec_to_solve", secToSolve)
        editor.putInt("num_digits", numDigits)
        editor.apply()
    }

    private fun retrieveSharedPrefs() {
        val prefs = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        secToSolve = prefs.getInt("sec_to_solve", 4)
        numDigits = prefs.getInt("num_digits", 3)
    }
}