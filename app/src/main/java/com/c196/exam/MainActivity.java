package com.c196.exam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.c196.exam.database.DatabaseHelper;
import com.c196.exam.entities.Term;
import com.c196.exam.ui.dialogs.CreateTermDialogFragment;
import com.c196.exam.ui.fragments.MainCardFragment;
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
            termDialogFragment.show(getSupportFragmentManager(), CreateTermDialogFragment.TAG);
        });

        LinearLayout ll = findViewById(R.id.main_layout);

        for ( Term t : dbh.getTerms() ) {
            getSupportFragmentManager().beginTransaction().add(ll.getId(), MainCardFragment.newInstance(t)).commit();
        }
    }
}