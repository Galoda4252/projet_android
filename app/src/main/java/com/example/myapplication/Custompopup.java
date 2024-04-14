package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Custompopup extends Dialog {
    EditText text_numVist, text_nom, text_nb_jours, text_tarifJours;
    Button buttonAjouter, buttonAnnuler;
    OnTaskCompleted listener;
    public Custompopup(Context context, OnTaskCompleted listener) {
        super(context);
        this.listener = listener;
        setContentView(R.layout.popup_edit);
        text_numVist = findViewById(R.id.text_numVist);
        text_nom = findViewById(R.id.text_nom);
        text_nb_jours = findViewById(R.id.text_nb_jours);
        text_tarifJours = findViewById(R.id.text_tarifJours);
        buttonAjouter = findViewById(R.id.buttonAjouter);
        buttonAnnuler = findViewById(R.id.buttonAnnuler);

        buttonAjouter.setOnClickListener(v -> {
            // Récupérer les valeurs des EditText
            String numVist = text_numVist.getText().toString().trim();
            String nom = text_nom.getText().toString().trim();
            String nb_jours = text_nb_jours.getText().toString().trim();
            String tarifJours = text_tarifJours.getText().toString().trim();

            if (numVist.isEmpty() || nom.isEmpty() || nb_jours.isEmpty() || tarifJours.isEmpty()) {
                Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Exécuter l'AsyncTask en passant les données du visiteur en tant qu'arguments
            new AddVisiteurTask().execute(numVist, nom, nb_jours, tarifJours);

        });

        // Définition de l'action lorsque le bouton "buttonAnnuler" est cliqué
        buttonAnnuler.setOnClickListener(v -> dismiss());
    }
    public interface OnTaskCompleted {
        void onTaskCompleted(String result);
    }
    private class AddVisiteurTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String numVist = params[0];
            String nom = params[1];
            String nb_jours = params[2];
            String tarifJours = params[3];

            String response = null;
            Url uri =new Url();
            try {
                URL url = new URL(uri.getBASE_URL());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setDoOutput(true);

                // Construire les paramètres de la requête
                String urlParameters = "numVist=" + URLEncoder.encode(numVist, "UTF-8") +
                        "&nom=" + URLEncoder.encode(nom, "UTF-8") +
                        "&nb_jours=" + nb_jours +
                        "&tarifJ=" + tarifJours;

                // Écrire les paramètres dans le corps de la requête
                try (OutputStream outputStream = conn.getOutputStream()) {
                    outputStream.write(urlParameters.getBytes());
                }

                // Lire la réponse du serveur
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    response = stringBuilder.toString();
                }

                // Fermer la connexion
                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Retourner la réponse du serveur
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            if (listener != null) {
                listener.onTaskCompleted(result); // Appel de la méthode de callback avec le résultat
            }
            Context context = getContext();
            if (result != null ) {
                dismiss();
                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            } else {
                // Sinon, affichez un message d'erreur
                Toast.makeText(context, "Erreur lors de l'ajout du visiteur", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
