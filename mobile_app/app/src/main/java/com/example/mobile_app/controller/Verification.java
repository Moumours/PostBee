package com.example.mobile_app.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;

public class Verification {

    public void verifPost (Poste poste) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    URL url = new URL("http://postbee.alwaysdata.net/test");
                    HttpURLConnection django = (HttpURLConnection) url.openConnection();

                    django.setRequestMethod("POST");
                    django.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    django.setRequestProperty("Accept","application/json");
                    django.setDoOutput(true);
                    django.setDoInput(true);

                    Gson gson = new Gson();
                    String jsonPoste = gson.toJson(poste);

                    System.out.println(jsonPoste);

                    try(OutputStreamWriter os = new OutputStreamWriter(django.getOutputStream(), "UTF-8")) {
                        os.write(jsonPoste);
                        os.flush();
                    }

                    int responseCode = django.getResponseCode();
                    System.out.println("\nSending 'POST' request to URL : " + url);
                    System.out.println("Response Code : " + responseCode);

                    BufferedReader in = new BufferedReader(new InputStreamReader(django.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    System.out.println(response.toString());

                    django.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();}
            }
        }).start();
    }

    public void receivePost(String rawPostData) {
        // Convertir la réponse JSON en un objet Poste
        Gson gson = new Gson();
        Poste poste = gson.fromJson(rawPostData, Poste.class);

        System.out.println("Title: " + poste.getTitle());
        System.out.println("Content: " + poste.getText());
        System.out.println("Status: " + poste.getStatus());
    }

}
