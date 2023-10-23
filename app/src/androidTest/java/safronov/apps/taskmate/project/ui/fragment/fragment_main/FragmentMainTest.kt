package safronov.apps.taskmate.project.ui.fragment.fragment_main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.not
import org.junit.Assert.*
import safronov.apps.taskmate.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import safronov.apps.taskmate.project.ui.activity.MainActivity

@RunWith(AndroidJUnit4::class)
class FragmentMainTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun test_clickOnSearchView_shouldOpenFragmentForSearching() {
        onView(withId(R.id.included_search_view)).perform(click())
        onView(withId(R.id.included_search_view)).check(matches(not(isDisplayed())))
    }

    //TODO write tests for event when user click on button, choose task category an etc

}