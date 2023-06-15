package com.example.mobile_app.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobile_app.R;
import com.example.mobile_app.model.Comment;
import com.example.mobile_app.model.Document;
import com.example.mobile_app.model.ResponseData;
import com.example.mobile_app.model.Token;
import com.example.mobile_app.model.RecyclerViewInterface;
import com.example.mobile_app.model.User;
import com.example.mobile_app.model.UserStatic;
import com.example.mobile_app.model.Video;
import com.example.mobile_app.model.ViewPost;
import com.example.mobile_app.model.Comment;
import com.example.mobile_app.model.item_comment.ItemCommentAdapter;
import com.example.mobile_app.model.item_media.ImageAdapter;
import com.example.mobile_app.model.item_media.VideoAdapter;

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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ViewPostActivity extends AppCompatActivity implements RecyclerViewInterface {

    private TextView mTextTitle, mTextContent, mTextAuthor, mTextDate;
    private ViewPost mViewPost;

    private String mTokenAccess;
    private List<Comment> comments = new ArrayList<>();
    private RecyclerView mCommentsRecyclerView;

    private List<Drawable> drawablePictures = new ArrayList<>();
    private List<Uri> uriVideos = new ArrayList<>();
    private List<Document> documents = new ArrayList<>();
    private RecyclerView mPicturesRecyclerView, mVideosRecyclerView, mDocumentsRecyclerView;

    private Button mButtonComment;
    private EditText mTextComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        mTextTitle = findViewById(R.id.viewpost_textview_title);
        mTextAuthor = findViewById(R.id.viewpost_textview_author);
        mTextDate = findViewById(R.id.viewpost_textview_date);
        mTextContent = findViewById(R.id.viewpost_textview_content);
        //Commentaires
        mButtonComment = findViewById(R.id.viewpost_button_postcomment);
        mTextComment = findViewById(R.id.viewpost_edittext_writecomment);
        mCommentsRecyclerView = findViewById(R.id.viewpost_recyclerview_comments);
        mPicturesRecyclerView = findViewById(R.id.viewpost_recyclerview_pictures);
        mVideosRecyclerView = findViewById(R.id.viewpost_recyclerview_videos);
        mDocumentsRecyclerView = findViewById(R.id.viewpost_recyclerview_documents);

        Intent i = getIntent();
        int postId = i.getIntExtra("ID", 0);
        downloadViewPost(postId);

        mCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCommentsRecyclerView.setAdapter(new ItemCommentAdapter(comments, getApplicationContext(), this));
        mPicturesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mPicturesRecyclerView.setAdapter(new ImageAdapter(drawablePictures));
        mVideosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mVideosRecyclerView.setAdapter(new VideoAdapter(uriVideos));
        //mDocumentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mDocumentsRecyclerView.setAdapter(new (documents));

        mTextTitle.setText(i.getStringExtra("TITLE"));
        mTextAuthor.setText(i.getStringExtra("AUTHOR"));
        mTextDate.setText(i.getStringExtra("DATE"));

        mTokenAccess = i.getStringExtra("TOKEN_ACCESS");

        Log.d("ViewPostActivity", "Voici l'id : " + postId);

        // Déclenche le chargement des données du post et des commentaires
        // loadPostAndComments("http://postbee.alwaysdata.net/post/?id=" + postId);

        // ViewPost a aussi les documents et les commentaires
        // ViewPost.getListdocument()
        // ViewPost.getListcomment()

        // RIM
        mButtonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ResponseData response = null;
                        try {
                            String commentText = mTextComment.getText().toString();
                            HashMap<String, String> params = new HashMap<String, String>();
                            params.put("text", commentText);
                            params.put("post", Integer.toString(postId));
                            response = (ResponseData) Token.connectToServer("comment", "POST", UserStatic.getAccess(), params, params.getClass(), ResponseData.class, null);
                        } catch (Exception e) {
                            Log.d("ViewPostActivity", "Erreur lors de l'upload d'un commentaire");
                            Log.d("ViewPostActivity", "Erreur : " + e);
                        } finally {
                            ResponseData finalResponse = response;
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if (finalResponse != null) {
                                        Log.d("ViewPostActivity", "Response : Success :" + finalResponse.getSuccess() + " | " + "Message :" + finalResponse.getMessage());
                                        if (finalResponse.getSuccess().equals("True")) {
                                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.hideSoftInputFromWindow(mTextComment.getWindowToken(), 0);
                                            mTextComment.setText("");
                                            Toast.makeText(ViewPostActivity.this, finalResponse.getMessage(), Toast.LENGTH_LONG).show();
                                            downloadViewPost(postId);
                                        } else {
                                            if (finalResponse.getMessage() != null) {
                                                Toast.makeText(ViewPostActivity.this, "Erreur : " + finalResponse.getMessage(), Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(ViewPostActivity.this, "Erreur : le post n'existe pas", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    } else {
                                        Log.d("ViewPostActivity", "Error : no server response");
                                    }
                                }
                            });
                        }


                    }
                }).start();

            }
        });
    }

    public void downloadViewPost(int postId) {
        new Thread(new Runnable() {
            public void run() {
                try {
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
                                    for (Comment comment : mViewPost.getComments()){
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                            String rawStringDate = comment.getDate();
                                            Log.d("HomeActivity","rawStringDate : "+rawStringDate);
                                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);
                                            LocalDateTime date = LocalDateTime.parse(rawStringDate, formatter);
                                            Log.d("HomeActivity","Conversion de la date : "+date.toString());
                                            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.FRENCH);
                                            Log.d("HomeActivity","Conversion de la date : "+date.format(formatter2).toString());
                                            comment.setDate(date.format(formatter2).toString());
                                        }
                                    }
                                    //Ajout des commentaires
                                    comments.clear();
                                    comments.addAll(mViewPost.getComments());
                                    mCommentsRecyclerView.getAdapter().notifyDataSetChanged();
                                    //Ajout des medias
                                }
                                if (mViewPost.getAttachments() != null){
                                    Log.d("ViewPostActivity", "Liste d'images reçue");
                                    drawablePictures.clear();
                                    uriVideos.clear();
                                    for(Document doc : mViewPost.getAttachments()){
                                        try {
                                            Thread t;
                                            switch (doc.getType()) {
                                                case "image": t = retreivePicture(doc.getUrl()); break;
                                                case "video": t = retreiveVideo(doc.getUrl()); break;
                                                default: t = retreiveDocument(doc); break;
                                            }
                                            t.start();
                                            t.join();
                                        } catch (Exception e) {
                                        }
                                    }
                                    mPicturesRecyclerView.getAdapter().notifyDataSetChanged();
                                    mVideosRecyclerView.getAdapter().notifyDataSetChanged();
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

    public Thread retreivePicture(String url) throws InterruptedException {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("ViewPostActivity","Tentative de récupération d'image Url : "+url);
                    InputStream is = (InputStream) new URL(url).getContent();
                    Drawable d = Drawable.createFromStream(is, "src name");
                    Log.d("ViewPostActivity","Image reçue");
                    drawablePictures.add(d);
                } catch (Exception e) {
                    Log.d("ViewPostActivity","Erreur lors du téléchargement de l'image");
                    Log.d("ViewPostActivity","Erreur : "+e);
                }
            }
        });
    }

    public Thread retreiveVideo(String url) throws InterruptedException {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("ViewPostActivity","Tentative de récupération de vidéo Url : "+url);
                    InputStream is = (InputStream) new URL(url).getContent();
                    Uri uri = Uri.parse(url);
                    Log.d("ViewPostActivity","Vidéo reçue");
                    uriVideos.add(uri);
                } catch (Exception e) {
                    Log.d("ViewPostActivity","Erreur lors du téléchargement de la vidéo");
                    Log.d("ViewPostActivity","Erreur : "+e);
                }
            }
        });
    }

    public Thread retreiveDocument(Document document) throws InterruptedException {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                /*try {
                    Log.d("ViewPostActivity","Tentative de récupération d'image Url : "+url);
                    InputStream is = (InputStream) new URL(url).getContent();
                    Drawable d = Drawable.createFromStream(is, "src name");
                    Log.d("ViewPostActivity","Image reçue");
                    drawablePictures.add(d);
                } catch (Exception e) {
                    Log.d("ViewPostActivity","Erreur lors du téléchargement de l'image");
                    Log.d("ViewPostActivity","Erreur : "+e);
                }*/
            }
        });
    }

    @Override
    public void onItemClick(int position) { }
}

