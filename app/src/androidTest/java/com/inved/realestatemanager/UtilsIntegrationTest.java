package com.inved.realestatemanager;

import android.content.Context;
import android.os.Bundle;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnitRunner;

import com.inved.realestatemanager.utils.Utils;
import com.linkedin.android.testbutler.TestButler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

public class UtilsIntegrationTest extends AndroidJUnitRunner {

    @Before
    public void setUp() throws Exception {
        final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        TestButler.setGsmState(false);
        TestButler.setWifiState(false);
    }

    @Override
    public void onStart() {
        TestButler.setup(InstrumentationRegistry.getInstrumentation().getTargetContext());

        super.onStart();
    }

    @Override
    public void finish(int resultCode, Bundle results) {
        TestButler.teardown(InstrumentationRegistry.getInstrumentation().getTargetContext());

        super.finish(resultCode, results);
    }

    @Test
    public void isInternetAvailable() throws Exception {
        assertTrue(Utils.isInternetAvailable(getInstrumentation().getContext()));
    }

    @Test
    public void verifyBehaviorWithNoWifi() {
        assertTrue(Utils.isInternetAvailable(InstrumentationRegistry.getInstrumentation().getContext()));
    }

    @After
    public void teardown() {
        TestButler.setGsmState(true);
        TestButler.setWifiState(true);
    }

}
