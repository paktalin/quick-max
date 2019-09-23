package com.example.quickmax

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment

class ResponseCorrectFragment: Fragment() {

    companion object {
        fun newInstance(): ResponseCorrectFragment {
            return ResponseCorrectFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_response_correct, container, false)
        view.findViewById<ImageButton>(R.id.btn_next).setOnClickListener { reloadActivity() }
        return view
    }

    private fun reloadActivity() {
        val intent = activity?.intent
        activity?.finish()
        activity?.startActivity(intent)
    }
}