package com.inved.realestatemanager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnitRunner;

import com.google.gson.annotations.Until;
import com.inved.realestatemanager.dao.RealEstateManagerDatabase;
import com.inved.realestatemanager.utils.Utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;


public class UtilsIntegrationTest extends AndroidJUnitRunner {

    @Before
    public void initNoConnection() {
        Context context=InstrumentationRegistry.getInstrumentation().getTargetContext();
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);




    }

    @Test
    public void isInternetAvailable() throws Exception {
        assertTrue(Utils.isInternetAvailable(getInstrumentation().getContext()));
    }

}
