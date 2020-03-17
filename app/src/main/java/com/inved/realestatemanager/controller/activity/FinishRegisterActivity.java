package com.inved.realestatemanager.controller.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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
        dialog.show(getSupportFragmentManager(), "AddAgentDialog");
    }
}
