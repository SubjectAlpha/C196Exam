package com.c196.exam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.c196.exam.database.DatabaseHelper;
import com.c196.exam.entities.Course;
import com.c196.exam.entities.Term;
import com.c196.exam.ui.dialogs.CreateNotificationDialogFragment;
import com.c196.exam.utility.ViewPagerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class CourseActivity extends AppCompatActivity {

    FragmentListener fragmentListener;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    ViewPagerAdapter viewPagerAdapter;
    FloatingActionButton fab;

    Course course;

    public Course getCourse() {
        return course;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.view_pager);

        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        DatabaseHelper dbh = new DatabaseHelper(this);
        Intent newIntent = getIntent();
        Integer id = newIntent.getIntExtra("courseId", -1);
        course = dbh.getCourse(id);

        String start = course.getStart().split("T")[0];
        String end = course.getEnd().split("T")[0];

        Toolbar myToolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setTitle(course.getTitle());
        myToolbar.setSubtitle(start + " - " + end);

        Button notificationButton = findViewById(R.id.notificationButton);
        notificationButton.setOnClickListener((v) -> {
            CreateNotificationDialogFragment createNotificationDialogFragment = new CreateNotificationDialogFragment();
            createNotificationDialogFragment.show(getSupportFragmentManager(), CreateNotificationDialogFragment.TAG);
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        fab = findViewById(R.id.dynamicFab);
        fab.setOnClickListener((view) -> {
            switch (viewPager.getCurrentItem()) {
                case 0:
                    fragmentListener.addNote();
                    break;
                case 1:
                    fragmentListener.addAssessment();
                    break;
            }
        });
    }

    public interface FragmentListener {
        void addNote();
        void addAssessment();
    }

    public void setFragmentListener(FragmentListener listener) {
        fragmentListener = listener;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(this, TermActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                DatabaseHelper dbh = new DatabaseHelper(this);
                Term t = dbh.getTerm(course.getTermId());

                i.putExtra("title", t.getTitle());
                i.putExtra("start", t.getStart());
                i.putExtra("end", t.getEnd());
                i.putExtra("id", course.getTermId());
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}