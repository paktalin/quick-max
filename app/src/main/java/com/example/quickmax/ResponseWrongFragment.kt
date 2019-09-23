package com.example.quickmax

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class ResponseWrongFragment: Fragment() {

    companion object {
        fun newInstance(): ResponseWrongFragment {
            return ResponseWrongFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_response_wrong, container, false)
    }
}