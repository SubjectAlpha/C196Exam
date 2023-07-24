package com.c196.exam.ui.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TermViewModel extends ViewModel {
    private final MutableLiveData<TermActivityState> state = new MutableLiveData<>(new TermActivityState());
    public LiveData<TermActivityState> getState() {
        return state;
    }
}
