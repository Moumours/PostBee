package com.example.mobile_app.model.item_post;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.R;
import com.example.mobile_app.model.Token;
import com.example.mobile_app.model.UserStatic;
import com.example.mobile_app.model.RecyclerViewInterface;

public class ItemPostValidationViewHolder extends RecyclerView.ViewHolder {
    TextView text_title, text_author, text_date;
    Button accept_button, deny_button;
    int postId;
    private String mTokenAccess = UserStatic.access;
    public ItemPostValidationViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
        super(itemView);

        text_title = itemView.findViewById(R.id.itempost_textview_title);
        text_author = itemView.findViewById(R.id.itempost_textview_author);
        text_date = itemView.findViewById(R.id.itempost_textview_date);

        accept_button = itemView.findViewById(R.id.itempostvalidation_button_accept);
        deny_button = itemView.findViewById(R.id.itempostvalidation_button_deny);

        PostDecision postDecision = new PostDecision();

        accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDecision.setPostId(postId);
                postDecision.setResponse("true");
                sendPostDecision(postDecision);
                Toast.makeText(itemView.getContext(), "Accepted", Toast.LENGTH_SHORT).show();
                Toast.makeText(itemView.getContext(), "Decision : " + postDecision.getResponse(), Toast.LENGTH_SHORT).show();
                Toast.makeText(itemView.getContext(), "ID :" + postId, Toast.LENGTH_SHORT).show();
            }
        });

        deny_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDecision.setPostId(postId);
                postDecision.setResponse("false");
                sendPostDecision(postDecision);
                Toast.makeText(itemView.getContext(), "Denied", Toast.LENGTH_SHORT).show();
                Toast.makeText(itemView.getContext(), "Decision : " + postDecision.getResponse(), Toast.LENGTH_SHORT).show();
                Toast.makeText(itemView.getContext(), "ID :" + postId, Toast.LENGTH_SHORT).show();
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerViewInterface == null) return;
                int pos = getAdapterPosition();
                if (pos == RecyclerView.NO_POSITION) return;
                recyclerViewInterface.onItemClick(pos);
            }
        });
    }

    public void sendPostDecision(PostDecision postDecision) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    //URL url = new URL("http://postbee.alwaysdata.net/approve");
                    String endUrl = "approve";
                    Token.connectToServer(endUrl,"POST",UserStatic.getAccess(),postDecision,PostDecision.class,null, null);

                } catch (Exception e) {
                    Log.e("HomeActivity", "Erreur dans receiveHomePage", e);
                }
            }
        }).start();
    }
}