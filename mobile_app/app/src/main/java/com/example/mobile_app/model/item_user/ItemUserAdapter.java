package com.example.mobile_app.model.item_user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.R;
import com.example.mobile_app.model.RecyclerViewInterface;

import java.util.List;

public class ItemUserAdapter extends RecyclerView.Adapter<ItemUserViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;

    Activity mActivity;
    Context mContext;
    List<ItemUser> users;

    public ItemUserAdapter(List<ItemUser> users, Activity activity, RecyclerViewInterface recyclerViewInterface) {
        this.users = users;
        this.mActivity = activity;
        this.mContext = activity.getApplicationContext();
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ItemUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemUserViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false),
                recyclerViewInterface
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ItemUserViewHolder holder, int position) {
        holder.text_fullname.setText(users.get(position).getName() + " " + users.get(position).getFirstname());
        holder.text_email.setText(users.get(position).getEmail());

        String roleText;
        switch (users.get(position).getRole()) {
            case 0: roleText = mContext.getString(R.string.status_student); break;
            case 1: roleText = mContext.getString(R.string.status_teacher); break;
            case 2: roleText = mContext.getString(R.string.status_other); break;
            default: roleText = ""; break;
        }
        holder.text_role.setText(roleText);

        holder.button_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setMessage(mActivity.getString(R.string.ui_deleteUserWarning) +
                        String.format(" %s ?", holder.text_fullname.getText()));
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(mContext, "Account Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                builder.create();
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() { return users.size(); }
}