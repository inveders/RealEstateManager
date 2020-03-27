package com.inved.realestatemanager.controller.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.base.BaseActivity;
import com.inved.realestatemanager.sharedpreferences.ManageAgency;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.utils.Utils;

import java.util.Collections;
import java.util.Objects;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
  1. I change activity_second in findViewById by activity_main)
   before:  this.textViewMain = findViewById(R.id.activity_second_activity_text_view_main);

   after:  this.textViewMain = findViewById(R.id.activity_main_activity_text_view_main);
  2. I put a string in the setText because before there was an integer (quantity) in the setText. I had Integer.toString(quantity)
 private void configureTextViewQuantity(){
 int quantity = Utils.convertDollarToEuro(100);
 this.textViewQuantity.setTextSize(20);

 before : this.textViewQuantity.setText(quantity);

 after : this.textViewQuantity.setText(Integer.toString(quantity));
 }*/

@RuntimePermissions
public class MainActivity extends BaseActivity {

    private static final int RC_SIGN_IN = 123;
    CoordinatorLayout coordinatorLayout;

    // --------------
    // LIFE CYCLE
    // --------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if user is signed in and update UI accordingly.
        if (this.getCurrentUser() != null) {
            if (ManageAgency.getAgencyPlaceId(MainApplication.getInstance().getApplicationContext()) == null) {

                this.checkIfUserExistInFirebase();

            } else {
               MainActivityPermissionsDispatcher.startListPropertyActivityWithPermissionCheck(this);
            }
        }
        
        this.connexionButton();

    }

    @Override
    protected int getLayoutContentViewID() {
        return R.layout.activity_main;
    }

    // --------------------
    // INTENT TO OPEN ACTIVITY
    // --------------------

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void startListPropertyActivity() {
        Intent intent = new Intent(this, ListPropertyActivity.class);

        startActivity(intent);
    }

    // --------------
    // PERMISSIONS
    // --------------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
    // --------------------
    // SIGN IN WITH EMAIL ADDRESS
    // --------------------

    private void connexionButton() {
        Button connexionButton = findViewById(R.id.login_button);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        connexionButton.setOnClickListener(view -> {
            if (Utils.isInternetAvailable(MainApplication.getInstance().getApplicationContext())) {
                startSignInActivity();
            } else {
                showSnackBar(this.coordinatorLayout, getString(R.string.error_no_internet));
            }

        });
    }

    // 2 - Launch Sign-In Activity
    private void startSignInActivity() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Collections.singletonList(new AuthUI.IdpConfig.EmailBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.ic_logo_appli_realestate_foreground)
                        .build(),
                RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }


    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                showSnackBar(this.coordinatorLayout, getString(R.string.connection_succeed));

                if (ManageAgency.getAgencyPlaceId(MainApplication.getInstance().getApplicationContext()) == null) {

                    this.checkIfUserExistInFirebase();

                } else {
                    MainActivityPermissionsDispatcher.startListPropertyActivityWithPermissionCheck(this);
                }


            } else { // ERRORS
                if (response == null) {
                    showSnackBar(this.coordinatorLayout, getString(R.string.error_authentication_canceled));
                } else if (Objects.requireNonNull(response.getError()).getErrorCode() != ErrorCodes.NO_NETWORK) {
                    if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                        showSnackBar(this.coordinatorLayout, getString(R.string.error_unknown_error));
                    }
                } else {
                    showSnackBar(this.coordinatorLayout, getString(R.string.error_no_internet));
                }
            }
        }
    }

    private void showSnackBar(CoordinatorLayout coordinatorLayout, String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }




}
