package com.c196.exam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.c196.exam.database.DatabaseHelper;
import com.c196.exam.entities.Course;
import com.c196.exam.entities.Term;
import com.c196.exam.ui.widgets.CalendarAdapter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener{

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYear);
        selectedDate = LocalDate.now();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setMonthView();
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

    private void setMonthView()
    {
        // First we want to determine which term we're in
        // Load that term, and its classes
        // Display classes under terms
        // Display assessments on their scheduled day

        ArrayList<Course> courses = null;
        DatabaseHelper dbh = new DatabaseHelper(this);
        Term term = dbh.getTerm(selectedDate);

        if(term != null)
        {
            courses = term.getCourses();
        }

        monthYearText.setText(getMonthYear(selectedDate));
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(selectedDate);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                if(courses != null) {
                    Course course = null;

                    for(int ii = 0; ii < courses.size(); ii++)
                    {
                        Course c = courses.get(ii);
                        LocalDate courseStart = LocalDate.parse(c.getStart());
                        LocalDate courseEnd = LocalDate.parse(c.getEnd());
                        LocalDate termStart = LocalDate.parse(term.getStart());
                        LocalDate termEnd = LocalDate.parse(term.getEnd());

                        boolean dateIsDuringTerm = selectedDate.isAfter(termStart) && selectedDate.isBefore(termEnd);
                        boolean dateIsDuringCourse = selectedDate.isAfter(courseStart) && selectedDate.isBefore(courseEnd);
                        if(dateIsDuringTerm && dateIsDuringCourse) {
                            course = c;
                        }
                    };
                }

                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonthArray, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private String getMonthYear(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public void previousMonthAction(View view)
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if(!dayText.equals(""))
        {
            String message = "Selected Date " + dayText + " " + getMonthYear(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    public class CalendarDay {
        public String termTitle;
        public String courseTitle;
        public String assignmentTitle;
    }
}