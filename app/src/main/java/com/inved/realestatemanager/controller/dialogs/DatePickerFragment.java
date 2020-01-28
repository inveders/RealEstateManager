package com.inved.realestatemanager.controller.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.inved.realestatemanager.controller.fragment.CreateUpdatePropertyFragmentTwo;
import com.inved.realestatemanager.utils.MainApplication;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePickerFragment extends AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener, CreateUpdatePropertyFragmentTwo.OnFragmentInteractionListener {

    private static final String TAG = "debago";
    private final Calendar calendar = Calendar.getInstance();

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (getActivity() != null) {
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, DatePickerFragment.this, yy, mm, dd);
        } else {
            /**Voir si on utilise cela*/
            Log.d(TAG,"getActivity is null in DatePickerFragment");
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(MainApplication.getInstance().getApplicationContext(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, DatePickerFragment.this, yy, mm, dd);
        }

    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        calendar.set(Calendar.YEAR, yy);
        calendar.set(Calendar.MONTH, mm);
        calendar.set(Calendar.DAY_OF_MONTH, dd);
        String selectedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.getTime());

        Log.d(TAG, "onDateSet: " + selectedDate);
        // send date back to the target fragment
        if(getTargetFragment()!=null){
            getTargetFragment().onActivityResult(
                    getTargetRequestCode(),
                    Activity.RESULT_OK,
                    new Intent().putExtra("selectedDate", selectedDate)
            );
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
