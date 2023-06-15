package com.example.mobile_app.model.item_user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.R;
import com.example.mobile_app.model.RecyclerViewInterface;
import com.example.mobile_app.model.Token;
import com.example.mobile_app.model.User;
import com.example.mobile_app.model.UserStatic;
import com.example.mobile_app.model.item_post.PostDecision;
import com.google.gson.Gson;

import java.util.List;

public class ItemUserAdapter extends RecyclerView.Adapter<ItemUserViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;

    Activity mActivity;
    Context mContext;
    List<ItemUser> users;

    private String mTokenAccess = UserStatic.access;

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
        ItemUser currentUser = users.get(position);

        if (currentUser.getIs_staff().equals("true")) {
            holder.button_addModo.setVisibility(View.GONE);
        } else {
            holder.button_addModo.setVisibility(View.VISIBLE);
        }


        String roleText;
        switch (users.get(position).getRole()) {
            case 0: roleText = mContext.getString(R.string.status_student);
                Log.d("ItemUserAdapter", "Voici le role : " + roleText);
                break;
            case 1: roleText = mContext.getString(R.string.status_teacher);
                Log.d("ItemUserAdapter", "Voici le role : " + roleText);
                break;
            case 2: roleText = mContext.getString(R.string.status_other);
                Log.d("ItemUserAdapter", "Voici le role : " + roleText);
                break;
            default: roleText = ""; break;
        }
        holder.text_role.setText(roleText);

        String staffText;
        if (users.get(position).getIs_staff().equals("true")) {
            staffText = "Modérateur";
        } else {
            staffText = "Utilisateur";
        }
        holder.text_is_staff.setText(staffText);

        holder.button_remove.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setMessage(mActivity.getString(R.string.ui_deleteUserWarning) +
                    String.format(" %s ?", holder.text_fullname.getText()));
            builder.setPositiveButton(R.string.ui_yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    int position = holder.getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        String userEmail = users.get(position).getEmail();
                        DelatedUser user = new DelatedUser(userEmail);
                        Log.d("ItemUserAdapter", "Voici l'email : " + userEmail);
                        deleteUser(user);
                        Toast.makeText(mContext, "Account Deleted", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton(R.string.ui_no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            builder.create();
            builder.show();
        });

        holder.button_addModo.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setMessage(mActivity.getString(R.string.ui_addModoConfirmation) +
                    String.format(" %s ?", holder.text_fullname.getText()));
            builder.setPositiveButton(R.string.ui_yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    int position = holder.getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {
                        String userEmail = users.get(position).getEmail();
                        DelatedUser user = new DelatedUser(userEmail);
                        Log.d("ItemUserAdapter", "Voici l'email : " + userEmail);
                        addModo(user);
                        Toast.makeText(mContext, "Modérateur ajouté", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton(R.string.ui_no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            builder.create();
            builder.show();
        });
    }

    @Override
    public int getItemCount() { return users.size(); }


    public static void deleteUser(DelatedUser user) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    String endUrl = "delete_user";
                    Token.connectToServer(endUrl,"POST", UserStatic.getAccess(),user, DelatedUser.class,null, null);

                } catch (Exception e) {
                    Log.e("HomeActivity", "Erreur dans receiveHomePage", e);
                }
            }
        }).start();
    }

    public void addModo (DelatedUser user) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    String endUrl = "add_modo";
                    Token.connectToServer(endUrl,"POST", UserStatic.getAccess(),user, DelatedUser.class,null, null);
                } catch (Exception e) {
                    Log.e("HomeActivity", "Erreur dans receiveHomePage", e);
                }
            }
        }).start();
    }


}