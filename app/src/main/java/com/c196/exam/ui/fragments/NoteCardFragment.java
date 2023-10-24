package com.c196.exam.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.c196.exam.CourseActivity;
import com.c196.exam.R;
import com.c196.exam.database.DatabaseHelper;
import com.c196.exam.entities.Course;
import com.c196.exam.entities.CourseNote;
import com.c196.exam.ui.dialogs.ModifyCourseDialogFragment;
import com.c196.exam.ui.dialogs.ModifyCourseNoteDialogFragment;
import com.c196.exam.ui.widgets.Card;

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
    private static final String NOTE_ID = "NOTE_ID";

    private String noteTitle;
    private String noteContent;
    private Integer noteId;

    public NoteCardFragment() {
        // Required empty public constructor
    }

    public static NoteCardFragment newInstance(CourseNote note) {
        NoteCardFragment fragment = new NoteCardFragment();
        Bundle args = new Bundle();
        args.putString(NOTE_TITLE, note.getTitle());
        args.putString(NOTE_CONTENT, note.getContent());
        args.putInt(NOTE_ID, note.getId());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            noteTitle = getArguments().getString(NOTE_TITLE);
            noteContent = getArguments().getString(NOTE_CONTENT);
            noteId = getArguments().getInt(NOTE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card, container, false);

        titleText = v.findViewById(R.id.name);
        contentText = v.findViewById(R.id.content);

        Button shareBtn = new Button(this.requireContext());
        shareBtn.setText("✉");
        shareBtn.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Card c = v.findViewById(R.id.card_fragment);
        c.addView(shareBtn);

        titleText.setText(noteTitle);
        contentText.setText(noteContent);

        shareBtn.setOnClickListener((l) -> {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_SEND);
            i.putExtra(Intent.EXTRA_SUBJECT, noteTitle);
            i.putExtra(Intent.EXTRA_TEXT, noteContent);
            i.setType("text/plain");

            if(i.resolveActivity(getActivity().getPackageManager()) != null){
                startActivity(i);
            }

            //return true;
        });

        c.setOnLongClickListener((l) -> {
            CourseNote n;
            try(DatabaseHelper dbh = new DatabaseHelper(this.requireContext())) {
                n = dbh.getNote(noteId);
            }catch (Exception e) {
                n = null;
            }
            ModifyCourseNoteDialogFragment modifyCourseNoteDialogFragment = new ModifyCourseNoteDialogFragment(n);
            modifyCourseNoteDialogFragment.show(this.getChildFragmentManager(), ModifyCourseNoteDialogFragment.TAG);
            return true;
        });

        return v;
    }
}