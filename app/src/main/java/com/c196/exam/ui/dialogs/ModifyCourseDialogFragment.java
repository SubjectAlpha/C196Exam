package com.c196.exam.ui.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.c196.exam.R;
import com.c196.exam.database.DatabaseHelper;
import com.c196.exam.entities.Course;
import com.c196.exam.ui.widgets.DatePicker;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

public class ModifyCourseDialogFragment extends DialogFragment {
    public static String TAG = "ModifyCourseDialog";

    private Course COURSE;

    public ModifyCourseDialogFragment(Course c) {
        COURSE = c;
    }

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
        final Button deleteButton = new Button(this.requireContext());
        final Spinner statusSelect = new Spinner(this.requireContext());

        ArrayList<Course.Statuses> arrayList = new ArrayList<>(Arrays.asList(Course.Statuses.values()));
        ArrayAdapter<Course.Statuses> spinnerAdapter = new ArrayAdapter<>(this.requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, arrayList);
        statusSelect.setAdapter(spinnerAdapter);

        startDate.setInputType(InputType.TYPE_CLASS_DATETIME);
        endDate.setInputType(InputType.TYPE_CLASS_DATETIME);
        instructorEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        instructorPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        instructorPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        className.setHint("Class name");
        instructorFirstName.setHint("Instructor First Name");
        instructorLastName.setHint("Instructor Last Name");
        instructorEmail.setHint("Instructor Email");
        instructorPhone.setHint("Instructor Phone Number");

        className.setText(COURSE.getTitle());
        startDate.setText(COURSE.getStart().split("T")[0]);
        endDate.setText(COURSE.getEnd().split("T")[0]);
        instructorFirstName.setText(COURSE.getInstructorFirstName());
        instructorLastName.setText(COURSE.getInstructorLastName());
        instructorEmail.setText(COURSE.getInstructorEmail());
        instructorPhone.setText(COURSE.getInstructorPhone());
        statusSelect.setSelection(
            spinnerAdapter.getPosition(
                Course.Statuses.valueOf(COURSE.getStatus())
            )
        );

        deleteButton.setText("Delete Course");
        deleteButton.setBackgroundColor(Color.RED);
        deleteButton.setOnClickListener((v) -> {
            try(DatabaseHelper dbh = new DatabaseHelper(this.getContext())){
                boolean result = dbh.deleteCourse(COURSE.getId());
                dbh.close();
                if(result){
                    //Reload activity
                    Activity activity = getActivity();
                    activity.finish();
                    activity.startActivity(this.getActivity().getIntent());
                }else{
                    Toast t = new Toast(this.getContext());
                    t.setText("Delete failed! Please try again.");
                    t.show();
                }


            }catch(Exception ex) {
                Toast t = new Toast(this.getContext());
                t.setText(ex.getMessage());
                t.show();
            }
        });

        layout.addView(className);
        layout.addView(startDate);
        layout.addView(endDate);
        layout.addView(instructorFirstName);
        layout.addView(instructorLastName);
        layout.addView(instructorEmail);
        layout.addView(instructorPhone);
        layout.addView(statusSelect);
        layout.addView(deleteButton);

        builder.setTitle("Modify a class");
        builder.setView(layout)
                // Add action button validates the start/end dates before and after conversion
                .setPositiveButton("Update", (dialog, id) -> {
                    String startDateTime = "";
                    String endDateTime = "";
                    Toast t = new Toast(this.getContext());
                    String courseName = className.getText().toString();
                    String courseStatus = statusSelect.getSelectedItem().toString();
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
                        Course c = new Course(
                            COURSE.getId(),
                            courseName,
                            startDateTime,
                            endDateTime,
                            courseStatus,
                            ciFName,
                            ciLName,
                            ciEmail,
                            ciPhone,
                            COURSE.getTermId(),
                            null,
                            null);
                        try (DatabaseHelper dh = new DatabaseHelper(getContext())) {
                            SQLiteDatabase db = dh.getWritableDatabase();
                            boolean result = dh.updateCourse(c);
                            db.close();

                            //Reload activity
                            Activity activity = getActivity();
                            activity.finish();
                            activity.startActivity(this.getActivity().getIntent());
                        }catch(Exception exx){
                            Log.e("EX", exx.getMessage());
                        }
                    } catch (Exception ex){
                        Log.e("EX", ex.getMessage());
                        t.setText(ex.getMessage());
                        t.show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    ModifyCourseDialogFragment.this.getDialog().cancel();
                });
        return builder.create();
    }
}
