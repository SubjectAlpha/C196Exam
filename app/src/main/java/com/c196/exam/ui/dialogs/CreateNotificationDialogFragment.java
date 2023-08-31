package com.c196.exam.ui.dialogs;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.c196.exam.ui.widgets.DatePicker;
import com.c196.exam.ui.widgets.TimePicker;
import com.c196.exam.utility.NotificationService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;

public class CreateNotificationDialogFragment extends DialogFragment {
    public static String TAG = "CreateNotificationDialog";

    @Override
    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) { }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Building the term creation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        LinearLayout layout = new LinearLayout(this.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText title = new EditText(this.getContext());
        final EditText content = new EditText(this.getContext());
        final DatePicker datePicker = new DatePicker(requireContext(), getChildFragmentManager(), "Notification Date");
        final TimePicker timePicker = new TimePicker(requireContext(), getChildFragmentManager(), "Notification Time");
        title.setHint("Notification title");
        content.setHint("Notification message");
        layout.addView(title);
        layout.addView(content);
        layout.addView(datePicker);
        layout.addView(timePicker);
        builder.setTitle("Create a new notification");
        builder.setView(layout)
                // Add action button validates the start/end dates before and after conversion
                .setPositiveButton("Create", (dialog, id) -> {
                    Toast toast = new Toast(this.getContext());

                    try{

                        ZonedDateTime zdt = LocalDateTime.parse(datePicker.getText() + "T" + timePicker.getText(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).atZone(ZoneOffset.systemDefault());
                        ZonedDateTime now = ZonedDateTime.now();
                        if(now.isAfter(zdt)){
                            toast.setText("Your notification must be set in the future.");
                            toast.show();
                            return;
                        }

                        //Integer courseId = this.getArguments().getInt("courseId");
                        AlarmManager am = (AlarmManager) requireContext().getSystemService(ALARM_SERVICE);
                        NotificationService ns = new NotificationService(requireContext());
                        long triggerTime = zdt.toEpochSecond() * 1000;
                        ns.scheduleNotification(requireContext(), am, triggerTime, title.getText().toString(), content.getText().toString());
                    } catch (Exception ex){
                        Log.e("EX", ex.getMessage());
                        toast.setText(ex.getMessage());
                        toast.show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    CreateNotificationDialogFragment.this.getDialog().cancel();
                });
        return builder.create();
    }
}
