package com.example.mobile_app.model.item_post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.R;
import com.example.mobile_app.model.RecyclerViewInterface;

import java.util.List;

public class ItemPostAdapter extends RecyclerView.Adapter<ItemPostViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;

    Context mContext;
    List<ItemPost> posts;

    public ItemPostAdapter(List<ItemPost> posts, Context context, RecyclerViewInterface recyclerViewInterface) {
        this.posts = posts;
        this.mContext = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ItemPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemPostViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_post, parent, false),
                recyclerViewInterface
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ItemPostViewHolder holder, int position) {
        holder.text_title.setText(posts.get(position).getTitle());
        holder.text_author.setText(posts.get(position).getAuthor().getFull_name());
        holder.text_date.setText(posts.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}