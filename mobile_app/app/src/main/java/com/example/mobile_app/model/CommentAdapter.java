package com.example.mobile_app.model;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<com.example.postbee.Comment> commentList;
    private CommentDataRepository commentDataRepository;
    private Context context;

    public CommentAdapter(List<com.example.postbee.Comment> commentList, CommentDataRepository commentDataRepository, Context context) {
        this.commentList = commentList;
        this.commentDataRepository = commentDataRepository;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        com.example.postbee.Comment comment = commentList.get(position);
        holder.bind(comment);
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDeleteCommentDialog(comment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public void setComments(List<com.example.postbee.Comment> comments) {
        commentList = comments;
        notifyDataSetChanged();
    }

    public void addComment(com.example.postbee.Comment comment) {
        commentList.add(comment);
        notifyItemInserted(commentList.size() - 1);
    }

    public void deleteComment(com.example.postbee.Comment comment) {
        commentDataRepository.deleteComment(comment);
        commentList.remove(comment);
        notifyDataSetChanged();
    }

    public com.example.postbee.Comment getComment(int position) {
        if (position >= 0 && position < commentList.size()) {
            return commentList.get(position);
        }
        return null;
    }

    private void showEditDeleteCommentDialog(final com.example.postbee.Comment comment) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Options de commentaire")
                .setItems(new CharSequence[]{"Modifier", "Supprimer"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // Logique pour modifier le commentaire
                                showEditCommentDialog(comment);
                                break;
                            case 1:
                                // Logique pour supprimer le commentaire
                                deleteComment(comment);
                                break;
                        }
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showEditCommentDialog(com.example.postbee.Comment comment) {
        // Implémentez la logique pour afficher une boîte de dialogue de modification du commentaire
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewUsername;
        private TextView textViewCommentContent;
        private TextView textViewDate;
        private Button editButton;

        public CommentViewHolder(View itemView) {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            textViewCommentContent = itemView.findViewById(R.id.textViewCommentContent);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            editButton = itemView.findViewById(R.id.editButton);
        }

        public void bind(com.example.postbee.Comment comment) {
            textViewUsername.setText(comment.getUsername());
            textViewCommentContent.setText(comment.getCommentContent());
            textViewDate.setText(comment.getFormattedDate());
        }
    }
}
