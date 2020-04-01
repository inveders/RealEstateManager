package com.inved.realestatemanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.inved.realestatemanager.utils.Utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.LooperMode;
import org.robolectric.shadows.ShadowConnectivityManager;
import org.robolectric.shadows.ShadowNetworkInfo;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@LooperMode(LooperMode.Mode.PAUSED)
@Config(sdk = Build.VERSION_CODES.O_MR1)
public class InternetConnexionTest {

    private ShadowConnectivityManager shadowConnectivityManager;

      @Before
      public void setUp() throws IOException {
          ConnectivityManager connectivityManager = getConnectivityManager();
          shadowConnectivityManager = Shadows.shadowOf(connectivityManager);

      }

      @Test
      public void availableConnexionTest() {

          NetworkInfo networkInfo =  ShadowNetworkInfo.newInstance(NetworkInfo.DetailedState.CONNECTED, ConnectivityManager.TYPE_WIFI, 0, true, NetworkInfo.State.CONNECTED);
          shadowConnectivityManager.setActiveNetworkInfo(networkInfo);
          assertTrue(Utils.isInternetAvailable(RuntimeEnvironment.systemContext));

      }

    @Test
    public void noAvailableConnexionTest() {

        NetworkInfo networkInfo =  ShadowNetworkInfo.newInstance(NetworkInfo.DetailedState.DISCONNECTED, ConnectivityManager.TYPE_WIFI, 0, false, NetworkInfo.State.DISCONNECTED);
        shadowConnectivityManager.setActiveNetworkInfo(networkInfo);
        assertFalse(Utils.isInternetAvailable(RuntimeEnvironment.systemContext));

    }

      private ConnectivityManager getConnectivityManager() {
          return (ConnectivityManager)     RuntimeEnvironment.systemContext.getSystemService(Context.CONNECTIVITY_SERVICE);
      }

}
