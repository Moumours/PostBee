package com.example.mobile_app.model.item_pfp;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.R;
import com.example.mobile_app.controller.ProfilePictureManager;
import com.example.mobile_app.model.RecyclerViewInterface;

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
            Toast.makeText(mContext, String.valueOf(position), Toast.LENGTH_SHORT).show();
            mDialog.dismiss();
        });
    }

    @Override
    public int getItemCount() { return ProfilePictureManager.PFP_AMOUNT; }
}