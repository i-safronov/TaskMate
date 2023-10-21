package safronov.apps.taskmate.project.ui.fragment.welcome

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import safronov.apps.taskmate.R
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import safronov.apps.taskmate.project.ui.activity.MainActivity

@RunWith(AndroidJUnit4::class)
class FragmentWelcomeTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testButtonContinueIsVisible() {
        onView(withId(R.id.btn_sign_in)).check(matches(isDisplayed()))
    }

    @Test
    fun testPrivacyPoliceIsVisible() {
        onView(withId(R.id.tv_privacy_police)).check(matches(isDisplayed()))
    }

    @Test
    fun testWelcomeImgIsVisible() {
        onView(withId(R.id.welcome_img)).check(matches(isDisplayed()))
    }

    @Test
    fun testButtonContinueClick_shouldShowProgressBarAndAnotherText() {
        onView(withId(R.id.btn_sign_in)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_sign_in)).perform(click()).check(matches(isDisplayed()))
        onView(withId(R.id.loaging_progress_bar)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_please_wait)).check(matches(isDisplayed()))
        onView(withId(R.id.welcome_img)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_privacy_police)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_sign_in)).check(matches(isDisplayed()))
    }

}