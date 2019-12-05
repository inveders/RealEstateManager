package com.inved.realestatemanager.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.firebase.RealEstateAgentHelper;
import com.inved.realestatemanager.utils.ManageAgency;

import java.util.Collections;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 123;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button connexionButton = findViewById(R.id.login_button);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        Log.d("debago", "MA onCreate");
        connexionButton.setOnClickListener(view -> startSignInActivity());

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d("debago", "MA onStart : "+currentUser.getEmail());
            checkIfUserExistInFirebase(currentUser.getEmail());


        }


    }

    private void startListPropertyActivity() {
        Intent intent = new Intent(this, ListPropertyActivity.class);

        startActivity(intent);
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

    private void showSnackBar(CoordinatorLayout coordinatorLayout, String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
           // Log.d("debago", "MA resultCode");
            if (resultCode == RESULT_OK) { // SUCCESS
                showSnackBar(this.coordinatorLayout, getString(R.string.connection_succeed));
               // Log.d("debago", "MA onsuccess");
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    checkIfUserExistInFirebase(FirebaseAuth.getInstance().getCurrentUser().getEmail());


                }

               // finish();

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

    private void startFinishRegisterActivity() {

        Intent intent = new Intent(this, FinishRegisterActivity.class);

        startActivity(intent);
    }

    private void checkIfUserExistInFirebase(String email){
        RealEstateAgentHelper.getAgentWhateverAgency(email).get().addOnCompleteListener(task -> {

        //    Log.d("debago", "MA CHECK TASK : "+ Objects.requireNonNull(task.getResult()));

            if (task.isSuccessful()) {
                if (task.getResult() != null) {
                 //   Log.d("debago", "MA CHECK task successful");
                    if (task.getResult().getDocuments().size() != 0) {
                        startListPropertyActivity();
                     //   Log.d("debago", "MA CHECK successful, firstname is : " + task.getResult().getDocuments().get(0).getString("firstname"));
                    } else {
                   //     Log.d("debago", "MA CHECK notSuccessful, size is: " + task.getResult().getDocuments().size());
                        ManageAgency.saveAgencyPlaceId(getApplicationContext(),null);
                        startFinishRegisterActivity();
                    }
                }


            }else{
             //   Log.d("debago", "MA CHECK task IS NOT successful");
            }


        });
    }


}
