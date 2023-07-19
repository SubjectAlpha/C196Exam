package com.c196.exam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.c196.exam.database.DatabaseHelper;
import com.c196.exam.entities.Term;
import com.c196.exam.ui.dialogs.CreateTermDialogFragment;
import com.c196.exam.ui.fragments.CardFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper dbh = new DatabaseHelper(this);
        //dbh.onUpgrade(dbh.getReadableDatabase(), 0, 1);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        FloatingActionButton fab = findViewById(R.id.addTerm);
        fab.setOnClickListener((v) -> {
            CreateTermDialogFragment termDialogFragment = new CreateTermDialogFragment();
            //COmmunicate between to  reload shit
            termDialogFragment.show(getSupportFragmentManager(), CreateTermDialogFragment.TAG);
        });

        LinearLayout ll = findViewById(R.id.main_layout);

        for ( Term t : dbh.getTerms() ) {
            getSupportFragmentManager().beginTransaction().add(ll.getId(), CardFragment.newInstance(t.getId(), t.getTitle(), t.getStart(), t.getEnd())).commit();
        }

        View v = this.findViewById(R.id.main_layout);
        v.setOnClickListener((view) -> {
            Log.d("MSG", "CLICKED");
            CardFragment f = (CardFragment) getSupportFragmentManager().findFragmentById(R.id.card_fragment);
            String title = f.getTermTitle();
            String start = f.getTermStart();
            String end = f.getTermEnd();
            Integer id = f.getTermId();

            OpenTermActivity(title, start, end, id);
        });
    }

    public void OpenTermActivity(String title, String start, String end, int id) {
        Intent i = new Intent(MainActivity.this, TermActivity.class);
        i.putExtra("title", title);
        i.putExtra("start", start);
        i.putExtra("end", end);
        i.putExtra("id", id);
        MainActivity.this.startActivity(i);
    }
}