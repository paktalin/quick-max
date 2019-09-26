package com.example.quickmax

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView

class AnswerCardView : MaterialCardView {

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

    fun markCorrect(context: Context) {
        checkedIcon = context.resources.getDrawable(R.drawable.ic_check, null)
        foregroundTintList = ContextCompat.getColorStateList(context, R.color.colorAccent)
    }

    fun markWrong(context: Context) {
        checkedIcon = context.resources.getDrawable(R.drawable.ic_cancel, null)
        foregroundTintList = ContextCompat.getColorStateList(context, R.color.red)
    }

    fun disable() {
        isCheckable = false
        isEnabled = false
    }
}