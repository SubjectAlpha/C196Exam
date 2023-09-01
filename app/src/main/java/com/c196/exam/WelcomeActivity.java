package com.c196.exam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button termsButton = findViewById(R.id.termsButton);
        Button scheduleButton = findViewById(R.id.scheduleButton);

        termsButton.setOnClickListener((v) -> {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        });

        scheduleButton.setOnClickListener((v) -> {
            Intent i = new Intent(this, ScheduleActivity.class);
            startActivity(i);
        });
    }
}