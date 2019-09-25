package com.example.quickmax

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        retrieveSharedPrefs()
        seek_bar.setMinStartValue(secToSolve.toFloat()).apply()

        seek_bar.setOnSeekbarChangeListener { n ->
            seek_bar_value.text = resources.getString(R.string.time_to_solve, n.toString())
            secToSolve = n.toInt()
        }

        card_2_digits.setOnClickListener(cardOnClickListener)
        card_3_digits.setOnClickListener(cardOnClickListener)
        card_4_digits.setOnClickListener(cardOnClickListener)

        btn_play.setOnClickListener(playClickListener)
    }

    private val cardOnClickListener = View.OnClickListener { card ->
        val cardM = card as MaterialCardView
        if (!cardM.isChecked) {
            cardM.isChecked = true
            numDigits = getTextView(card).text.toString().toInt()
            listOf(card_2_digits, card_3_digits, card_4_digits).forEach { c ->
                if (c.id != cardM.id)
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
        editor.apply()
    }

    private fun retrieveSharedPrefs() {
        val prefs = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        secToSolve = prefs.getInt("sec_to_solve", 4)
        checkedCard = findViewById(prefs.getInt("checked_num_id", R.id.card_3_digits))
        checkedCard.isChecked = true
    }
}