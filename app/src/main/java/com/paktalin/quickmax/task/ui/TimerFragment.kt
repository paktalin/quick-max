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

private const val COUNTDOWN_INTERVAL: Long = 1000
private const val COLOR_FROM: Int = Color.TRANSPARENT

private const val KEY_MILLIS_TO_SOLVE = "millis_to_solve"
private const val KEY_MILLIS_LEFT = "millis_left"
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
    private lateinit var mView: View

    private var millisToSolve: Long = 0
    private var state: State = State.NONE

    private var colorAnimation: ValueAnimator? = null
    private var timer: CountDownTimer? = null
    private var millisLeft: Long = 0
    private var colorFrom: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        millisToSolve = arguments!!.getLong(KEY_MILLIS_TO_SOLVE)
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
        outState.putLong(KEY_MILLIS_LEFT, millisLeft)
        outState.putInt(KEY_COLOR_FROM, colorFrom)

        // try to update COLOR_FROM from animation
        colorAnimation?.let { animator ->
            animator.animatedValue?.let { value ->
                outState.putInt(KEY_COLOR_FROM, value as Int)
            }
        }
    }

    fun startNewRound(left: Long = millisToSolve, from: Int = COLOR_FROM) {
        if (isAdded) {
            state = State.IN_PROGRESS
            mView.tv_response
                .apply { text = "" }
                .apply { setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeLarge(resources)) }

            timer = initTimer(left).start()
            colorAnimation = initColorAnimation(from).apply { start() }
        }
    }

    fun cancel(state: State) {
        this.state = state
        setResult()
        timer?.cancel()
        colorAnimation?.cancel()
    }

    private fun restoreState(savedInstanceState: Bundle) {
        state = State.valueOf(savedInstanceState.getString(KEY_STATE)!!)
        millisToSolve = savedInstanceState.getLong(KEY_MILLIS_TO_SOLVE)
        colorFrom = savedInstanceState.getInt(KEY_COLOR_FROM)

        if (state == State.IN_PROGRESS)
            startNewRound(savedInstanceState.getLong(KEY_MILLIS_LEFT), colorFrom)
        else {
            setResult()
            setBackgroundFilter(colorFrom)
        }
    }

    private fun setResult() {
        if (isAdded) {
            mView.tv_response
                .apply { text = "" }
                .apply { setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizeSmall(resources)) }
                .apply { text = state.response }
        }
    }

    private val setBackgroundFilter = { color: Int ->
        if (isAdded) mView.background.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }

    private fun initTimer(toSolve: Long): CountDownTimer {
        return object : CountDownTimer(toSolve, COUNTDOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                millisLeft = millisUntilFinished
                mView.tv_response.text = (millisUntilFinished / COUNTDOWN_INTERVAL).toString()
            }

            override fun onFinish() {
                this@TimerFragment.state = State.TIME_IS_OVER
                setResult()
                if (isAdded) (activity as TaskActivity).onTimeOver()
            }
        }
    }

    private fun initColorAnimation(colorFrom: Int): ValueAnimator {
        val colorTo = color(context!!, R.color.transparent_red)
        return ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
            .apply { duration = millisToSolve }
            .apply { addUpdateListener { a -> setBackgroundFilter(a.animatedValue as Int) } }
    }
}