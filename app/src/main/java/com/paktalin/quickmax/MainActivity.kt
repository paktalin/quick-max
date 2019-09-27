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
    private lateinit var checkedCard: MaterialCardView
    private lateinit var cards: List<MaterialCardView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) retrieveSharedPrefs()
        else restoreInstanceState(savedInstanceState)

        checkedCard.isChecked = true

        seek_bar.apply {
            setMinStartValue(secToSolve.toFloat()).apply()
            setOnSeekbarChangeListener { n ->
                seek_bar_value.text = resources.getString(R.string.time_to_solve, n.toString())
                secToSolve = n.toInt()
            }
        }

        cards = listOf(card_2_digits, card_3_digits, card_4_digits)
        cards.forEach { card -> card.setOnClickListener(cardOnClickListener) }

        btn_play.setOnClickListener(playClickListener)
    }

    private fun restoreInstanceState(savedInstanceState: Bundle) {
        checkedCard = findViewById(savedInstanceState.getInt("checked_num_id"))
        secToSolve = savedInstanceState.getInt("sec_to_solve")
        numDigits = savedInstanceState.getInt("num_digits")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("num_digits", numDigits)
        outState.putInt("sec_to_solve", secToSolve)
        outState.putInt("checked_num_id", checkedCard.id)
    }

    private val cardOnClickListener = View.OnClickListener { card ->
        val cardM = card as MaterialCardView
        if (!cardM.isChecked) {
            checkedCard = cardM
            checkedCard.isChecked = true
            numDigits = numDigitsFromCard(checkedCard)
            cards.forEach { c ->
                if (c.id != checkedCard.id)
                    c.isChecked = false
            }
        }
    }

    private val playClickListener = View.OnClickListener {
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
        editor.putInt("checked_num_id", checkedCard.id)
        editor.apply()
    }

    private fun retrieveSharedPrefs() {
        val prefs = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        secToSolve = prefs.getInt("sec_to_solve", 4)
        checkedCard = findViewById(
            prefs.getInt(
                "checked_num_id",
                R.id.card_3_digits
            )
        )
        numDigits = numDigitsFromCard(checkedCard)
    }

    private fun numDigitsFromCard(card: MaterialCardView): Int {
        return getTextView(card).text.toString().toInt()
    }
}