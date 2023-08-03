package com.c196.exam.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.c196.exam.CourseActivity;
import com.c196.exam.MainActivity;
import com.c196.exam.R;
import com.c196.exam.entities.Course;
import com.c196.exam.entities.Term;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TermCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TermCardFragment extends Fragment {

    private TextView titleText;
    private TextView startText;
    private TextView endText;

    private static final String COURSE_ID = "COURSE_ID";
    private static final String COURSE_TITLE = "COURSE_TITLE";
    private static final String COURSE_START = "COURSE_START";
    private static final String COURSE_END= "COURSE_END";

    private String courseTitle;
    private String courseStart;
    private String courseEnd;
    private Integer courseId;

    public TermCardFragment() {
        // Required empty public constructor
    }

    public static TermCardFragment newInstance(Course c) {
        TermCardFragment fragment = new TermCardFragment();
        Bundle args = new Bundle();
        args.putString(COURSE_TITLE, c.getTitle());
        args.putString(COURSE_START, c.getStart().split("T")[0]);
        args.putString(COURSE_END, c.getEnd().split("T")[0]);
        args.putInt(COURSE_ID, c.getId());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseTitle = getArguments().getString(COURSE_TITLE);
            courseStart = getArguments().getString(COURSE_START);
            courseEnd = getArguments().getString(COURSE_END);
            courseId = getArguments().getInt(COURSE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card, container, false);
        startText = v.findViewById(R.id.start);
        endText = v.findViewById(R.id.end);
        titleText = v.findViewById(R.id.name);

        titleText.setText(courseTitle);
        startText.setText(courseStart);
        endText.setText(courseEnd);

        v.setOnClickListener((view) -> {
            Intent i = new Intent(getContext(), CourseActivity.class);
            i.putExtra("courseId", courseId);
            getActivity().startActivity(i);
        });

        return v;
    }
}