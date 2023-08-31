package com.c196.exam.ui.dialogs;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.textclassifier.TextClassifier;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.Calendar;

public class SelectTimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private final EditText Output;
    public SelectTimeFragment(EditText output){
        Output = output;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final LocalTime now = Instant.now().atZone(ZoneOffset.systemDefault()).toLocalTime();
        return new TimePickerDialog(getActivity(), this, now.getHour(), now.getMinute(), false);
    }

    public void populateSetTime(int hour, int minute) {
        String hourFormat = String.valueOf(hour);
        String minuteFormat = String.valueOf(minute);

        if(hour == 0){
            hourFormat = "00";
        }

        if(minute < 10) {
            minuteFormat = "0" + minute;
        }

        Output.setText(hourFormat + ":" + minuteFormat);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        populateSetTime(hourOfDay, minute);
    }
}
