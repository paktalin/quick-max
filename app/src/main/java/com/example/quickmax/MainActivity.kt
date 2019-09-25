package com.example.quickmax

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seek_bar.setOnSeekbarChangeListener {
            n -> seek_bar_value.text = resources.getString(R.string.time_to_solve, n.toString())
        }


        seek_bar.setMinStartValue(4f).apply()
    }
}