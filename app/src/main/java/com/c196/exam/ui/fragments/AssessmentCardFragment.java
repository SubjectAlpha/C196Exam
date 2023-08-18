package com.c196.exam.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.c196.exam.R;
import com.c196.exam.database.DatabaseHelper;
import com.c196.exam.entities.Assessment;
import com.c196.exam.entities.CourseNote;
import com.c196.exam.ui.dialogs.ModifyAssessmentDialogFragment;
import com.c196.exam.ui.dialogs.ModifyTermDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssessmentCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssessmentCardFragment extends Fragment {

    private TextView titleText;
    private TextView startText;
    private TextView endText;

    private static final String ASSESSMENT_TITLE = "ASSESSMENT_TITLE";
    private static final String ASSESSMENT_START = "ASSESSMENT_START";
    private static final String ASSESSMENT_END = "ASSESSMENT_END";
    private static final String ASSESSMENT_ID = "ASSESSMENT_ID";

    private String assessmentTitle;
    private String assessmentStart;
    private String assessmentEnd;
    private int assessmentId;

    public AssessmentCardFragment() {
        // Required empty public constructor
    }

    public static AssessmentCardFragment newInstance(Assessment assessment) {
        AssessmentCardFragment fragment = new AssessmentCardFragment();
        Bundle args = new Bundle();
        args.putString(ASSESSMENT_TITLE, assessment.getTitle());
        args.putString(ASSESSMENT_START, assessment.getStart());
        args.putString(ASSESSMENT_END, assessment.getEnd());
        args.putInt(ASSESSMENT_ID, assessment.getId());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            assessmentTitle = getArguments().getString(ASSESSMENT_TITLE);
            assessmentStart = getArguments().getString(ASSESSMENT_START);
            assessmentEnd = getArguments().getString(ASSESSMENT_END);
            assessmentId = getArguments().getInt(ASSESSMENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card, container, false);
        titleText = v.findViewById(R.id.name);
        startText = v.findViewById(R.id.start);
        endText = v.findViewById(R.id.end);

        titleText.setText(assessmentTitle);
        startText.setText(assessmentStart);
        endText.setText(assessmentEnd);

        v.setOnClickListener((view) -> {
            Assessment assessment;
            try(DatabaseHelper dbh = new DatabaseHelper(this.getContext())){
                assessment = dbh.getAssessment(assessmentId);
            }
            ModifyAssessmentDialogFragment modifyAssessmentDialogFragment = new ModifyAssessmentDialogFragment(assessment);
            modifyAssessmentDialogFragment.show(this.getChildFragmentManager(), ModifyAssessmentDialogFragment.TAG);
        });

        return v;
    }
}