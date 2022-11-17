package com.example.villageplanner_teaminfiniteloop;

import android.app.Activity;
import android.view.View;
import android.widget.TimePicker;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
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

/**
 * Basic tests showcasing simple view matchers and actions like {@link ViewMatchers#withId},
 * {@link ViewActions#click} and {@link ViewActions#typeText}.
 * <p>
 * Note that there is no need to tell Espresso that a view is in a different {@link Activity}.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BlackBoxTest_Reminder_Creation{

    public static final String STRING_TO_BE_TYPED_GO_TO_CAVA = "go to cava";
    public static final int HOUR = 11;
    public static final int MINUTE = 15;


    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test, and close it
     * after test completes.
     */
    @Rule public ActivityScenarioRule<ReminderCreatorActivity> activityScenarioRule
            = new ActivityScenarioRule<>(ReminderCreatorActivity.class);

//    @Before
//    public void stubAllExternalIntents() {
//        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
//        // every test run. In this case all external Intents will be blocked.
//        intending(not(isInternal())).respondWith(new ActivityResult(Activity.RESULT_OK, null));
//    }

    @Test
    public void Test_Reminder_Creation() throws InterruptedException {
        // Type text and then press the button.
        onView(withId(R.id.email))
                .perform(typeText("jack@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        Thread.sleep(2000);



        onView(withId(R.id.reminderDescription))
                .perform(typeText(STRING_TO_BE_TYPED_GO_TO_CAVA), closeSoftKeyboard());
        onView(withId(R.id.reminderTimePicker))
                .perform(setTime());
        onView(withId(R.id.createReminder)).perform(click());
        Thread.sleep(2000);
        // Check that the text was changed.
        onView(withId(R.id.nav_view)).check(matches(isDisplayed()));
    }
    public static ViewAction setTime() {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                TimePicker tp = (TimePicker) view;
                tp.setCurrentHour(HOUR);
                tp.setCurrentMinute(MINUTE);
            }
            @Override
            public String getDescription() {
                return "Set the passed time into the TimePicker";
            }
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(TimePicker.class);
            }
        };
    }
}
