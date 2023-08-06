package com.c196.exam.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.c196.exam.CourseActivity;
import com.c196.exam.R;
import com.c196.exam.entities.Course;

public class CourseFragment extends Fragment {

    private TextView instructorFName;
    private TextView instructorEmail;
    private TextView instructorPhone;
    private Course course;
    public CourseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_course, container, false);

        instructorFName = v.findViewById(R.id.instructorFName);
        instructorEmail = v.findViewById(R.id.instructorEmail);
        instructorPhone = v.findViewById(R.id.instructorPhone);

        CourseActivity act = (CourseActivity) getActivity();
        course = act.getCourse();
        instructorFName.setText(course.getInstructorFirstName() + " " + course.getInstructorLastName());
        instructorEmail.setText("Email: " + course.getInstructorEmail());
        instructorPhone.setText("Phone: " + course.getInstructorPhone());

        return v;
    }
}