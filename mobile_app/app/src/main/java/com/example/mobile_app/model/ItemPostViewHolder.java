package com.example.mobile_app.model;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.R;

public class ItemPostViewHolder extends RecyclerView.ViewHolder {
    TextView text_title, text_author, text_date;

    public ItemPostViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
        super(itemView);
        text_title = itemView.findViewById(R.id.itempost_textview_title);
        text_author = itemView.findViewById(R.id.itempost_textview_author);
        text_date = itemView.findViewById(R.id.itempost_textview_date);

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
