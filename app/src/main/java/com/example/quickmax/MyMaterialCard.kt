package com.example.quickmax

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import com.google.android.material.card.MaterialCardView

class MyMaterialCard : MaterialCardView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun clean(context: Context) {
            isCheckable = true
            isEnabled = true
            isChecked = false
            setCardBackgroundColor(Color.WHITE)
            getTextView(this).setTextColor(color(context, R.color.transparent_black))
    }

    fun markCorrect() {
            setCardBackgroundColor(color(context, R.color.colorAccent))
    }

    fun markWrong(context: Context) {
        setCardBackgroundColor(color(context, R.color.colorPrimary))
        getTextView(this).setTextColor(Color.WHITE)
    }

    fun disable() {
            isCheckable = false
            isEnabled = false
    }
}