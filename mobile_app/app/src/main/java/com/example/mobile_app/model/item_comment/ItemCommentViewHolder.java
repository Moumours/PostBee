package com.example.mobile_app.model.item_comment;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.R;
import com.example.mobile_app.model.RecyclerViewInterface;
import com.example.mobile_app.model.UserStatic;

public class ItemCommentViewHolder extends RecyclerView.ViewHolder {
    TextView text_username, text_content, text_role, text_date;
    ImageView image_pfp;
    ImageButton button_edit, button_remove;

    public ItemCommentViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
        super(itemView);
        text_username = itemView.findViewById(R.id.itemcomment_textview_username);
        text_content = itemView.findViewById(R.id.itemcomment_textview_content);
        text_role = itemView.findViewById(R.id.itemcomment_textview_role);
        text_date = itemView.findViewById(R.id.itemcomment_textview_date);
        button_edit = itemView.findViewById(R.id.itemcomment_button_edit);
        button_remove = itemView.findViewById(R.id.itemcomment_button_remove);

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
}
