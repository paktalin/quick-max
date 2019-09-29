package com.paktalin.quickmax.task.ui

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
import androidx.fragment.app.Fragment
import com.paktalin.quickmax.R
import com.paktalin.quickmax.color
import com.paktalin.quickmax.textSizeLarge
import com.paktalin.quickmax.textSizeSmall
import kotlinx.android.synthetic.main.fragment_timer.view.*

private const val interval: Long = 1000

private const val KEY_MILLIS_TO_SOLVE = "millis_to_solve"
private const val KEY_COLOR_FROM = "color_from"
private const val KEY_STATE = "state"

enum class State(val response: String?) {
    NONE(null),
    IN_PROGRESS(null),
    CORRECT("Correct!"),
    WRONG("Wrong!"),
    TIME_IS_OVER("Time is over!")
}

class TimerFragment : Fragment() {

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
        state = State.NONE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timer, container, false)
        mView = view

        if (savedInstanceState != null)
            restoreState(savedInstanceState)
        else
            startNewRound()
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_STATE, state.name)
        outState.putLong(KEY_MILLIS_TO_SOLVE, millisToSolve)
        // try to update colorFrom from animation
        colorAnimation?.let { animator ->
            animator.animatedValue?.let {
                value -> colorFrom = value as Int
            }
        }
        outState.putInt(KEY_COLOR_FROM, colorFrom)
    }

    fun startNewRound() {
        if (isAdded) {
            state = State.IN_PROGRESS
            mView.tv_response
                .apply { text = "" }
                .apply { setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeLarge(resources)) }
            if (timer == null)
                initTimer()
            timer?.start()



            if (colorAnimation == null)
                initColorAnimation()
            colorAnimation?.start()
        }
    }

    fun cancel(state: State) {
        this.state = state
        setResult()
        timer?.cancel()
        colorAnimation?.cancel() }

    private fun setResult() {
        if (isAdded) {
            mView.tv_response
                .apply { text = "" }
                .apply { setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeSmall(resources)) }
                .apply { text = state.response }
        }
    }

    private val setBackgroundFilter = {color: Int ->
        if (isAdded) mView.background.setColorFilter(color, PorterDuff.Mode.SRC_ATOP) }

    private val startTimer  = {}

    private fun restoreState(savedInstanceState: Bundle) {
        state = State.valueOf(savedInstanceState.getString(KEY_STATE)!!)
        colorFrom = savedInstanceState.getInt(KEY_COLOR_FROM)
        millisToSolve = savedInstanceState.getLong(KEY_MILLIS_TO_SOLVE)

        if (state == State.IN_PROGRESS)
            startNewRound()
        else {
            setResult()
            setBackgroundFilter(colorFrom)
        }
    }

    private fun initTimer() {
        timer = object : CountDownTimer(millisToSolve, interval) {
            override fun onTick(millisUntilFinished: Long) {
                millisToSolve = millisUntilFinished
                mView.tv_response.text = (millisUntilFinished / interval).toString()
            }

            override fun onFinish() {
                this@TimerFragment.state = State.TIME_IS_OVER
                setResult()
                if (isAdded) (activity as TaskActivity).onTimeOver()
            }
        }
    }

    private fun initColorAnimation() {
        val colorTo =
            color(context!!, R.color.transparent_red)
        colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
            .apply { duration = millisToSolve }
        colorAnimation?.addUpdateListener { animator ->
            setBackgroundFilter(animator.animatedValue as Int)
        }
    }
}