package com.example.quickmax

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import android.widget.TextView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.UiController
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import org.hamcrest.Matcher
import org.junit.Before

class MainActivityTest {
    @get:Rule
    val testRule = ActivityTestRule<MainActivity>(MainActivity::class.java, true, true)

    private lateinit var answers: Map<Int, Int>

    @Before
    fun init() {
        answers = mapOf(
            getText(withId(R.id.btn_left_top)).toInt() to R.id.btn_left_top,
            getText(withId(R.id.btn_right_top)).toInt() to R.id.btn_right_top,
            getText(withId(R.id.btn_left_bottom)).toInt() to R.id.btn_left_bottom,
            getText(withId(R.id.btn_right_bottom)).toInt() to R.id.btn_right_bottom
        )
    }

    @Test
    fun correct_answer_marked_as_correct() {
        val correctAnswerId = getCorrectAnswerId()
        onView(withId(correctAnswerId)).perform(click())
        onView(withId(R.id.tv_response))
            .check(matches(withText(testRule.activity.resources.getString(R.string.response_correct))))
    }

    @Test
    fun wrong_answer_marked_as_wrong() {
        val wrongAnswerId = getWrongAnswerId()
        onView(withId(wrongAnswerId)).perform(click())
        onView(withId(R.id.tv_response))
            .check(matches(withText(testRule.activity.resources.getString(R.string.response_wrong))))
    }

    @Test
    fun only_fist_answer_accepted() {
        onView(withId(R.id.btn_right_bottom)).perform(click())
        assertTrue(testRule.activity.supportFragmentManager.fragments.size == 1)
        onView(withId(R.id.btn_left_bottom)).perform(click())
        assertTrue(testRule.activity.supportFragmentManager.fragments.size == 1)
    }

    @Test
    fun time_decreases() {
        val time1 = getText(withId(R.id.tv_time_left)).toInt()
        Thread.sleep(1000)
        val time2 = getText(withId(R.id.tv_time_left)).toInt()
        assertTrue(time2 < time1)
    }

    @Test
    fun fragment_when_time_is_over() {
        Thread.sleep(4000)
        assertNotNull(testRule.activity.supportFragmentManager.fragments.find {
                f -> f is TimeIsOverFragment })
    }

    private fun getCorrectAnswerId(): Int {
        val correctAnswer = findSecondMax(answers.keys)
        return answers.getValue(correctAnswer)
    }

    private fun getWrongAnswerId(): Int {
        val correctAnswer = findSecondMax(answers.keys)
        for (answer in answers)
            if (answer.key != correctAnswer)
                return answer.value
        return answers.getValue(-1)
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