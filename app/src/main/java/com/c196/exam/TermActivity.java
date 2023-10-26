package com.c196.exam;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
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
import com.c196.exam.entities.Course;
import com.c196.exam.ui.dialogs.CreateClassDialogFragment;
import com.c196.exam.ui.dialogs.CreateNotificationDialogFragment;
import com.c196.exam.ui.fragments.TermCardFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TermActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.SCHEDULE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_EXACT_ALARM) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(new String[] {Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.SCHEDULE_EXACT_ALARM, Manifest.permission.USE_EXACT_ALARM}, 100);
        }

        DatabaseHelper dbh = new DatabaseHelper(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent newIntent = getIntent();
        String title = newIntent.getStringExtra("title");
        String start = newIntent.getStringExtra("start");
        String end = newIntent.getStringExtra("end");
        Integer id = newIntent.getIntExtra("id", -1);

        start = start.split("T")[0];
        end = end.split("T")[0];

        myToolbar.setTitle(title);
        myToolbar.setSubtitle(start + " - " + end);

        Button notificationButton = findViewById(R.id.notificationButton);
        notificationButton.setOnClickListener((v) -> {
            CreateNotificationDialogFragment createNotificationDialogFragment = new CreateNotificationDialogFragment();
            createNotificationDialogFragment.show(getSupportFragmentManager(), CreateNotificationDialogFragment.TAG);
        });

        FloatingActionButton fab = findViewById(R.id.addCourse);
        fab.setOnClickListener((v) -> {
            CreateClassDialogFragment termDialogFragment = new CreateClassDialogFragment();
            Bundle b = new Bundle();
            b.putInt("termId", id);
            termDialogFragment.setArguments(b);
            termDialogFragment.show(getSupportFragmentManager(), CreateClassDialogFragment.TAG);
        });

        LinearLayout ll = findViewById(R.id.main_layout);

        if(savedInstanceState == null){
            for ( Course c : dbh.getCourses(id) ) {
                getSupportFragmentManager().beginTransaction().add(ll.getId(), TermCardFragment.newInstance(c)).commit();
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
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}