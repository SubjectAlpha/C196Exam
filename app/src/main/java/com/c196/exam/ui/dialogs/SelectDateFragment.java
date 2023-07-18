package com.c196.exam.ui.dialogs;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private final EditText Output;
    public SelectDateFragment(EditText output){
        Output = output;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm, dd);
    }

    @SuppressLint("SetTextI18n")
    public void populateSetDate(int year, int mm, int dd) {
        String month = String.valueOf(mm + 1);
        String day = String.valueOf(dd);

        if (month.length() < 2) {
            month = "0" + month;
        }

        if(day.length() < 2) {
            day = "0" + day;
        }

        Output.setText(year + "-" + month + "-" + day);
    }

}
