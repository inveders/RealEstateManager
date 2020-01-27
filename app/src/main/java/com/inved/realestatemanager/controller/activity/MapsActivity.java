package com.inved.realestatemanager.controller.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.domain.SplitString;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.GeocodingViewModel;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.PropertyViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.inved.realestatemanager.view.PropertyListViewHolder.PROPERTY_ID;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener {

    //GOOGLE GEOCODING
    private GoogleMap mGoogleMap;
    //  private Marker mMarker;
    List<Double> latitudeList = new ArrayList<>();
    List<Double> longitudeList = new ArrayList<>();

    //FOR LOCATION

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1500; // 1000 meters for tests, after come back to 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60; // 1 minute*/

    //View Model
    private PropertyViewModel propertyViewModel;
    private GeocodingViewModel geocodingViewModel;

    // --------------
    // LIFE CYCLE AND VIEW MODEL
    // --------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        checkLocation();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        this.configureToolbar();
        this.configureViewModel();
        this.initializeMap();


    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.propertyViewModel = ViewModelProviders.of(this, viewModelFactory).get(PropertyViewModel.class);

        this.geocodingViewModel = ViewModelProviders.of(this).get(GeocodingViewModel.class);
    }

    // --------------
    // TOOLBAR
    // --------------

    private void configureToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbar_map);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.page_name_activity_maps));
        }
    }

    // --------------
    // CONFIGURE MAP
    // --------------

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mGoogleMap.setOnMarkerClickListener(this);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.setMyLocationEnabled(true);

        LatLng initialPosition = new LatLng(0, 0);

        mGoogleMap.addMarker(new MarkerOptions()
                .position(initialPosition));


    }


    private void initializeMap() {

        propertyViewModel.getAllPropertiesForOneAgent(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()).observe(this, properties -> {

            String streetNumber;
            String streetName;
            String zipCode;
            String town;
            String country;
            String addressToConvert;
            SplitString splitString = new SplitString();
            List<String> addressList = new ArrayList<>();
            List<String> propertyList = new ArrayList<>();

            for (Property p : properties) {

                streetNumber = p.getStreetNumber();
                streetName = p.getStreetName();
                zipCode = p.getZipCode();
                town = p.getTownProperty();
                country = p.getCountry();
                addressToConvert = streetNumber + " " + streetName + " " + zipCode + " " + town + " " + country;
                String addressFormatted = splitString.replaceAllSpacesOrCommaByAddition(addressToConvert);
                String propertyId = p.getPropertyId();


                addressList.add(addressFormatted);
                propertyList.add(propertyId);

            }

            geocodingSearch(addressList);

            Handler handler = new Handler();
            handler.postDelayed(() -> {
                if(latitudeList!=null){
                    for (int i = 0; i <latitudeList.size() ; i++) {
                        customizeMarker(propertyList.get(i),latitudeList.get(i),longitudeList.get(i));
                    }
                }
            }, 4000);


        });


    }


    private void geocodingSearch(List<String> addressFormatted) {

        for (int i = 0; i <addressFormatted.size() ; i++) {
            geocodingViewModel.getLatLngWithAddress(addressFormatted.get(i)).observe(this, results -> {
                if (results.size() != 0) {

                    double latitude = results.get(0).getGeometry().getLocation().getLat();
                    double longitude = results.get(0).getGeometry().getLocation().getLng();

                    if(!latitudeList.contains(latitude)){
                        latitudeList.add(latitude);
                        longitudeList.add(longitude);
                    }

                } else {
                    Log.d("debago", "Geocoding no result ");
                }

            });
        }

    }

    private void customizeMarker(String propertyId, double lat, double longi) {

        LatLng latLng = new LatLng(lat, longi);

        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.snippet(String.valueOf(propertyId));
        markerOptions.position(latLng);


        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        mGoogleMap.addMarker(markerOptions).setTag(propertyId);

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        if (marker.getSnippet()!=null){

            startDetailActivity(marker.getSnippet());
        }
        return true;
    }



    private void moveCamera(double myCurrentLat, double myCurrentLongi) {

        int mZoom = 12;
        int mBearing = 4;
        int mTilt = 35;

        LatLng latLngCurrent = new LatLng(myCurrentLat, myCurrentLongi);
        Log.d("debago","mycurrent lat is "+myCurrentLat);
        CameraPosition Liberty = CameraPosition.builder().target(latLngCurrent).zoom(mZoom).bearing(mBearing).tilt(mTilt).build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLngCurrent));

    }


    // --------------
    // LOCATION
    // --------------

    @SuppressLint("MissingPermission")

    public void checkLocation() {

        //Subscribe to providers
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (lm != null) {
            if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            }

            if (lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
                lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            }

            if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            }
            if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            }

        }

    }


    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        moveCamera(latitude, longitude);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void buildAlertMessageNoGps() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MyDialogTheme);
        builder.setMessage(R.string.alert_dialog_gps_disabled)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.alert_dialog_yes), (dialog, id) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                )
                .setNegativeButton(getString(R.string.alert_dialog_no), (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }


    // --------------
    // INTENT TO OPEN ACTIVITY
    // --------------

    // Launch View Place Activity
    private void startDetailActivity(String propertyId) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(PROPERTY_ID, propertyId);
        startActivity(intent);
    }


}
