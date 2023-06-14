package com.example.mobile_app.controller;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.R;
import com.example.mobile_app.model.RecyclerViewInterface;
import com.example.mobile_app.model.item_pfp.ProfilePictureAdapter;
import com.example.mobile_app.model.item_post.ItemPostAdapter;

public class ProfilePictureManager {

    public static final int PFP_AMOUNT = 15;

    public static void setProfilePicture(Context context, ImageView imageView, int pfpId) {
        int profilePicture;
        switch (pfpId) {
            case 1: profilePicture = R.drawable.profile_1; break;
            case 2: profilePicture = R.drawable.profile_2; break;
            case 3: profilePicture = R.drawable.profile_3; break;
            case 4: profilePicture = R.drawable.profile_4; break;
            case 5: profilePicture = R.drawable.profile_5; break;
            case 6: profilePicture = R.drawable.profile_6; break;
            case 7: profilePicture = R.drawable.profile_7; break;
            case 8: profilePicture = R.drawable.profile_8; break;
            case 9: profilePicture = R.drawable.profile_9; break;
            case 10: profilePicture = R.drawable.profile_10; break;
            case 11: profilePicture = R.drawable.profile_11; break;
            case 12: profilePicture = R.drawable.profile_12; break;
            case 13: profilePicture = R.drawable.profile_13; break;
            case 14: profilePicture = R.drawable.profile_14; break;
            default: profilePicture = R.drawable.profile_0; break;
        }

        imageView.setImageDrawable(context.getDrawable(profilePicture));
    }
}
