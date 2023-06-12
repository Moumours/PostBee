package com.example.mobile_app.model.item_post;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_app.R;
import com.example.mobile_app.controller.Poste;
import com.example.mobile_app.model.User;
import com.google.gson.Gson;
import com.example.mobile_app.model.RecyclerViewInterface;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ItemPostValidationViewHolder extends RecyclerView.ViewHolder {
    TextView text_title, text_author, text_date;
    Button accept_button, deny_button;
    int postId;
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
                postDecision.setResponse("accepted");
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
                postDecision.setResponse("denied");
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
                    URL url = new URL("https://postbee.alwaysdata.net/register");
                    //TODO:Mettre la bonn URL (Pour envoiyer les décision des admin au serveur)
                    HttpURLConnection django = (HttpURLConnection) url.openConnection();

                    django.setRequestMethod("POST");
                    django.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    django.setRequestProperty("Accept","application/json");
                    django.setDoOutput(true);
                    django.setDoInput(true);

                    Gson gson = new Gson();
                    String jsonDecision = gson.toJson(postDecision);

                    System.out.println(jsonDecision);

                    Log.d("MainActivity", "Envoi de la requête de la decision d'un poste");

                    try (OutputStreamWriter os = new OutputStreamWriter(django.getOutputStream(), "UTF-8")) {
                        os.write(jsonDecision);
                        os.flush();
                        Log.d("MainActivity", "Requête envoyée avec succès");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("MainActivity", "Erreur lors de l'envoi de la requête : " + e.getMessage());
                    }

                    int responseCode = django.getResponseCode();
                    Log.d("MainActivity", "Code de réponse : " + responseCode);

                    BufferedReader in = new BufferedReader(new InputStreamReader(django.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    Log.d("MainActivity", "Réponse du serveur : " + response);


                    System.out.println(response.toString());

                    django.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();}
            }
        }).start();
    }
}
