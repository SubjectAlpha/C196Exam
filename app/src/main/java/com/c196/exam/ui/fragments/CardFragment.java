package com.c196.exam.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.c196.exam.MainActivity;
import com.c196.exam.R;
import com.c196.exam.TermActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardFragment extends Fragment {

    private TextView titleText;
    private TextView startText;
    private TextView endText;

    private static final String TERM_ID = "TERM_ID";
    private static final String TERM_TITLE = "TERM_TITLE";
    private static final String TERM_START = "TERM_START";
    private static final String TERM_END= "TERM_END";

    private String termTitle;
    private String termStart;
    private String termEnd;
    private Integer termId;

    public CardFragment() {
        // Required empty public constructor
    }

    public static CardFragment newInstance(int id, String title, String start, String end) {
        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();
        args.putString(TERM_TITLE, title);
        args.putString(TERM_START, start);
        args.putString(TERM_END, end);
        args.putInt(TERM_ID, id);
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
        startText = v.findViewById(R.id.term_start);
        endText = v.findViewById(R.id.term_end);
        titleText = v.findViewById(R.id.term_name);

        titleText.setText(termTitle);
        startText.setText(termStart);
        endText.setText(termEnd);

        v.setOnClickListener((view) -> {
            MainActivity ma = (MainActivity) getActivity();
            ma.OpenTermActivity(termTitle, termStart, termEnd, termId);
        });

        return v;
    }
}