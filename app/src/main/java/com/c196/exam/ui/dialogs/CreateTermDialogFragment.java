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

import com.c196.exam.MainActivity;
import com.c196.exam.database.DatabaseHelper;
import com.c196.exam.entities.Term;
import com.c196.exam.ui.widgets.DatePicker;

import java.time.Instant;

public class CreateTermDialogFragment extends DialogFragment {
    public static String TAG = "CreateTermDialog";

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
        termName.setHint("Term name");
        layout.addView(termName);
        layout.addView(startDate);
        layout.addView(endDate);
        builder.setTitle("Create a new term");
        builder.setView(layout)
                // Add action button validates the start/end dates before and after conversion
                .setPositiveButton("Create", (dialog, id) -> {
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
                        Term term = new Term(termName.getText().toString(), startDateTime, endDateTime);
                        try (DatabaseHelper dh = new DatabaseHelper(getContext())) {
                            SQLiteDatabase db = dh.getWritableDatabase();
                            Log.d("INFO", "DB Open: " + db.isOpen());
                            boolean result = dh.addTerm(term);
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
                    CreateTermDialogFragment.this.getDialog().cancel();
                });
        return builder.create();
    }
}
