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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.c196.exam.database.DatabaseHelper;
import com.c196.exam.entities.CourseNote;

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
        final EditText content = new EditText(this.getContext());
        title.setHint("Note Subject");
        content.setHint("Note content");
        layout.addView(title);
        layout.addView(content);
        builder.setTitle("Create a new assessment");
        builder.setView(layout)
                // Add action button validates the start/end dates before and after conversion
                .setPositiveButton("Create", (dialog, id) -> {
                    Toast t = new Toast(this.getContext());

                    try{
                        Integer courseId = this.getArguments().getInt("courseId");
                        CourseNote note = new CourseNote(title.getText().toString(), content.getText().toString(), courseId);
                        try (DatabaseHelper dh = new DatabaseHelper(getContext())) {
                            SQLiteDatabase db = dh.getWritableDatabase();
                            Log.d("INFO", "DB Open: " + db.isOpen());
                            boolean result = dh.addCourseNote(note);
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
                    CreateAssessmentDialogFragment.this.getDialog().cancel();
                });
        return builder.create();
    }
}
