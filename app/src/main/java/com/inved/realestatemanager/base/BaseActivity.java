package com.inved.realestatemanager.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.inved.realestatemanager.R;
import com.inved.realestatemanager.controller.activity.FinishRegisterActivity;
import com.inved.realestatemanager.controller.activity.MainActivity;
import com.inved.realestatemanager.firebase.RealEstateAgentHelper;
import com.inved.realestatemanager.injections.Injection;
import com.inved.realestatemanager.injections.ViewModelFactory;
import com.inved.realestatemanager.models.PropertyViewModel;
import com.inved.realestatemanager.utils.MainApplication;
import com.inved.realestatemanager.utils.ManageAgency;

import java.io.File;


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

    protected void logoutAlertDialog(){
        new AlertDialog.Builder(this, R.style.MyDialogTheme)
                .setMessage(R.string.alert_dialog_logout)
                .setPositiveButton(R.string.alert_dialog_yes, (dialogInterface, i) -> logout())
                .setNegativeButton(R.string.alert_dialog_no, null)
                .show();
    }

    private void logout(){
        AuthUI.getInstance()

                .signOut(this)
                .addOnSuccessListener(this, this.updateUIAfterRESTRequestsCompleted());

    }

    // Create OnCompleteListener called after tasks ended
    private OnSuccessListener<Void> updateUIAfterRESTRequestsCompleted() {
        return aVoid -> {
            startMainActivity();
            getApplicationContext().deleteDatabase("MyDatabase.db");
            File storageDir = MainApplication.getInstance().getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            assert storageDir != null;
            if (storageDir.isDirectory())
            {
                String[] children = storageDir.list();
                for (int i = 0; i < children.length; i++)
                {
                    new File(storageDir, children[i]).delete();
                }
            }
            Log.d("debago","we delete database");
            finish();
        };
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}

