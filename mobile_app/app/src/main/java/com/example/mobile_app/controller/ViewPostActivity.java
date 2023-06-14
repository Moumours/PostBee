package com.example.mobile_app.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobile_app.R;
import com.example.mobile_app.model.Comment;
import com.example.mobile_app.model.Document;
import com.example.mobile_app.model.Token;
import com.example.mobile_app.model.RecyclerViewInterface;
import com.example.mobile_app.model.UserStatic;
import com.example.mobile_app.model.ViewPost;
import com.example.mobile_app.model.Comment;
import com.example.mobile_app.model.item_comment.ItemCommentAdapter;
import com.example.mobile_app.model.item_media.MediaAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ViewPostActivity extends AppCompatActivity implements RecyclerViewInterface {

    ImageView mPicture;
    private TextView mTextTitle, mTextContent, mTextAuthor, mTextDate;
    private ViewPost mViewPost;

    private String mTokenAccess;
    private List<Comment> comments = new ArrayList<>();
    private RecyclerView mCommentsRecyclerView;

    private List<Drawable> drawables = new ArrayList<>();
    private RecyclerView mMediaRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        mTextTitle = findViewById(R.id.viewpost_textview_title);
        mTextAuthor = findViewById(R.id.viewpost_textview_author);
        mTextDate = findViewById(R.id.viewpost_textview_date);
        mTextContent = findViewById(R.id.viewpost_textview_content);
        mPicture = findViewById(R.id.XanxanHotPic);

        mCommentsRecyclerView = findViewById(R.id.viewpost_recyclerview_comments);

        mMediaRecyclerView = findViewById(R.id.viewpost_recyclerview_media);
        mMediaRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mMediaRecyclerView.setAdapter(new MediaAdapter(drawables));


        Intent i = getIntent();
        int postId = i.getIntExtra("ID", 0);
        downloadViewPost(postId);

        mCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCommentsRecyclerView.setAdapter(new ItemCommentAdapter(comments, getApplicationContext(), this));

        mTextTitle.setText(i.getStringExtra("TITLE"));
        mTextAuthor.setText(i.getStringExtra("AUTHOR"));
        mTextDate.setText(i.getStringExtra("DATE") + " " + getIntent().getIntExtra("ID", 0));

        mTokenAccess = i.getStringExtra("TOKEN_ACCESS");

        Log.d("ViewPostActivity", "Voici l'id : " + postId);

        // Déclenche le chargement des données du post et des commentaires
        // loadPostAndComments("http://postbee.alwaysdata.net/post/?id=" + postId);

        // ViewPost a aussi les documents et les commentaires
        // ViewPost.getListdocument()
        // ViewPost.getListcomment()

        // RIM
    }

    /*
    private static class FetchPostTask extends AsyncTask<String, Void, Void> {

        private TextView mTextContent;

        private String postText;
        private List<Comment> comments;

        public FetchPostTask(TextView textContent) {
            mTextContent = textContent;
        }


        @Override
        protected Void doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Lire la réponse de l'API
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Analyser la réponse pour extraire le texte du post et les commentaires
                    parsePostAndComments(response.toString());
                } else {
                    Log.e("ViewPostActivity", "Erreur de requête HTTP: " + responseCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // Mettre à jour l'interface utilisateur avec les données récupérées
            mTextContent.setText(postText);

            for (Comment comment : comments) {

                String commentText = comment.getContent();
                String fullName = comment.getUsername();
                int profilePicture = comment.getProfilePicture();
                String date = comment.getDate();

                System.out.println("Comment: " + comment.getContent());
                System.out.println("Full Name: " + comment.getUsername());
                System.out.println("Profile Picture: " + comment.getProfilePicture());
                System.out.println("Date: " + comment.getDate());
            }
        }

        private void parsePostAndComments(String response) throws JSONException {
            // Convertir la réponse en un objet JSON
            JSONObject jsonData = new JSONObject(response);
            // Extraire le texte du post
            postText = jsonData.getString("post_text");
            // Extraire la liste de commentaires
            JSONArray jsonComments = jsonData.getJSONArray("comments");
            comments = new ArrayList<>();

            // Parcourir chaque commentaire dans la liste
            for (int i = 0; i < jsonComments.length(); i++) {
                JSONObject jsonComment = jsonComments.getJSONObject(i);
                // Extraire les détails du commentaire
                String commentText = jsonComment.getString("comment_text");
                String author = jsonComment.getString("author");
                String fullName = jsonComment.getString("full_name");
                String profilePicture = jsonComment.getString("profile_picture");
                String date = jsonComment.getString("date");
                // Créer un objet Comment et l'ajouter à la liste des commentaires
                Comment comment = new Comment(fullName, commentText, 2, Integer.parseInt(profilePicture), date);
                comments.add(comment);
            }
        }
    }

    public void loadPostAndComments(String apiUrl) {
        FetchPostTask task = new FetchPostTask(mTextContent);
        task.execute(apiUrl);
    }
     */


    public void downloadViewPost(int postId) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    /*
                    try {
                        Log.d("ViewPostActivity","Tentative de récupération d'image");
                        InputStream is = (InputStream) new URL("http://postbee.alwaysdata.net/media/images/halo.jpeg").getContent();
                        Drawable d = Drawable.createFromStream(is, "src name");
                        Log.d("ViewPostActivity","Image reçue on la set sur le post");
                        mPicture.setImageDrawable(d);
                        Log.d("ViewPostActivity","Image set");
                    } catch (Exception e) {
                        Log.d("ViewPostActivity","Erreur lors du téléchargement de l'image");
                        Log.d("ViewPostActivity","Erreur : "+e);
                    }
                     */

                    try {

                        File imgFile = new File("/storage/emulated/0/Pictures/IMG_20230614_152524.jpg");

                        if (imgFile.exists()) {

                            Drawable d = Drawable.createFromPath("/storage/emulated/0/Pictures/IMG_20230614_152524.jpg");
                            mPicture.setImageDrawable(d);

                        }
                    }
                    catch (Exception e){
                        Log.d("ViewPostActivity","Erreur lors du téléchargement de l'image");
                        Log.d("ViewPostActivity","Erreur : "+e);
                    }

                    URL url = new URL("http://postbee.alwaysdata.net/post/?id=" + postId);
                    mViewPost = (ViewPost.class).cast(Token.connectToServer("post/?id=" + postId,"GET", UserStatic.getAccess(),null,null, ViewPost.class,null));

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mViewPost != null) {
                                Log.d("ViewPostActivity", "Post content received successfully");
                                mTextContent.setText(mViewPost.getText());
                                if (mViewPost.getComments() != null) {
                                    //Ajout des commentaires
                                    comments.clear();
                                    comments.addAll(mViewPost.getComments());
                                    mCommentsRecyclerView.getAdapter().notifyDataSetChanged();
                                    //Ajout des medias
                                }
                                if (mViewPost.getAttachments() != null){
                                    Log.d("ViewPostActivity", "Liste d'images reçue");
                                    drawables.clear();
                                    for(Document doc : mViewPost.getAttachments()){
                                        try {
                                            if (doc.getType().equals("image")) {
                                                retreivePicture(doc.getUrl());
                                            }
                                        } catch (Exception e) {
                                        }
                                    }

                                    mCommentsRecyclerView.getAdapter().notifyDataSetChanged();
                                }
                            }
                            else{
                                Log.d("ViewPostActivity","Post content not received");
                            }
                        }
                    });
                }
            }
        }).start();
    }

    public void retreivePicture(String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("ViewPostActivity","Tentative de récupération d'image Url : "+url);
                    InputStream is = (InputStream) new URL(url).getContent();
                    Drawable d = Drawable.createFromStream(is, "src name");
                    Log.d("ViewPostActivity","Image reçue");
                    drawables.add(d);
                } catch (Exception e) {
                    Log.d("ViewPostActivity","Erreur lors du téléchargement de l'image");
                    Log.d("ViewPostActivity","Erreur : "+e);
                }
            }
        }).start();
    }

    @Override
    public void onItemClick(int position) { }
}

