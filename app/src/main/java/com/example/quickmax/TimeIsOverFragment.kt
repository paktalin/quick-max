package com.example.quickmax

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        view.setOnClickListener { (activity as TaskActivity).reload() }
        return view
    }
}