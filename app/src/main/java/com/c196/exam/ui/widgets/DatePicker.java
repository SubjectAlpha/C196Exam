package com.c196.exam.ui.widgets;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.FragmentManager;

import com.c196.exam.ui.dialogs.SelectDateFragment;

public class DatePicker extends AppCompatEditText {
    public DatePicker(@NonNull Context context, FragmentManager manager, String hint) {
        super(context);
        setHint(hint);

        this.setOnFocusChangeListener((v, hasFocus) -> {
            openSelectDateFragment(manager);
        });

        this.setOnClickListener(v -> {
            openSelectDateFragment(manager);
        });
    }

    private void openSelectDateFragment(FragmentManager manager) {
        SelectDateFragment startDateFragment = new SelectDateFragment(this);
        startDateFragment.show(manager, "DatePicker");
    }
}
