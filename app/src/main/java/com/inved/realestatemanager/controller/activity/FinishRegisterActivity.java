package com.inved.realestatemanager.controller.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.controller.dialogs.AddAgentDialog;

public class FinishRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_register);
        openDialog();

    }

    private void openDialog() {

        AddAgentDialog dialog = new AddAgentDialog();
        Log.d("debago","MA openDialog");
        dialog.show(getSupportFragmentManager(), "AddAgentDialog");
    }
}
