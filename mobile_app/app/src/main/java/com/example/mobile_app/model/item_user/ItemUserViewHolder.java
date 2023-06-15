package com.example.mobile_app.model.item_user;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mobile_app.R;
import com.example.mobile_app.model.RecyclerViewInterface;

public class ItemUserViewHolder extends RecyclerView.ViewHolder {
    TextView text_fullname, text_email, text_role, text_is_staff;
    ImageButton button_addModo, button_remove;


    public ItemUserViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
        super(itemView);
        text_fullname = itemView.findViewById(R.id.itemuser_textview_fullname);
        text_email = itemView.findViewById(R.id.itemuser_textview_email);
        text_role = itemView.findViewById(R.id.itemuser_textview_role);
        text_is_staff = itemView.findViewById(R.id.itemuser_textview_staff);
        button_addModo = itemView.findViewById(R.id.itemuser_button_addModo);
        button_remove = itemView.findViewById(R.id.itemuser_button_remove);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {}
        });
    }
}
