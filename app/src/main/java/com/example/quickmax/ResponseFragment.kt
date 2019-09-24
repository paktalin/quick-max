package com.example.quickmax

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

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
            view(view, ContextCompat.getColor(activity!!, R.color.gradient_light), R.string.response_correct)
        else
            view(view, ContextCompat.getColor(activity!!, R.color.gradient_dark), R.string.response_wrong)
    }

    private fun view(view: View, color: Int, responseId: Int): View {
        view.findViewById<TextView>(R.id.tv_response).text = resources.getString(responseId)
        view.findViewById<ConstraintLayout>(R.id.response_layout).setBackgroundColor(color)
        view.findViewById<ImageButton>(R.id.btn_next).setOnClickListener { (activity as TaskActivity).reload() }
        return view
    }
}