package com.example.mobile_app.model;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mobile_app.model.fragments.ModerationPostsValidationFragment;
import com.example.mobile_app.model.fragments.ModerationUsersFragment;

public class ModerationViewPagerAdapter extends FragmentStateAdapter {

    public ModerationViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 1: return new ModerationUsersFragment();
            default: return new ModerationPostsValidationFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}