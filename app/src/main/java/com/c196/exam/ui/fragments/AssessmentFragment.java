package com.c196.exam.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.c196.exam.CourseActivity;
import com.c196.exam.R;
import com.c196.exam.entities.Assessment;
import com.c196.exam.entities.Course;
import com.c196.exam.ui.dialogs.CreateAssessmentDialogFragment;
import com.c196.exam.ui.dialogs.CreateNoteDialogFragment;

import java.util.ArrayList;

public class AssessmentFragment extends Fragment implements CourseActivity.FragmentListener {

    Course course;
    public AssessmentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CourseActivity act = (CourseActivity) getActivity();
        act.setFragmentListener(this);
        course = act.getCourse();

        for(Assessment assessment : course.getAssessments()) {
            getChildFragmentManager().beginTransaction().add(R.id.assessmentLayout, AssessmentCardFragment.newInstance(assessment)).commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_assessment, container, false);
    }

    @Override
    public void addNote() {}

    @Override
    public void addAssessment() {
        CreateAssessmentDialogFragment createAssessmentDialogFragment = new CreateAssessmentDialogFragment();
        Bundle b = new Bundle();
        b.putInt("courseId", course.getId());
        createAssessmentDialogFragment.setArguments(b);
        createAssessmentDialogFragment.show(getChildFragmentManager(), CreateNoteDialogFragment.TAG);
    }
}