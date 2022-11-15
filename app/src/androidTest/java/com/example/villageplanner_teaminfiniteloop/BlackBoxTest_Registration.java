package com.example.villageplanner_teaminfiniteloop;

import android.app.Activity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.activity.result.ActivityResult;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BlackBoxTest_Registration {
    @Rule public ActivityScenarioRule<LoginActivity> activityScenarioRule
            = new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void Test_Register() throws InterruptedException {
        // Type text and then press the button.
        onView(withId(R.id.register)).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.name))
                .perform(typeText("Tester"), closeSoftKeyboard());
        onView(withId(R.id.email))
                .perform(typeText("unittest@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.button2)).perform(click());
        Thread.sleep(2000);
        // Check that the text was changed.
        onView(withId(R.id.nav_view)).check(matches(isDisplayed()));
    }

}
