package com.example.quickmax

import android.graphics.Color

class CardStyle(var isCheckable: Boolean,
                var isEnabled: Boolean,
                var isChecked: Boolean,
                var backgroundColor: Int,
                var textColorId: Int) {

    companion object {
        fun initial(): CardStyle {
            return CardStyle(
                isCheckable = true,
                isEnabled = true,
                isChecked = false,
                backgroundColor = Color.WHITE,
                textColorId = R.color.transparent_black)
        }
    }
}