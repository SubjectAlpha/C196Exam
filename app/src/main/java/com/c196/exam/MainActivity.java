package com.c196.exam;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.c196.exam.database.DatabaseHelper;
import com.c196.exam.entities.Assessment;
import com.c196.exam.entities.Course;
import com.c196.exam.entities.Term;
import com.c196.exam.ui.dialogs.CreateNotificationDialogFragment;
import com.c196.exam.ui.dialogs.CreateTermDialogFragment;
import com.c196.exam.ui.fragments.MainCardFragment;
import com.c196.exam.utility.NotificationService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.Instant;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHelper dbh = new DatabaseHelper(this);
        //dbh.onUpgrade(dbh.getReadableDatabase(), 0, 1);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Button notificationButton = findViewById(R.id.notificationButton);
        notificationButton.setOnClickListener((v) -> {
            CreateNotificationDialogFragment createNotificationDialogFragment = new CreateNotificationDialogFragment();
            createNotificationDialogFragment.show(getSupportFragmentManager(), CreateNotificationDialogFragment.TAG);
        });

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(this, WelcomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}