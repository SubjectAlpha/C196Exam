package com.c196.exam.ui.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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

import com.c196.exam.MainActivity;
import com.c196.exam.database.DatabaseHelper;
import com.c196.exam.entities.Term;
import com.c196.exam.ui.widgets.DatePicker;

import java.time.Instant;

public class ModifyTermDialogFragment extends DialogFragment {
    public static String TAG = "ModifyTermDialog";

    private final int termId;
    private final String termName;
    private final String termStart;
    private final String termEnd;

    public ModifyTermDialogFragment(int termId, String termName, String start, String end){
        this.termId = termId;
        this.termName = termName;
        this.termStart = start;
        this.termEnd = end;
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
        final EditText termName = new EditText(this.getContext());
        final EditText startDate = new DatePicker(this.requireContext(), this.getChildFragmentManager(), "Start Date");
        final EditText endDate = new DatePicker(this.requireContext(), this.getChildFragmentManager(), "End Date");
        final Button deleteButton = new Button(this.getContext());
        termName.setHint("Term name");
        termName.setText(this.termName);
        startDate.setText(this.termStart);
        endDate.setText(this.termEnd);
        deleteButton.setText("Delete Term");
        deleteButton.setBackgroundColor(Color.RED);
        deleteButton.setOnClickListener((v) -> {
            try(DatabaseHelper dbh = new DatabaseHelper(this.getContext())){
                boolean result = dbh.deleteTerm(this.termId);
                dbh.close();
                if(result){
                    //Reload activity
                    Activity activity = getActivity();
                    activity.finish();
                    activity.startActivity(this.getActivity().getIntent());
                }
                Toast t = new Toast(this.getContext());
                t.setText("Delete failed! Verify there are no attached courses and try again.");
                t.show();

            }catch(Exception ex) {
                Toast t = new Toast(this.getContext());
                t.setText(ex.getMessage());
                t.show();
            }
        });
        layout.addView(termName);
        layout.addView(startDate);
        layout.addView(endDate);
        layout.addView(deleteButton);
        builder.setTitle("Modify term {term}");
        builder.setView(layout)
                // Add action button validates the start/end dates before and after conversion
                .setPositiveButton("Save", (dialog, id) -> {
                    String startDateTime = "";
                    String endDateTime = "";
                    Toast t = new Toast(this.getContext());
                    try{
                        String start = startDate.getText().toString() + "T00:00:00Z";
                        String end = endDate.getText().toString() + "T00:00:00Z";
                        Instant startInstant = Instant.parse(start);
                        Instant endInstant = Instant.parse(end);

                        if(startInstant.isAfter(endInstant)){
                            t.setText("Please ensure your start and end dates are in yyyy-MM-dd format.");
                            t.show();
                        }

                        startDateTime = startInstant.toString();
                        endDateTime = endInstant.toString();
                    } catch (Exception ex){

                        t.setText("Please ensure your start and end dates are in yyyy-MM-dd format.");
                        t.show();
                    }

                    try{
                        Term term = new Term(this.termId, termName.getText().toString(), startDateTime, endDateTime);
                        try (DatabaseHelper dh = new DatabaseHelper(getContext())) {
                            SQLiteDatabase db = dh.getWritableDatabase();
                            boolean result = dh.updateTerm(term);
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
                    ModifyTermDialogFragment.this.getDialog().cancel();
                });
        return builder.create();
    }
}
