package com.c196.exam.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.c196.exam.MainActivity;
import com.c196.exam.R;
import com.c196.exam.TermActivity;
import com.c196.exam.entities.Term;
import com.c196.exam.ui.dialogs.CreateTermDialogFragment;
import com.c196.exam.ui.dialogs.ModifyTermDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainCardFragment extends Fragment {

    private TextView titleText;
    private TextView startText;
    private TextView endText;

    private static final String TERM_ID = "TERM_ID";
    private static final String TERM_TITLE = "TERM_TITLE";
    private static final String TERM_START = "TERM_START";
    private static final String TERM_END= "TERM_END";

    public String getTermTitle() {
        return termTitle;
    }

    public String getTermStart() {
        return termStart;
    }

    public String getTermEnd() {
        return termEnd;
    }

    public Integer getTermId() {
        return termId;
    }

    private String termTitle;
    private String termStart;
    private String termEnd;
    private Integer termId;

    public MainCardFragment() {
        // Required empty public constructor
    }

    public static MainCardFragment newInstance(Term t) {
        MainCardFragment fragment = new MainCardFragment();
        Bundle args = new Bundle();
        args.putString(TERM_TITLE, t.getTitle());
        args.putString(TERM_START, t.getStart().split("T")[0]);
        args.putString(TERM_END, t.getEnd().split("T")[0]);
        args.putInt(TERM_ID, t.getId());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            termTitle = getArguments().getString(TERM_TITLE);
            termStart = getArguments().getString(TERM_START);
            termEnd = getArguments().getString(TERM_END);
            termId = getArguments().getInt(TERM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_card, container, false);
        startText = v.findViewById(R.id.start);
        endText = v.findViewById(R.id.end);
        titleText = v.findViewById(R.id.name);

        titleText.setText(termTitle);
        startText.setText(termStart);
        endText.setText(termEnd);

        v.setOnClickListener((view) -> {
            Intent i = new Intent(this.requireContext(), TermActivity.class);
            i.putExtra("title", termTitle);
            i.putExtra("start", termStart);
            i.putExtra("end", termEnd);
            i.putExtra("id", termId);
            getActivity().startActivity(i);
        });

        v.setOnLongClickListener((view) -> {
            ModifyTermDialogFragment modifyTermDialogFragment = new ModifyTermDialogFragment(termId, termTitle, termStart, termEnd);
            modifyTermDialogFragment.show(this.getChildFragmentManager(), ModifyTermDialogFragment.TAG);
            return true;
        });

        return v;
    }
}