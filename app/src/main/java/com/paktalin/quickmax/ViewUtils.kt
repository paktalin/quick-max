package com.paktalin.quickmax

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.android.material.card.MaterialCardView
import com.paktalin.quickmax.task.ui.ButtonNextFragment

fun getTextView(card: View): TextView {
    return  ((card as MaterialCardView).getChildAt(0) as FrameLayout).getChildAt(0) as TextView
}

fun color(context: Context, id: Int): Int {
    return ContextCompat.getColor(context, id)
}

fun textSize(resources: Resources): Float {
    val screenDensity = resources.displayMetrics.density
    return resources.getDimension(R.dimen.response_text_size) / screenDensity
}

fun addButtonNextFragment(supportFragmentManager: FragmentManager, correct: Boolean) {
    val fragmentBtnNext = ButtonNextFragment().apply {
        arguments = Bundle().apply { putBoolean("correct", correct) }
    }
    supportFragmentManager.commit(true) {
        add(R.id.main_layout, fragmentBtnNext, "btn_next_fragment")
    }
}

fun removeButtonNextFragment(supportFragmentManager: FragmentManager) {
    supportFragmentManager.findFragmentByTag("btn_next_fragment")?.let {fragment ->
        supportFragmentManager.commit(true) {
            remove(fragment)
        }
    }
}