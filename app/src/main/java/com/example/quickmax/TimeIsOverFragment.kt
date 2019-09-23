package com.example.quickmax

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment

class TimeIsOverFragment: Fragment() {
    companion object {
        fun newInstance(): TimeIsOverFragment {
            return TimeIsOverFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_time_is_over, container, false)
        view.findViewById<ImageButton>(R.id.btn_next).setOnClickListener { (activity as MainActivity).reload() }
        return view
    }
}