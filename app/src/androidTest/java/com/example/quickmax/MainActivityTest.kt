package com.example.quickmax

import android.view.View
import android.widget.RadioGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.rule.ActivityTestRule
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.Rule
import org.junit.Test
import android.widget.TextView
import androidx.core.view.get
import androidx.test.espresso.ViewAction
import androidx.test.espresso.UiController
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matcher
import org.junit.Before


class MainActivityTest {
    @get:Rule
    val testRule = ActivityTestRule<MainActivity>(MainActivity::class.java)
    private lateinit var radioGroup: RadioGroup


    @Before
    fun init() {
        radioGroup = testRule.activity.radio_group
    }

    @Test
    fun correct_answer_marked_as_correct() {
        val correctAnswerIndex = getCorrectAnswerIndex()
        val correctAnswerId = radioGroup[correctAnswerIndex].id
        onView(withId(correctAnswerId)).perform(click())
        onView(withId(R.id.response)).check(matches(withText(R.string.response_correct)))
    }

    @Test
    fun wrong_answer_marked_as_wrong() {
        val correctAnswerIndex = getCorrectAnswerIndex()
        val wrongAnswerIndex = if (correctAnswerIndex == 3) 2 else 3
        val wrongAnswerId = radioGroup[wrongAnswerIndex].id
        onView(withId(wrongAnswerId)).perform(click())
        onView(withId(R.id.response)).check(matches(withText(R.string.response_wrong)))
    }

    private fun getCorrectAnswerIndex(): Int {
        val options = getOptions()
        val correctAnswer = findSecondMax(options)
        return options.indexOf(correctAnswer)
    }

    private fun getOptions(): MutableList<Int> {
        val options = mutableListOf<Int>()
        for (i in 0 until radioGroup.childCount)
            options.add(getText(withId(radioGroup[i].id)).toInt())
        return options
    }

    private fun getText(matcher: Matcher<View>): String {
        var text = ""
        onView(matcher).perform(object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(TextView::class.java)
            }

            override fun getDescription(): String { return "" }

            override fun perform(uiController: UiController, view: View) {
                val tv = view as TextView
                text = tv.text.toString()
            }
        })
        return text
    }
}