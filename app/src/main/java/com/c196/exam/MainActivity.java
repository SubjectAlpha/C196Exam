package com.c196.exam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.c196.exam.database.DatabaseHelper;
import com.c196.exam.entities.Assessment;
import com.c196.exam.entities.Course;
import com.c196.exam.entities.Term;
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
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            NotificationService ns = new NotificationService();
            for ( Term t : dbh.getTerms() ) {

                //Notification Logic
                //If a term is coming up within a week, notify, same for class, and assessment
                //If a term is ending within a week, notify

                int secondsInAWeek = 604800;
                Instant now = Instant.now();
                Instant weekFromNow = now.plusSeconds(secondsInAWeek);
                Instant termStart = Instant.parse(t.getStart());
                Instant termEnd = Instant.parse(t.getEnd());

                if(termStart.isBefore(now) && termStart.isAfter(weekFromNow)){
                    String content = "Your term " + t.getTitle() + " begins next week!";
                    ns.scheduleNotification(this, am,0, "Term Starting Soon", content);
                }

                if(termEnd.isBefore(now) && termEnd.isAfter(weekFromNow)){
                    String content = "Your term " + t.getTitle() + " ends next week!";
                    ns.scheduleNotification(this, am,0, "Term Ending Soon", content);
                }

                for(Course c : t.getCourses()){
                    Instant courseStart = Instant.parse(c.getStart());
                    Instant courseEnd = Instant.parse(c.getEnd());

                    if(courseStart.isBefore(now) && courseStart.isAfter(weekFromNow)){
                        String content = "Your course " + c.getTitle() + " begins next week!";
                        ns.scheduleNotification(this, am,0, "Course Starting Soon", content);
                    }

                    if(courseEnd.isBefore(now) && courseEnd.isAfter(weekFromNow)){
                        String content = "Your course " + c.getTitle() + " ends next week!";
                        ns.scheduleNotification(this, am,0, "Course Ending Soon", content);
                    }

                    for(Assessment a : c.getAssessments()){
                        Instant assessmentStart = Instant.parse(a.getStart() + "T00:00:00Z");
                        Instant assessmentEnd = Instant.parse(a.getEnd() + "T00:00:00Z");

                        if(assessmentStart.isBefore(now) && assessmentStart.isAfter(weekFromNow)){
                            String content = "Your assessment " + a.getTitle() + " begins next week!";
                            ns.scheduleNotification(this, am,0, "Assessment Opens Soon", content);
                        }

                        if(assessmentEnd.isBefore(now) && assessmentEnd.isAfter(weekFromNow)){
                            String content = "Your assessment " + a.getTitle() + " ends next week!";
                            ns.scheduleNotification(this, am,0, "Assessment Due Soon", content);
                        }
                    }
                }

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