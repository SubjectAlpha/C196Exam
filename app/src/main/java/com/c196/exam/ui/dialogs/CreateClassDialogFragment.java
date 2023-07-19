package com.c196.exam.ui.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
        final EditText startDate = new DatePicker(this.requireContext(), this.getChildFragmentManager(), "Start Date");
        final EditText endDate = new DatePicker(this.requireContext(), this.getChildFragmentManager(), "End Date");
        final EditText classNote = new EditText(this.requireContext());
        className.setHint("Class name");
        classNote.setHint("Required note for class");
        layout.addView(className);
        layout.addView(startDate);
        layout.addView(endDate);
        layout.addView(classNote);
        builder.setTitle("Create a new class");
        builder.setView(layout)
                // Add action button validates the start/end dates before and after conversion
                .setPositiveButton("Create", (dialog, id) -> {
                    String startDateTime = "";
                    String endDateTime = "";
                    try{
                        String start = startDate.getText().toString() + "T00:00:00Z";
                        String end = endDate.getText().toString() + "T00:00:00Z";
                        startDateTime = Instant.parse(start).toString();
                        endDateTime = Instant.parse(end).toString();
                    } catch (Exception ex){
                        Toast t = new Toast(this.getContext());
                        t.setText("Please ensure your start and end dates are in yyyy-MM-dd format.");
                        t.show();
                    }

                    try{
                        Integer termId = this.getArguments().getInt("termId");
                        Course c = new Course(termId, className.getText().toString(), startDateTime, endDateTime);
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
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    CreateClassDialogFragment.this.getDialog().cancel();
                });
        return builder.create();
    }
}