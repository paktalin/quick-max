package com.paktalin.quickmax

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView

fun getTextView(card: View): TextView {
    return  ((card as MaterialCardView).getChildAt(0) as FrameLayout).getChildAt(0) as TextView
}

fun color(context: Context, id: Int): Int {
    return ContextCompat.getColor(context, id)
}