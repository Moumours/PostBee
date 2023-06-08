package com.example.mobile_app.model;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;

public class CommentMainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private EditText editTextComment;
    private Button buttonSubmit;
    private CommentDataRepository commentDataRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiser commentDataRepository
        commentDataRepository = CommentDataRepository.getInstance();

        // Référencer les vues EditText et Button
        editTextComment = findViewById(R.id.editTextComment);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        // Initialisation de RecyclerView et CommentAdapter
        recyclerView = findViewById(R.id.recyclerView);
        commentAdapter = new CommentAdapter(commentDataRepository.getComments(), commentDataRepository, this);
        recyclerView.setAdapter(commentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Écouteur de clic sur le bouton de soumission
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer le commentaire saisi dans le champ EditText
                String commentContent = editTextComment.getText().toString();

                // Les valeurs pour Comment
                int commentId = 1;  // Remplacer par la valeur réelle de commentId
                String username = "Rim";  // Remplacer par la valeur réelle du nom d'utilisateur
                Date date = new Date();  // Remplacer par la valeur réelle de la date

                // Créer un nouvel objet Comment avec les données du commentaire
                com.example.postbee.Comment comment = new com.example.postbee.Comment(commentId, username, commentContent, date);

                // Ajouter le commentaire à la liste de commentaires ou à la base de données
                commentDataRepository.addComment(comment);

                // Mettre à jour l'affichage de RecyclerView
                commentAdapter.addComment(comment);

                // Effacer le champ EditText après avoir soumis le commentaire
                editTextComment.setText("");
            }
        });
    }

    public void onDeleteButtonClick(View view) {
        // Récupérer le commentaire correspondant à l'élément de la liste
        int position = recyclerView.getChildLayoutPosition(view);
        com.example.postbee.Comment comment = commentAdapter.getComment(position);

        // Supprimer le commentaire en appelant la méthode deleteComment du CommentAdapter
        commentAdapter.deleteComment(comment);
    }
}
