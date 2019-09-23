package com.example.quickmax

import androidx.test.rule.ActivityTestRule
import kotlinx.android.synthetic.main.activity_main.*
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    @get:Rule
    val testRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Test
    fun test() {
        testRule.activity.radio_group.getChildAt(0)
    }
}