package com.example.myapplication.ui

import android.widget.Toast
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.ComponentNameMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.example.myapplication.R
import com.example.myapplication.ui.component.LoginActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class LoginActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var mActivityTestRule = IntentsTestRule(LoginActivity::class.java, true, true)

    @Before
    fun setup() {
    }

    @Test
    fun testLoginSuccess() {
        mActivityTestRule.launchActivity(null)
        onView(withId(R.id.username)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.password)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.login)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.username)).perform(ViewActions.clearText())
        onView(withId(R.id.password)).perform(ViewActions.clearText())
        onView(withId(R.id.username)).perform(ViewActions.typeText("anil@gmail.com"))
        onView(withId(R.id.password)).perform(ViewActions.typeText("anil"))
        onView(withId(R.id.login)).perform(ViewActions.click())
        Intents.intended(IntentMatchers.hasComponent(ComponentNameMatchers.hasClassName("com.example.myapplication.ui.component.MainActivity")))
    }

    @Test
    fun testWrongUserName() {
        Thread.sleep(Toast.LENGTH_LONG * 2L)
        mActivityTestRule.launchActivity(null)
        onView(withId(R.id.username)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.password)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.login)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.username)).perform(ViewActions.clearText())
        onView(withId(R.id.password)).perform(ViewActions.clearText())
        onView(withId(R.id.username)).perform(ViewActions.typeText(""))
        onView(withId(R.id.password)).perform(ViewActions.typeText("ahmed"))
        Thread.sleep(Toast.LENGTH_LONG.toLong())
        onView(withId(R.id.login)).perform(ViewActions.click())
        onView(withText(R.string.invalid_user_name)).inRoot(RootMatchers.withDecorView(CoreMatchers.not(`is`(mActivityTestRule.activity.window.decorView)))).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    @Test
    fun testWrongPassword() {
        Thread.sleep(Toast.LENGTH_LONG * 2L)
        mActivityTestRule.launchActivity(null)
        onView(withId(R.id.username)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.password)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.login)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.username)).perform(ViewActions.clearText())
        onView(withId(R.id.password)).perform(ViewActions.clearText())
        onView(withId(R.id.username)).perform(ViewActions.typeText("anil@gmail.com"))
        onView(withId(R.id.password)).perform(ViewActions.typeText(""))
        onView(withId(R.id.login)).perform(ViewActions.click())
        onView(withText(R.string.invalid_password))
            .inRoot(RootMatchers.withDecorView(CoreMatchers.not(`is`(mActivityTestRule.activity.window.decorView)))).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    @Test
    fun testWrongUserNameAndPassword() {
        Thread.sleep(Toast.LENGTH_LONG * 2L)
        mActivityTestRule.launchActivity(null)
        onView(withId(R.id.username)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.password)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.login)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.username)).perform(ViewActions.clearText())
        onView(withId(R.id.password)).perform(ViewActions.clearText())
        onView(withId(R.id.username)).perform(ViewActions.typeText(" "))
        onView(withId(R.id.password)).perform(ViewActions.typeText(" "))
        onView(withId(R.id.login)).perform(ViewActions.click())
        onView(withText(R.string.invalid_username_and_password))
            .inRoot(RootMatchers.withDecorView(CoreMatchers.not(`is`(mActivityTestRule.activity.window.decorView)))).check(
            ViewAssertions.matches(ViewMatchers.isDisplayed())
        )
    }

    @After
    fun shutDown() {
    }

}