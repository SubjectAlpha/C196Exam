package com.c196.exam.ui.widgets;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.FragmentManager;

import com.c196.exam.ui.dialogs.SelectDateFragment;
import com.c196.exam.ui.dialogs.SelectTimeFragment;

public class TimePicker extends AppCompatEditText {
    public TimePicker(@NonNull Context context, FragmentManager manager, String hint) {
        super(context);
        setHint(hint);

        this.setOnFocusChangeListener((v,e) -> {
            if(e){
                openSelectTimeFragment(manager);
            }
        });
    }

    private void openSelectTimeFragment(FragmentManager manager) {
        SelectTimeFragment selectTimeFragment = new SelectTimeFragment(this);
        selectTimeFragment.show(manager, "DatePicker");
    }
}
