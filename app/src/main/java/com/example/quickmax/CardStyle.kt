package com.example.quickmax

import android.content.Context
import android.graphics.Color

class CardStyle private constructor(var isCheckable: Boolean,
                var isEnabled: Boolean,
                var isChecked: Boolean,
                var backgroundColor: Int,
                var textColor: Int) {

    companion object {
        fun initial(context: Context): CardStyle {
            return CardStyle(
                isCheckable = true,
                isEnabled = true,
                isChecked = false,
                backgroundColor = Color.WHITE,
                textColor = color(context, R.color.transparent_black))
        }

        fun correct(context: Context): CardStyle {
            return CardStyle(
                isCheckable = true,
                isEnabled = true,
                isChecked = true,
                backgroundColor = color(context, R.color.colorAccent),
                textColor = color(context, R.color.transparent_black))
        }

/*        fun wrong(context: Context): CardStyle {
            return CardStyle(
                isCheckable = true,
                isEnabled = true,
                isChecked = true,
                backgroundColor = color(context, R.color.colorAccent),
                textColor = color(context, R.color.transparent_black))
        }*/
    }
}