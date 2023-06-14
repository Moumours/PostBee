package com.example.mobile_app.model.item_post;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.R;
import com.example.mobile_app.model.RecyclerViewInterface;

import java.util.List;

public class ItemPostValidationAdapter extends RecyclerView.Adapter<ItemPostValidationViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;

    Context mContext;
    List<ItemPost> posts;

    public ItemPostValidationAdapter(List<ItemPost> posts, Context context, RecyclerViewInterface recyclerViewInterface) {
        this.posts = posts;
        this.mContext = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public ItemPostValidationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemPostValidationViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_post_validation, parent, false),
                recyclerViewInterface
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ItemPostValidationViewHolder holder, int position) {
        ItemPost post = posts.get(position);
        holder.text_title.setText(posts.get(position).getTitle());
        holder.text_author.setText(posts.get(position).getAuthor().getFull_name());
        holder.text_date.setText(posts.get(position).getDate());
        holder.postId = post.getId();
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}