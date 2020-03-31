package com.inved.realestatemanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowConnectivityManager;
import org.robolectric.shadows.ShadowNetworkInfo;

@RunWith(RobolectricTestRunner.class)
public class ShadowConnectivityManagerTest {
    @Before
    public void before() {

        ConnectivityManager cm = (ConnectivityManager)
                Robolectric.application.getSystemService(Context.CONNECTIVITY_SERVICE);

        ShadowConnectivityManager shadowCM = Robolectric.shadowOf(cm);

        shadowCM.setNetworkInfo(
                ConnectivityManager.TYPE_MOBILE,
                ShadowNetworkInfo.newInstance(
                        NetworkInfo.DetailedState.DISCONNECTED,
                        ConnectivityManager.TYPE_MOBILE,
                        ConnectivityManager.TYPE_MOBILE_MMS,
                        true,
                        false));


    }

    @Test
    public void mobileIsDisconnected() {
        ConnectivityManager cm = (ConnectivityManager)
                Robolectric.application.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo mobileInfo =
                cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        assertThat(mobileInfo.isConnected()).isFalse();
    }
}
