package com.c196.exam.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.c196.exam.CourseActivity;
import com.c196.exam.R;
import com.c196.exam.entities.Course;
import com.c196.exam.entities.CourseNote;
import com.c196.exam.ui.dialogs.CreateAssessmentDialogFragment;
import com.c196.exam.ui.dialogs.CreateClassDialogFragment;
import com.c196.exam.ui.dialogs.CreateNoteDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class CourseFragment extends Fragment implements CourseActivity.FragmentListener {

    private TextView courseStatus;
    private TextView instructorFName;
    private TextView instructorEmail;
    private TextView instructorPhone;
    private LinearLayout noteLayout;
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

        courseStatus = v.findViewById(R.id.courseStatus);
        instructorFName = v.findViewById(R.id.instructorFName);
        instructorEmail = v.findViewById(R.id.instructorEmail);
        instructorPhone = v.findViewById(R.id.instructorPhone);
        noteLayout = v.findViewById(R.id.noteLayout);

        CourseActivity act = (CourseActivity) getActivity();
        act.setFragmentListener(this);
        course = act.getCourse();
        courseStatus.setText("Course Status: " + course.getStatus());
        instructorFName.setText(course.getInstructorFirstName() + " " + course.getInstructorLastName());
        instructorEmail.setText("Email: " + course.getInstructorEmail());
        instructorPhone.setText("Phone: " + course.getInstructorPhone());

        if(savedInstanceState == null){
            for(CourseNote note : course.getNotes()) {
                getChildFragmentManager().beginTransaction().add(R.id.noteLayout, NoteCardFragment.newInstance(note)).commit();
            }
        }

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("reRender", false);
    }

    @Override
    public void addNote() {
        CreateNoteDialogFragment noteDialogFragment = new CreateNoteDialogFragment();
        Bundle b = new Bundle();
        b.putInt("courseId", course.getId());
        noteDialogFragment.setArguments(b);
        noteDialogFragment.show(getChildFragmentManager(), CreateNoteDialogFragment.TAG);
    }

    @Override
    public void addAssessment() {
        CreateAssessmentDialogFragment createAssessmentDialogFragment = new CreateAssessmentDialogFragment();
        Bundle b = new Bundle();
        b.putInt("courseId", course.getId());
        createAssessmentDialogFragment.setArguments(b);
        createAssessmentDialogFragment.show(getChildFragmentManager(), CreateAssessmentDialogFragment.TAG);
    }
}