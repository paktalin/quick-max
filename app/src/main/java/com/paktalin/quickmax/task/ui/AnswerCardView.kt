package com.paktalin.quickmax.task.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.paktalin.quickmax.R
import com.paktalin.quickmax.color
import com.paktalin.quickmax.getTextView

class AnswerCardView : MaterialCardView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    fun initial(context: Context, text: Int) {
        isCheckable = true
        isEnabled = true
        isChecked = false
        setCardBackgroundColor(Color.WHITE)
        getTextView(this).setTextColor(
            color(
                context,
                R.color.transparent_black
            )
        )
        getTextView(this).text = text.toString()
        setOnClickListener { isChecked = true }
    }

    fun markCorrect(context: Context) {
        checkedIcon = context.resources.getDrawable(R.drawable.ic_check, null)
        foregroundTintList = ContextCompat.getColorStateList(context,
            R.color.colorAccent
        )
    }

    fun markWrong(context: Context) {
        checkedIcon = context.resources.getDrawable(R.drawable.ic_cancel, null)
        foregroundTintList = ContextCompat.getColorStateList(context,
            R.color.red
        )
    }

    fun disable() {
        isCheckable = false
        isEnabled = false
    }
}