package com.c196.exam.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.c196.exam.R;
import com.c196.exam.entities.CourseNote;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssessmentCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssessmentCardFragment extends Fragment {

    private TextView titleText;

    private static final String NOTE_TITLE = "COURSE_TITLE";
    private static final String NOTE_CONTENT = "COURSE_CONTENT";

    private String noteTitle;
    private String noteContent;

    public AssessmentCardFragment() {
        // Required empty public constructor
    }

    public static AssessmentCardFragment newInstance(CourseNote note) {
        AssessmentCardFragment fragment = new AssessmentCardFragment();
        Bundle args = new Bundle();
        args.putString(NOTE_TITLE, note.getTitle());
        args.putString(NOTE_CONTENT, note.getContent());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteTitle = getArguments().getString(NOTE_TITLE);
            noteContent = getArguments().getString(NOTE_CONTENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card, container, false);
        titleText = v.findViewById(R.id.name);

        titleText.setText(noteTitle);

        /*v.setOnClickListener((view) -> {
            Intent i = new Intent(getContext(), CourseActivity.class);
            i.putExtra("courseId", courseId);
            getActivity().startActivity(i);
        });*/

        return v;
    }
}