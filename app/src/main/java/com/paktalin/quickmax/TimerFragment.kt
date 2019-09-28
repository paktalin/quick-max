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

private const val interval: Long = 1000

private const val KEY_MILLIS_TO_SOLVE = "millis_to_solve"
private const val KEY_COLOR_FROM = "color_from"
private const val KEY_STATE = "state"

enum class State(val response: String?) {
    IN_PROGRESS(null),
    CORRECT("Correct!"),
    WRONG("Wrong!"),
    TIME_IS_OVER("Time is over!")
}

class TimerFragment : Fragment() {

    private lateinit var tvResponse: TextView
    private lateinit var state: State
    private lateinit var mView: View

    private var millisToSolve: Long = 0
    private var colorFrom: Int = -1

    private var colorAnimation: ValueAnimator? = null
    private  var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        millisToSolve = arguments!!.getLong(KEY_MILLIS_TO_SOLVE)
        colorFrom = Color.TRANSPARENT
        state = State.IN_PROGRESS
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
        if (state == State.IN_PROGRESS) {
            initTimer()
            initColorAnimation()
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        timer?.start()
        colorAnimation?.start()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_STATE, state.name)

        if (state == State.IN_PROGRESS)
            outState.putLong(KEY_MILLIS_TO_SOLVE, millisToSolve)
        colorAnimation?.let { animator ->
            animator.animatedValue?.let {
                    value -> colorFrom = value as Int
            }
        }
        outState.putInt(KEY_COLOR_FROM, colorFrom)
    }

    fun cancel(state: State) {
        this.state = state
        setResult()
        timer?.cancel()
        colorAnimation?.cancel() }

    private fun setResult() {
        if (isAdded) {
            tvResponse.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize(resources))
            tvResponse.text = state.response
        }
    }

    private val setBackgroundFilter = {color: Int ->
        if (isAdded) mView.background.setColorFilter(color, PorterDuff.Mode.SRC_ATOP) }

    private fun restoreState(savedInstanceState: Bundle) {
        state = State.valueOf(savedInstanceState.getString(KEY_STATE)!!)
        colorFrom = savedInstanceState.getInt(KEY_COLOR_FROM)
        if (state == State.IN_PROGRESS) {
            millisToSolve = savedInstanceState.getLong(KEY_MILLIS_TO_SOLVE)
        } else {
            setResult()
            setBackgroundFilter(colorFrom)
        }
    }

    private fun initTimer() {
        timer = object : CountDownTimer(millisToSolve, interval) {
            override fun onTick(millisUntilFinished: Long) {
                millisToSolve = millisUntilFinished
                tvResponse.text = (millisUntilFinished / interval).toString()
            }

            override fun onFinish() {
                this@TimerFragment.state = State.TIME_IS_OVER
                setResult()
                if (isAdded) (activity as TaskActivity).onTimeOver()
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