package com.example.quickmax

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class ResponseFragment: Fragment() {

    companion object {
        fun newInstance(): ResponseFragment {
            return ResponseFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_response, container, false)
        val correct = arguments!!.getBoolean("correct")
        return if (correct)
            view(view, color(R.color.colorAccent), R.string.response_correct, color(R.color.transparent_dark_black))
        else
            view(view, color(R.color.colorPrimary), R.string.response_wrong, Color.WHITE)
    }

    private fun view(view: View, color: Int, responseId: Int, textColor: Int): View {
        view.findViewById<TextView>(R.id.tv_response).apply {
            text = resources.getString(responseId)
            setTextColor(color)
        }

        view.findViewById<MaterialButton>(R.id.btn_next).apply {
            background.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
            setTextColor(textColor)
            setOnClickListener { (activity as TaskActivity).reload() } }
        return view
    }

    private fun color(colorId: Int): Int {
        return ContextCompat.getColor(activity!!, colorId)
    }
}