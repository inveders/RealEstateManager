package com.inved.realestatemanager.controller.activity;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.inved.realestatemanager.R;
import com.inved.realestatemanager.controller.dialogs.AddAgentDialog;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class FinishRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_register);
        FinishRegisterActivityPermissionsDispatcher.openDialogWithPermissionCheck(this);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void openDialog() {
        AddAgentDialog dialog = new AddAgentDialog();
        dialog.show(getSupportFragmentManager(), "AddAgentDialog");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        FinishRegisterActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
