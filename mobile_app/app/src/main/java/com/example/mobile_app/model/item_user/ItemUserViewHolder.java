package com.example.mobile_app.model.item_user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.R;
import com.example.mobile_app.model.RecyclerViewInterface;

import java.util.zip.Inflater;

public class ItemUserViewHolder extends RecyclerView.ViewHolder {
    TextView text_fullname, text_email, text_role;
    ImageButton button_resetPassword, button_remove;

    public ItemUserViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
        super(itemView);
        text_fullname = itemView.findViewById(R.id.itemuser_textview_fullname);
        text_email = itemView.findViewById(R.id.itemuser_textview_email);
        text_role = itemView.findViewById(R.id.itemuser_textview_role);
        button_remove = (ImageButton) itemView.findViewById(R.id.itemuser_button_remove);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        });
    }
}
