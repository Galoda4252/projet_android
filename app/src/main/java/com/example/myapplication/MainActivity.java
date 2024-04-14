package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Custompopup.OnTaskCompleted {
    private ListView listViewVisiteur;
    private Button ajouter_visiteur;
    private TextView textView8,textView12;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewVisiteur = findViewById(R.id.listView);
        ajouter_visiteur = findViewById(R.id.add);
        textView8=findViewById(R.id.textView8);
        textView12=findViewById(R.id.textView12);
        fetchData();

        ajouter_visiteur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Custompopup customPopup = new Custompopup(MainActivity.this, MainActivity.this);
                customPopup.show();
            }
        });
        listViewVisiteur.setOnItemClickListener((parent, view, position, id) -> {
            TextView numTextView = view.findViewById(R.id.num);
            TextView nomTextView = view.findViewById(R.id.nom);
            TextView nbTextView = view.findViewById(R.id.nb_jour);
            TextView tarifTextView = view.findViewById(R.id.T);

            String numValue = numTextView.getText().toString();
            String nomValue = nomTextView.getText().toString();
            String nbValue = nbTextView.getText().toString();
            String tarifValue = tarifTextView.getText().toString();

            showAlertDialog(numValue, nomValue, nbValue, tarifValue);
        });
    }

    private void showAlertDialog(String numValue, String nomValue, String nbValue, String tarifValue) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Action ");
        builder.setMessage("Choisissez votre action pour le visiteur: " + nomValue);

        builder.setNegativeButton("Modifier", (dialog, which) -> {
            // Code de suppression à insérer ici
            showCustomEditDialog(numValue, nomValue, nbValue, tarifValue);
        });

        builder.setPositiveButton("Supprimer", (dialog, which) -> {
            new DeleteVisiteurTask().execute(numValue);
            fetchData();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showCustomEditDialog(String numValue, String nomValue, String nbValue, String tarifValue) {
        Customedit customedit = new Customedit(MainActivity.this, MainActivity.this,numValue, nomValue, nbValue, tarifValue);
        customedit.show();
    }

    void fetchData() {
        new FetchDataTask().execute();
    }

    private class FetchDataTask extends AsyncTask<Void, Void, List<Visiteur>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        String response = null;
        Url uri = new Url();
        String BASE_URL = uri.getBASE_URL();

        @Override
        protected List<Visiteur> doInBackground(Void... voids) {
            List<Visiteur> visiteurList = new ArrayList<>();

            try {
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
                    visiteurList.add(new Visiteur(numvist, nom, nb_jours, tarifJ, tarif));
                }

                reader.close();
                inputStream.close();
                conn.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return visiteurList;
        }

        @Override
        protected void onPostExecute(List<Visiteur> visiteurList) {
            super.onPostExecute(visiteurList);
            if (visiteurList != null && !visiteurList.isEmpty()) {
                VisiteurAdapter adapter = new VisiteurAdapter(MainActivity.this, visiteurList);
                listViewVisiteur.setAdapter(adapter);
            }
        }
    }

    private class MinMax extends AsyncTask<Void, Void, List<Visiteur>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        String response = null;
        Url uri = new Url();
        String BASE_URL = uri.getBASE_URL();

        @Override
        protected List<Visiteur> doInBackground(Void... voids) {
            List<Visiteur> minmax = new ArrayList<>();

            try {
                URL url = new URL(BASE_URL+"minmax");
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


                    int min = jsonObject.getInt("min");
                    int max = jsonObject.getInt("max");
                    minmax.add(new Visiteur(min ,max));
                }

                reader.close();
                inputStream.close();
                conn.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return minmax;
        }

        @Override
        protected void onPostExecute(List<Visiteur> minmax) {
            super.onPostExecute(minmax);

            if (minmax != null && !minmax.isEmpty()) {

            }
        }
    }
    private class DeleteVisiteurTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String numVist = params[0];
            String response = null;
            HttpURLConnection conn = null;
            Url uri = new Url();
            String BASE_URL = uri.getBASE_URL();
            try {
                URL url = new URL(BASE_URL + numVist);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("DELETE");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setDoOutput(true);

                // Lire la réponse du serveur
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    response = stringBuilder.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect(); // Fermer la connexion
                }
            }

            // Retourner la réponse du serveur
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Context context = MainActivity.this; // Obtenir le contexte de l'activité principale

            if (result != null) {
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            } else {
                // Sinon, affichez un message d'erreur
                Toast.makeText(context, "Erreur lors de la suppression du visiteur", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onTaskCompleted(String result) {
        // Rafraîchir la liste des visiteurs après l'ajout
        if (result != null && result.equals("ok")) {
            fetchData(); // Mettre à jour la liste après l'ajout
        }
    }
}
