package com.c196.exam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.c196.exam.database.DatabaseHelper;
import com.c196.exam.entities.Term;
import com.c196.exam.ui.dialogs.CreateTermDialogFragment;
import com.c196.exam.ui.fragments.MainCardFragment;
import com.c196.exam.utility.NotificationService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotificationChannel channel = new NotificationChannel(NotificationService.CHANNEL_ID, NotificationService.CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(NotificationService.CHANNEL_DESC);
        NotificationManager nManager = getSystemService(NotificationManager.class);
        nManager.createNotificationChannel(channel);

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

        if(savedInstanceState == null){
            for ( Term t : dbh.getTerms() ) {
                getSupportFragmentManager().beginTransaction().add(ll.getId(), MainCardFragment.newInstance(t)).commit();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("reRender", false);
    }
}