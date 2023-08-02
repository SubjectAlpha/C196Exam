package com.c196.exam.ui.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.TextClassifier;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.c196.exam.database.DatabaseHelper;
import com.c196.exam.entities.Course;
import com.c196.exam.entities.Term;
import com.c196.exam.ui.widgets.DatePicker;

import java.time.Instant;

public class CreateClassDialogFragment extends DialogFragment {
    public static String TAG = "CreateClassDialog";

    @Override
    public void onViewCreated(@NonNull View v, Bundle savedInstanceState) { }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Building the class creation dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        LinearLayout layout = new LinearLayout(this.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText className = new EditText(this.requireContext());
        final DatePicker startDate = new DatePicker(this.requireContext(), this.getChildFragmentManager(), "Start Date");
        final DatePicker endDate = new DatePicker(this.requireContext(), this.getChildFragmentManager(), "End Date");
        final EditText instructorFirstName = new EditText(this.requireContext());
        final EditText instructorLastName = new EditText(this.requireContext());
        final EditText instructorEmail = new EditText(this.requireContext());
        final EditText instructorPhone = new EditText(this.requireContext());

        startDate.setInputType(InputType.TYPE_CLASS_DATETIME);
        endDate.setInputType(InputType.TYPE_CLASS_DATETIME);
        instructorEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        instructorPhone.setInputType(InputType.TYPE_CLASS_PHONE);

        className.setHint("Class name");
        instructorFirstName.setHint("Instructor First Name");
        instructorLastName.setHint("Instructor First Name");
        instructorEmail.setHint("Instructor Email");
        instructorPhone.setHint("Instructor Phone Number");
        layout.addView(className);
        layout.addView(startDate);
        layout.addView(endDate);
        layout.addView(instructorFirstName);
        layout.addView(instructorLastName);
        layout.addView(instructorEmail);
        layout.addView(instructorPhone);
        builder.setTitle("Create a new class");
        builder.setView(layout)
                // Add action button validates the start/end dates before and after conversion
                .setPositiveButton("Create", (dialog, id) -> {
                    String startDateTime = "";
                    String endDateTime = "";
                    Toast t = new Toast(this.getContext());
                    String courseName = className.getText().toString();
                    String courseStatus = Course.Statuses.PENDING.name();
                    String ciFName = instructorFirstName.getText().toString();
                    String ciLName = instructorLastName.getText().toString();
                    String ciEmail = instructorEmail.getText().toString();
                    String ciPhone = instructorPhone.getText().toString();

                    try{
                        String startFormatted = startDate.getText().toString() + "T00:00:00Z";
                        String endFormatted = endDate.getText().toString() + "T00:00:00Z";
                        Instant startInstant = Instant.parse(startFormatted);
                        Instant endInstant = Instant.parse(endFormatted);

                        if(startInstant.isAfter(endInstant)){
                            t.setText("The course can not start after it ends.");
                            t.show();
                            return;
                        }

                        if(courseName.isEmpty() || ciFName.isEmpty() || ciLName.isEmpty() || ciEmail.isEmpty() || ciPhone.isEmpty() ){
                            t.setText("You must fill out all fields.");
                            t.show();
                            return;
                        }

                        startDateTime = startInstant.toString();
                        endDateTime = endInstant.toString();

                    } catch (Exception ex){
                        t.setText("Please ensure your start and end dates are in yyyy-MM-dd format.");
                        t.show();
                        return;
                    }

                    try{
                        Integer termId = this.getArguments().getInt("termId");
                        Course c = new Course(courseName,
                                startDateTime,
                                endDateTime,
                                courseStatus,
                                ciFName,
                                ciLName,
                                ciEmail,
                                ciPhone,
                                termId,
                                null);
                        try (DatabaseHelper dh = new DatabaseHelper(getContext())) {
                            SQLiteDatabase db = dh.getWritableDatabase();
                            Log.d("INFO", "DB Open: " + db.isOpen());
                            boolean result = dh.addCourse(c);
                            Log.d("DB RESULT", "" + result);
                            db.close();

                            //Reload activity
                            Activity activity = getActivity();
                            activity.finish();
                            activity.startActivity(this.getActivity().getIntent());
                        }
                    } catch (Exception ex){
                        Log.e("EX", ex.getMessage());
                        t.setText(ex.getMessage());
                        t.show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    CreateClassDialogFragment.this.getDialog().cancel();
                });
        return builder.create();
    }
}
