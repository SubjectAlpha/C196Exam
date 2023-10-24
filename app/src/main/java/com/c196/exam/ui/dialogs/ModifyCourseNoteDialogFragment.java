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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.c196.exam.database.DatabaseHelper;
import com.c196.exam.entities.CourseNote;
import com.c196.exam.entities.Term;
import com.c196.exam.ui.widgets.DatePicker;

import java.time.Instant;

public class ModifyCourseNoteDialogFragment extends DialogFragment {
    public static String TAG = "ModifyCourseNoteDialog";

    private final CourseNote courseNote;

    public ModifyCourseNoteDialogFragment(CourseNote n){
        this.courseNote = n;
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
        final EditText noteTitle = new EditText(this.getContext());
        final EditText noteContent = new EditText(this.requireContext());
        final Button deleteButton = new Button(this.getContext());
        noteTitle.setHint("Note title");
        noteTitle.setText(this.courseNote.getTitle());
        noteContent.setText(this.courseNote.getContent());

        deleteButton.setText("Delete Note");
        deleteButton.setBackgroundColor(Color.RED);
        deleteButton.setOnClickListener((v) -> {
            try(DatabaseHelper dbh = new DatabaseHelper(this.getContext())){
                boolean result = dbh.deleteNote(this.courseNote.getId());
                dbh.close();
                if(result){
                    //Reload activity
                    Activity activity = getActivity();
                    activity.finish();
                    activity.startActivity(this.getActivity().getIntent());
                }else{
                    Toast t = new Toast(this.getContext());
                    t.setText("Delete failed! Verify there are no attached courses and try again.");
                    t.show();
                }
            }catch(Exception ex) {
                Toast t = new Toast(this.getContext());
                t.setText(ex.getMessage());
                t.show();
            }
        });
        layout.addView(noteTitle);
        layout.addView(noteContent);
        layout.addView(deleteButton);
        builder.setTitle("Modify note");
        builder.setView(layout)
                // Add action button validates the start/end dates before and after conversion
                .setPositiveButton("Save", (dialog, id) -> {
                    Toast t = new Toast(this.getContext());


                    try{
                        CourseNote note = new CourseNote(this.courseNote.getId(), noteTitle.getText().toString(), noteContent.getText().toString(), this.courseNote.getCourseId());
                        try (DatabaseHelper dh = new DatabaseHelper(getContext())) {
                            SQLiteDatabase db = dh.getWritableDatabase();
                            boolean result = dh.updateNote(note);
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
                    ModifyCourseNoteDialogFragment.this.getDialog().cancel();
                });
        return builder.create();
    }
}
