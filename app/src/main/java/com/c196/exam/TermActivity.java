package com.c196.exam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.c196.exam.database.DatabaseHelper;
import com.c196.exam.entities.Term;
import com.c196.exam.ui.dialogs.CreateClassDialogFragment;
import com.c196.exam.ui.dialogs.CreateTermDialogFragment;
import com.c196.exam.ui.fragments.CardFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TermActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);
        DatabaseHelper dbh = new DatabaseHelper(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        Intent newIntent = getIntent();
        String title = newIntent.getStringExtra("title");
        String start = newIntent.getStringExtra("start");
        String end = newIntent.getStringExtra("end");
        Integer id = newIntent.getIntExtra("id", -1);

        myToolbar.setTitle(title);
        myToolbar.setSubtitle(start + " - " + end);

        FloatingActionButton fab = findViewById(R.id.addCourse);
        fab.setOnClickListener((v) -> {
            CreateClassDialogFragment termDialogFragment = new CreateClassDialogFragment();
            Bundle b = new Bundle();
            b.putInt("termId", id);
            termDialogFragment.setArguments(b);
            //COmmunicate between to  reload shit
            termDialogFragment.show(getSupportFragmentManager(), CreateClassDialogFragment.TAG);
        });

        LinearLayout ll = findViewById(R.id.main_layout);

        for ( Term t : dbh.getCourses() ) {
            getSupportFragmentManager().beginTransaction().add(ll.getId(), CardFragment.newInstance(t.getId(), t.getTitle(), t.getStart(), t.getEnd())).commit();
        }
    }
}