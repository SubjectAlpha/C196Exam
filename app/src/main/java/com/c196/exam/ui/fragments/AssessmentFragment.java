package com.c196.exam.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.c196.exam.CourseActivity;
import com.c196.exam.R;

public class AssessmentFragment extends Fragment implements CourseActivity.FragmentListener {

    private String mParam1;
    private String mParam2;

    public AssessmentFragment() {
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
        return inflater.inflate(R.layout.fragment_assessment, container, false);
    }

    @Override
    public void addNote() {}

    @Override
    public void addAssessment() {

    }
}