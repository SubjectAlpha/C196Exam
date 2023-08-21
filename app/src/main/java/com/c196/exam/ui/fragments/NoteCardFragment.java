package com.c196.exam.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.c196.exam.CourseActivity;
import com.c196.exam.R;
import com.c196.exam.entities.Course;
import com.c196.exam.entities.CourseNote;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteCardFragment extends Fragment {

    private TextView titleText;
    private TextView contentText;

    private static final String NOTE_TITLE = "NOTE_TITLE";
    private static final String NOTE_CONTENT = "NOTE_CONTENT";

    private String noteTitle;
    private String noteContent;

    public NoteCardFragment() {
        // Required empty public constructor
    }

    public static NoteCardFragment newInstance(CourseNote note) {
        NoteCardFragment fragment = new NoteCardFragment();
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
        contentText = v.findViewById(R.id.content);

        titleText.setText(noteTitle);
        contentText.setText(noteContent);

        v.setOnLongClickListener((l) -> {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_SEND);
            i.putExtra(Intent.EXTRA_SUBJECT, noteTitle);
            i.putExtra(Intent.EXTRA_TEXT, noteContent);
            i.setType("text/plain");

            if(i.resolveActivity(getActivity().getPackageManager()) != null){
                startActivity(i);
            }

            return true;
        });

        return v;
    }
}