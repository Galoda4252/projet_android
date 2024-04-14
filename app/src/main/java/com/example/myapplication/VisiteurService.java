package com.example.myapplication;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VisiteurService {
    String BASE_URL = "http://192.168.10.157:8080/backend/rest/visiteur";
    public List<Visiteur> fetchVisiteurs() throws Exception {
        List<Visiteur> visiteurList = new ArrayList<>();
        URL url = new URL(BASE_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        InputStream inputStream = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        JSONArray jsonArray = new JSONArray(response.toString());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String numvist = jsonObject.getString("numVist");
            String nom = jsonObject.getString("nom");
            int nb_jours = jsonObject.getInt("nb_jours");
            int tarifJ = jsonObject.getInt("tarifJ");
            int tarif = jsonObject.getInt("tarif");
            visiteurList.add(new Visiteur(numvist, nom, nb_jours, tarifJ,tarif));
        }
        reader.close();
        inputStream.close();
        conn.disconnect();
        return visiteurList;
    }
}
