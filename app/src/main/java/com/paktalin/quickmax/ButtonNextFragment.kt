package com.paktalin.quickmax

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.btn_next.view.*

class ButtonNextFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val correct = arguments!!.getBoolean("correct")
        val view = inflater.inflate(R.layout.btn_next, container, false)
        if (correct)
            view.btn_next.backgroundTintList = ContextCompat.getColorStateList(context!!, R.color.colorAccent)

        view.btn_next.setOnClickListener { (activity as TaskActivity).startNewRound() }
        return view
    }
}