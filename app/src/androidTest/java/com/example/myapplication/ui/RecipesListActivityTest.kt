package com.example.myapplication.ui


import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import com.example.myapplication.DataStatus
import com.example.myapplication.R
import com.example.myapplication.TestUtils.dataStatus
import com.example.myapplication.ui.component.MainActivity
import com.example.myapplication.utils.EspressoIdLingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class RecipesListActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java, false, false)
    private var mIdlingResource: IdlingResource? = null

    @Before
    fun setup() {
        IdlingRegistry.getInstance().register(EspressoIdLingResource.countingIdlingResource )
    }

    @Test
    fun displayRecipesList() {
        dataStatus = DataStatus.Success
        mActivityTestRule.launchActivity(null)
        onView(withId(R.id.rv_recipes_list))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.pb_loading))
            .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
    }



    @Test
    fun noData() {
        dataStatus = DataStatus.Fail
        mActivityTestRule.launchActivity(null)
        onView(withId(R.id.rv_recipes_list))
            .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
        onView(withId(R.id.pb_loading))
            .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
        onView(withId(R.id.tv_no_data))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @After
    fun unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister()
        }
    }
}