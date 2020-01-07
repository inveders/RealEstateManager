package com.inved.realestatemanager.base;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.inved.realestatemanager.controller.activity.FinishRegisterActivity;
import com.inved.realestatemanager.firebase.RealEstateAgentHelper;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.utils.ManageAgency;


public abstract class BaseActivity extends AppCompatActivity {

    // --------------------
    // LIFE CYCLE
    // --------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutContentViewID());
    }

    protected abstract int getLayoutContentViewID();

    // --------------------
    // UTILS
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

    protected void checkIfUserExistInFirebase(){
        RealEstateAgentHelper.getAgentWhateverAgency(getUserEmail()).get().addOnCompleteListener(task -> {

            //  Log.d("debago", "MA CHECK TASK : "+ Objects.requireNonNull(task.getResult()));

            if (task.isSuccessful()) {
                if (task.getResult() != null) {
                    //   Log.d("debago", "MA CHECK task successful");
                    if (task.getResult().getDocuments().size() != 0) {
                        //     Log.d("debago", "MA CHECK successful, firstname is : " + task.getResult().getDocuments().get(0).getString("firstname"));
                    } else {
                        //  Log.d("debago", "MA CHECK notSuccessful, size is: " + task.getResult().getDocuments().size());
                        ManageAgency.saveAgencyPlaceId(this,null);
                        startFinishRegisterActivity();
                    }
                }


            }else{
                Log.d("debago", "MA CHECK task IS NOT successful");
            }


        });
    }

    private void startFinishRegisterActivity() {

        Intent intent = new Intent(this, FinishRegisterActivity.class);

        startActivity(intent);
    }

}

