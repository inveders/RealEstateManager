package com.inved.realestatemanager.controller.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
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
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.domain.SplitString;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.GeocodingViewModel;
import com.inved.realestatemanager.models.Property;
import com.inved.realestatemanager.models.PropertyViewModel;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.inved.realestatemanager.property.PropertyListViewHolder.PROPERTY_ID;

@RuntimePermissions
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    //GOOGLE GEOCODING
    private GoogleMap mGoogleMap;
    private Marker mMarker;

    //FOR LOCATION

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1500; // 1000 meters for tests, after come back to 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60; // 1 minute*/

    //View Model
    private PropertyViewModel propertyViewModel;
    private GeocodingViewModel geocodingViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        MapsActivityPermissionsDispatcher.checkLocationWithPermissionCheck(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        this.configureToolbar();
        this.configureViewModel();
        this.initializeMap();
    }


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

        mGoogleMap.setOnMarkerClickListener(marker -> false);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.setMyLocationEnabled(true);

        LatLng initialPosition = new LatLng(0, 0);

        mMarker = mGoogleMap.addMarker(new MarkerOptions()
                .position(initialPosition));
    }

    private void configureToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbar);

     /*   setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }*/
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.propertyViewModel = ViewModelProviders.of(this, viewModelFactory).get(PropertyViewModel.class);

        this.geocodingViewModel = ViewModelProviders.of(this).get(GeocodingViewModel.class);
    }

    private void initializeMap() {

        propertyViewModel.getProperties(1).observe(this, properties -> {

            String streetNumber;
            String streetName;
            String zipCode;
            String town;
            String country;
            String addressToConvert;
            SplitString splitString = new SplitString();

            for (Property p : properties) {
                streetNumber = p.getStreetNumber();
                streetName = p.getStreetName();
                zipCode = p.getZipCode();
                town = p.getTownProperty();
                country = p.getCountry();
                addressToConvert = streetNumber + " " + streetName + " " + zipCode + " " + town + " " + country;
                String addressFormatted = splitString.replaceAllSpacesOrCommaByAddition(addressToConvert);

                geocodingViewModel.getLatLngWithAddress(addressFormatted).observe(this, results -> {
                    if(results.size()!=0){
                        double latitude = results.get(0).getGeometry().getLocation().getLat();
                        double longitude = results.get(0).getGeometry().getLocation().getLng();
                        String placeId = results.get(0).getPlaceId();
                        customizeMarker(placeId, latitude, longitude);
                    }else{
                        Log.d("debago","Geocoding no result ");
                    }

                });
            }
        });

    }

    private void customizeMarker(String placeId, double lat, double longi) {

        if (mGoogleMap != null) {
            mGoogleMap.clear();
        }

        if (mMarker != null) {
            mMarker.remove();
        }

        LatLng latLng = new LatLng(lat, longi);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.snippet(placeId);

        markerOptions.position(latLng);

        //creating and getting restaurant information

        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        mGoogleMap.addMarker(markerOptions);

        if(mGoogleMap !=null)

        {
            //Configure action on marker click
            mGoogleMap.setOnMarkerClickListener(marker -> {

                if (marker.getSnippet() != null) {

                    startDetailActivity(marker.getSnippet());

                }
                return true;
            });
        }
    }

    // Launch View Place Activity
    private void startDetailActivity(String placeId) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(PROPERTY_ID, placeId);
        startActivity(intent);
    }

    private void moveCamera(double myCurrentLat,double myCurrentLongi) {

        int mZoom = 12;
        int mBearing = 4;
        int mTilt = 35;

        LatLng latLngCurrent = new LatLng(myCurrentLat, myCurrentLongi);

        CameraPosition Liberty = CameraPosition.builder().target(latLngCurrent).zoom(mZoom).bearing(mBearing).tilt(mTilt).build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLngCurrent));

    }


    //LOCATION

    @SuppressLint("MissingPermission")
    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
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
            if ( !lm.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                buildAlertMessageNoGps();
            }

        }

    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d("debago","onlocation changed "+location.getLongitude());
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        moveCamera(latitude,longitude);

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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alert_dialog_gps_disabled)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.alert_dialog_yes), (dialog, id) -> {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                        }
                )
                .setNegativeButton(getString(R.string.alert_dialog_no), (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MapsActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
