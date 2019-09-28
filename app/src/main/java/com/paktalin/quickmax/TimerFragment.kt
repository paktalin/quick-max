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

private const val RESPONSE_CORRECT = "Correct!"
private const val RESPONSE_WRONG = "Wrong!"
private const val RESPONSE_TIME_OVER = "Time is over!"

class TimerFragment : Fragment() {

    private lateinit var timer: CountDownTimer
    private lateinit var tvResponse: TextView
    private var colorAnimation: ValueAnimator? = null

    private var millisToSolve: Long = 0
    private var colorFrom: Int = -1

    private val interval: Long = 1000
    private var finished: Boolean = false
    private lateinit var mView: View
    private var restoredColor: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        millisToSolve = arguments!!.getLong("millis_to_solve")
        colorFrom = Color.TRANSPARENT
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.gradient_timer, container, false)
        mView = view
        tvResponse = mView.findViewById(R.id.tv_response)

        if (savedInstanceState != null)
            restoreState(savedInstanceState)
        if (!finished) {
            initTimer()
            initColorAnimation()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        if (!finished) {
            timer?.start()
            colorAnimation?.start()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val text = tvResponse.text.toString()
        if (text == RESPONSE_CORRECT || text == RESPONSE_WRONG || text == RESPONSE_TIME_OVER)
            outState.putString("response_text", text)
        else
            outState.putLong("millis_to_solve", millisToSolve)

        colorAnimation?.apply {
            if (animatedValue != null)
                restoredColor = animatedValue as Int
        }
        restoredColor?.let { outState.putInt("color_from", it) }
    }

    fun cancelCorrect() {
        cancel()
        tvResponse.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize(resources))
        tvResponse.text = RESPONSE_CORRECT
    }

    fun cancelWrong() {
        cancel()
        tvResponse.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize(resources))
        tvResponse.text = RESPONSE_WRONG
    }

    private fun restoreState(savedInstanceState: Bundle) {
        val responseText = savedInstanceState.getString("response_text")
        if (responseText != null) {
            tvResponse.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize(resources))
            tvResponse.text = responseText
            restoredColor = savedInstanceState.getInt("color_from")
            restoredColor?.let { setBackgroundFilter(it) }
            finished = true
        } else {
            millisToSolve = savedInstanceState.getLong("millis_to_solve")
            colorFrom = savedInstanceState.getInt("color_from")
        }
    }

    private fun cancel() {
        timer.cancel()
        colorAnimation?.cancel()
    }

    private fun setBackgroundFilter(color: Int) {
        if (isAdded)
           mView.background.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
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
                    tvResponse.text = RESPONSE_TIME_OVER
                    (activity as TaskActivity).onTimeOver()
                }

            }
        }
    }

    private fun initColorAnimation() {
        val colorTo = color(context!!, R.color.transparent_red)
        colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
            .apply { duration = millisToSolve }
        colorAnimation?.addUpdateListener { animator ->
            setBackgroundFilter(animator.animatedValue as Int)
        }
    }
}