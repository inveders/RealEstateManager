package com.inved.realestatemanager.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.controller.activity.FinishRegisterActivity;
import com.inved.realestatemanager.controller.activity.ListPropertyActivity;
import com.inved.realestatemanager.controller.activity.MainActivity;
import com.inved.realestatemanager.firebase.RealEstateAgentHelper;
import com.inved.realestatemanager.sharedpreferences.ManageAgency;
import com.inved.realestatemanager.sharedpreferences.ManageDatabaseFilling;
import com.inved.realestatemanager.utils.MainApplication;


public abstract class BaseActivity extends AppCompatActivity {

    // --------------------
    // LIFE CYCLE
    // --------------------

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutContentViewID());

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

    }

    protected abstract int getLayoutContentViewID();


    // --------------------
    // FIREBASE
    // --------------------

    @Nullable
    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    @Nullable
    protected String getUserEmail() {
        if (getCurrentUser() != null) {
            return getCurrentUser().getEmail();
        } else {
            return null;
        }
    }

    protected void checkIfUserExistInFirebase() {
        RealEstateAgentHelper.getAgentWhateverAgency(getUserEmail()).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                if (task.getResult() != null) {

                    if (task.getResult().getDocuments().size() == 0) {
                        ManageAgency.saveAgencyPlaceId(this, null);
                        startFinishRegisterActivity();
                    }else{
                        ManageAgency.saveAgencyPlaceId(this, task.getResult().getDocuments().get(0).getString("agencyPlaceId"));
                        ManageAgency.saveAgencyName(this, task.getResult().getDocuments().get(0).getString("agencyName"));
                        startListPropertyActivity();

                    }
                }

            }


        });
    }



    // --------------------
    // LOGOUT
    // --------------------

    protected void logoutAlertDialog() {
        new AlertDialog.Builder(this, R.style.MyDialogTheme)
                .setMessage(R.string.alert_dialog_logout)
                .setPositiveButton(R.string.alert_dialog_yes, (dialogInterface, i) -> logout())
                .setNegativeButton(R.string.alert_dialog_no, null)
                .show();
    }



    private void logout() {
        AuthUI.getInstance()

                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted());
    }

    // Create OnCompleteListener called after tasks ended
    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted() {
        return aVoid -> {
            startMainActivity();
            ManageDatabaseFilling.saveDatabaseFilledState(MainApplication.getInstance().getApplicationContext(),false);
            finish();
        };
    }

    // --------------------
    // INTENTS TO START ACTIVITY
    // --------------------

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void startFinishRegisterActivity() {

        Intent intent = new Intent(this, FinishRegisterActivity.class);

        startActivity(intent);
    }

    private void startListPropertyActivity() {

        Intent intent = new Intent(this, ListPropertyActivity.class);

        startActivity(intent);
    }

}

