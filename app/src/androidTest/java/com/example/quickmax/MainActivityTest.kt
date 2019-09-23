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
import androidx.test.espresso.matcher.ViewMatchers.*
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
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

        assertTrue(testRule.activity.supportFragmentManager.fragments.size == 1)
        assertTrue(testRule.activity.supportFragmentManager.fragments[0] is ResponseCorrectFragment)
    }

    @Test
    fun wrong_answer_marked_as_wrong() {
        val correctAnswerIndex = getCorrectAnswerIndex()
        val wrongAnswerIndex = if (correctAnswerIndex == 3) 2 else 3
        val wrongAnswerId = radioGroup[wrongAnswerIndex].id
        onView(withId(wrongAnswerId)).perform(click())

        assertTrue(testRule.activity.supportFragmentManager.fragments.size == 1)
        assertTrue(testRule.activity.supportFragmentManager.fragments[0] is ResponseWrongFragment)
    }

    @Test
    fun only_fist_answer_accepted() {
        onView(withId(radioGroup[0].id)).perform(click())
        assertTrue(testRule.activity.supportFragmentManager.fragments.size == 1)
        onView(withId(radioGroup[1].id)).perform(click())
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