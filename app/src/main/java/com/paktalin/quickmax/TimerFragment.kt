package com.paktalin.quickmax

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.CountDownTimer
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class TimerFragment: Fragment()  {

    private lateinit var timer: CountDownTimer
    private lateinit var tvResponse: TextView
    private lateinit var colorAnimation: ValueAnimator

    private var millisToSolve: Long = 0

    private val interval: Long = 1000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        millisToSolve = arguments!!.getLong("millis_to_solve")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.gradient_timer, container, false)
        tvResponse = view!!.findViewById(R.id.tv_response)

        if (savedInstanceState != null)
            restoreState(savedInstanceState)
        else {
            initTimer()
            initColorAnimation()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        timer.start()
//        colorAnimation.start()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("millis_to_solve", millisToSolve)
    }

    fun cancelCorrect() {
        cancel()
        tvResponse.text = resources.getString(R.string.response_correct)
    }

    fun cancelWrong() {
        cancel()
        tvResponse.text = resources.getString(R.string.response_wrong)
    }

    private fun restoreState(savedInstanceState: Bundle) {
        millisToSolve = savedInstanceState.getLong("millis_to_solve")
        initTimer()
    }

    private fun cancel() {
        timer.cancel()
        colorAnimation.cancel()
        tvResponse.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize(resources) )
    }

    private fun initTimer() {
        timer = object : CountDownTimer(millisToSolve, interval) {
            override fun onTick(millisUntilFinished: Long) {
                millisToSolve = millisUntilFinished
                tvResponse.text = (millisUntilFinished / interval).toString()
            }

            override fun onFinish() {
                if (isAdded) {
                    tvResponse.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize(resources))
                    tvResponse.text = resources.getString(R.string.time_is_over)
                    (activity as TaskActivity).onTimeOver()
                }
            }
        }
    }

    private fun initColorAnimation() {
        val colorFrom = Color.TRANSPARENT
        val colorTo = color(context!!, R.color.transparent_red)
        colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
            .apply { duration = millisToSolve }
        colorAnimation.addUpdateListener { animator ->
            view!!.background.setColorFilter(animator.animatedValue as Int, PorterDuff.Mode.SRC_ATOP)
        }
    }
}