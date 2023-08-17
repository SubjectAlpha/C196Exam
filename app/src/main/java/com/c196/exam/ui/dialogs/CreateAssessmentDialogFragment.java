package com.c196.exam.ui.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.c196.exam.R;
import com.c196.exam.database.DatabaseHelper;
import com.c196.exam.entities.Assessment;
import com.c196.exam.entities.CourseNote;
import com.c196.exam.ui.widgets.DatePicker;

public class CreateAssessmentDialogFragment extends DialogFragment {
    public static String TAG = "CreateAssessmentDialog";

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
        performanceButton.setText("Performance Assessment");
        objectiveButton.setText("Objective Assessment");
        radioGroup.addView(performanceButton);
        radioGroup.addView(objectiveButton);

        title.setHint("Note Subject");
        layout.addView(title);
        layout.addView(radioGroup);
        builder.setTitle("Create a new assessment");
        builder.setView(layout)
                // Add action button validates the start/end dates before and after conversion
                .setPositiveButton("Create", (dialog, id) -> {
                    Toast t = new Toast(this.getContext());

                    try{
                        Integer courseId = this.getArguments().getInt("courseId");
                        boolean isPerformance = performanceButton.isSelected();
                        Assessment a = new Assessment(
                                title.getText().toString(),
                                start.getText().toString(),
                                end.getText().toString(),
                                isPerformance, courseId);
                        try (DatabaseHelper dh = new DatabaseHelper(getContext())) {
                            SQLiteDatabase db = dh.getWritableDatabase();
                            boolean result = dh.addAssessment(a);
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
                    CreateAssessmentDialogFragment.this.getDialog().cancel();
                });
        return builder.create();
    }
}
