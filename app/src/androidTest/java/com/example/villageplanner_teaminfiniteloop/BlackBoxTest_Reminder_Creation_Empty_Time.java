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
import androidx.test.espresso.action.CoordinatesProvider;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Tap;
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
public class BlackBoxTest_Reminder_Creation_Empty_Time{

    public static final String STRING_TO_BE_TYPED_EMPTY = "go to cava";
//    public static final int HOUR = 11;
//    public static final int MINUTE = 15;


    /**
     * Use {@link ActivityScenarioRule} to create and launch the activity under test, and close it
     * after test completes.
     */
    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule
            = new ActivityScenarioRule<>(LoginActivity.class);

    //    @Before
//    public void stubAllExternalIntents() {
//        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
//        // every test run. In this case all external Intents will be blocked.
//        intending(not(isInternal())).respondWith(new ActivityResult(Activity.RESULT_OK, null));
//    }
    public static ViewAction clickXY(final int x, final int y){
        return new GeneralClickAction(
                Tap.SINGLE,
                new CoordinatesProvider() {
                    @Override
                    public float[] calculateCoordinates(View view) {

                        final int[] screenPos = new int[2];
                        view.getLocationOnScreen(screenPos);

                        final float screenX = screenPos[0] + x;
                        final float screenY = screenPos[1] + y;
                        float[] coordinates = {screenX, screenY};

                        return coordinates;
                    }
                },
                Press.FINGER);
    }

    @Test
    public void Test_Reminder_Creation() throws InterruptedException {
        // Type text and then press the button.
        onView(withId(R.id.email))
                .perform(typeText("jack@usc.edu"), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText("123"), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        Thread.sleep(2000);

        // Check map annotation works
        // Max X: 1080, Max Y: 1794, click on one restaurant
        onView(withId(R.id.mapView)).perform(clickXY(280, 1120));
        Thread.sleep(2000);

        //Click on Annotation
        onView(withId(R.id.mapView)).perform(clickXY(530, 500));
        Thread.sleep(2000);

        //Click Set a Reminder Button
        onView(withId(R.id.button_setReminderButton)).perform(click());
        Thread.sleep(2000);

        //Enter reminder information
        onView(withId(R.id.reminderDescription))
                .perform(typeText(STRING_TO_BE_TYPED_EMPTY), closeSoftKeyboard());
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
//                tp.setCurrentHour(HOUR);
//                tp.setCurrentMinute(MINUTE);
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
