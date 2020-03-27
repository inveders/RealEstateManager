package com.inved.realestatemanager;

import androidx.test.runner.AndroidJUnitRunner;

import com.inved.realestatemanager.utils.Utils;

import org.junit.Test;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

public class UtilsIntegrationTest extends AndroidJUnitRunner {

    @Test
    public void isInternetAvailable() throws Exception {
        assertTrue(Utils.isInternetAvailable(getInstrumentation().getContext()));
    }

}
