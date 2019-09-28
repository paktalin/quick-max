package com.paktalin.quickmax

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton

class ButtonNext: MaterialButton {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun initial() {
        visibility = View.INVISIBLE
    }

    fun correct(context: Context) {
        visibility = View.VISIBLE
        backgroundTintList = ContextCompat.getColorStateList(context, R.color.colorAccent)
        setTextColor(color(context, R.color.transparent_dark_black))
    }

    fun incorrect(context: Context) {
        visibility = View.VISIBLE
        backgroundTintList = ContextCompat.getColorStateList(context, R.color.colorPrimary)
        setTextColor(Color.WHITE)
    }
}
