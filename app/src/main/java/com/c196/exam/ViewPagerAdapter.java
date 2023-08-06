package com.c196.exam;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.c196.exam.ui.fragments.AssessmentFragment;
import com.c196.exam.ui.fragments.CourseFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 1){
            return new AssessmentFragment();
        } else {
            return new CourseFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
