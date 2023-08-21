package com.c196.exam.ui.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.c196.exam.database.DatabaseHelper;
import com.c196.exam.entities.Assessment;
import com.c196.exam.ui.widgets.DatePicker;

public class ModifyAssessmentDialogFragment extends DialogFragment {
    public static String TAG = "CreateAssessmentDialog";

    private Assessment assessment;

    public ModifyAssessmentDialogFragment(Assessment assessment) {
        this.assessment = assessment;
    }

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
        final EditText start = new DatePicker(this.getContext(), this.getChildFragmentManager(), "Start Date");
        final EditText end = new DatePicker(this.getContext(), this.getChildFragmentManager(), "End Date");
        final RadioGroup radioGroup = new RadioGroup(this.getContext());
        final RadioButton performanceButton = new RadioButton(this.getContext());
        final RadioButton objectiveButton = new RadioButton(this.getContext());
        final Button deleteButton = new Button(this.requireContext());

        performanceButton.setText("Performance Assessment");
        objectiveButton.setText("Objective Assessment");
        radioGroup.addView(performanceButton);
        radioGroup.addView(objectiveButton);

        title.setHint("Assessment Title");
        start.setHint("Start Date");
        end.setHint("End Date");

        title.setText(assessment.getTitle());
        start.setText(assessment.getStart());
        end.setText(assessment.getEnd());

        if(assessment.is_performance()){
            performanceButton.setChecked(true);
        }else{
            objectiveButton.setChecked(true);
        }

        deleteButton.setText("Delete Assessment");
        deleteButton.setBackgroundColor(Color.RED);
        deleteButton.setOnClickListener((v) -> {
            try(DatabaseHelper dbh = new DatabaseHelper(this.getContext())){
                boolean result = dbh.deleteAssessment(assessment.getId());
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

        layout.addView(title);
        layout.addView(start);
        layout.addView(end);
        layout.addView(radioGroup);
        layout.addView(deleteButton);
        builder.setTitle("Modify assessment");
        builder.setView(layout)
                // Add action button validates the start/end dates before and after conversion
                .setPositiveButton("Update", (dialog, id) -> {
                    Toast t = new Toast(this.getContext());

                    try{
                        boolean isPerformance = performanceButton.isSelected();
                        Assessment a = new Assessment(
                                assessment.getId(),
                                title.getText().toString(),
                                start.getText().toString(),
                                end.getText().toString(),
                                isPerformance, assessment.getCourseId());
                        try (DatabaseHelper dh = new DatabaseHelper(getContext())) {
                            SQLiteDatabase db = dh.getWritableDatabase();
                            boolean result = dh.updateAssessment(a);
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
                    ModifyAssessmentDialogFragment.this.getDialog().cancel();
                });
        return builder.create();
    }
}
