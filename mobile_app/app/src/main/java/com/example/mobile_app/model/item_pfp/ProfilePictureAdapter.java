package com.example.mobile_app.model.item_pfp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.R;
import com.example.mobile_app.controller.ProfilePictureManager;
import com.example.mobile_app.model.RecyclerViewInterface;
import com.example.mobile_app.model.Token;
import com.example.mobile_app.model.UserStatic;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProfilePictureAdapter extends RecyclerView.Adapter<ProfilePictureViewHolder> {

    private final RecyclerViewInterface mRecyclerViewInterface;

    Dialog mDialog;
    Context mContext;
    List<ProfilePicture> pfps;

    public ProfilePictureAdapter(Context context, RecyclerViewInterface recyclerViewInterface, Dialog dialog) {
        this.pfps = new ArrayList<>(ProfilePictureManager.PFP_AMOUNT);
        this.mDialog = dialog;
        this.mContext = context;
        this.mRecyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ProfilePictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProfilePictureViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_pfp, parent, false),
                mRecyclerViewInterface
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ProfilePictureViewHolder holder, int position) {
        ProfilePictureManager.setProfilePicture(mContext, holder.mImageView, position);
        holder.mImageView.setOnClickListener(v -> {
            UserStatic.setProfile_picture(position);
            UploadChanges(position);
            ProfilePictureManager.setProfilePicture(
                    mContext,
                    ((Activity)mContext).findViewById(R.id.profile_imageview_pfp),
                    position);
        });
    }

    @Override
    public int getItemCount() { return ProfilePictureManager.PFP_AMOUNT; }

    public void UploadChanges(int newId) {
        try {
            Token.connectToServer(
                    "profile_picture/?id=" + newId,
                    "GET",
                    UserStatic.getAccess(),
                    null,
                    null,
                    null,
                    null);
        } finally {
            mDialog.dismiss();
        }
    }
}