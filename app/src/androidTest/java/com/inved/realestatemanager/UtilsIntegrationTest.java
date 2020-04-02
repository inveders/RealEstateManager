package com.inved.realestatemanager;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnitRunner;

import com.inved.realestatemanager.controller.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


public class UtilsIntegrationTest extends AndroidJUnitRunner {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void isInternetAvailable() throws Exception {
        onView(withId(R.id.login_button)).perform(click());
    }

}
