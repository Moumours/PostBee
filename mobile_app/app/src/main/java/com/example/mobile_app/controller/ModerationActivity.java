package com.example.mobile_app.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.mobile_app.R;
import com.example.mobile_app.model.ModerationViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ModerationActivity extends AppCompatActivity {

    TabLayout mTabLayout;
    ViewPager2 mViewPager2;
    ModerationViewPagerAdapter mModerationViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moderation);

        mTabLayout = findViewById(R.id.moderation_tablayout);
        mViewPager2 = findViewById(R.id.moderation_viewpager);
        mModerationViewAdapter = new ModerationViewPagerAdapter(this);
        mViewPager2.setAdapter(mModerationViewAdapter);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mTabLayout.getTabAt(position).select();
            }
        });
    }
}