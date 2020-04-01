package com.inved.realestatemanager;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnitRunner;

import com.inved.realestatemanager.controller.activity.MainActivity;
import com.inved.realestatemanager.utils.Utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;


public class UtilsIntegrationTest extends AndroidJUnitRunner {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initNoConnection() {
        Utils.enableData(getInstrumentation().getContext(),true);
    }

    @Test
    public void isInternetAvailable() throws Exception {
        onView(withId(R.id.login_button)).perform(click());
    }

}
