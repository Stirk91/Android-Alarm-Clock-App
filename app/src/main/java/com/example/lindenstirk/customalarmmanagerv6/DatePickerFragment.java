package com.example.lindenstirk.customalarmmanagerv6;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import java.util.Calendar;





public class DatePickerFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


      //  calendar.set(Calendar.YEAR, year);
      //  calendar.set(Calendar.MONTH, month);
      //  calendar.set(Calendar.DAY_OF_MONTH, day);

        return new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, (DatePickerDialog.OnDateSetListener)getActivity(), year, month, day);
    }
}
