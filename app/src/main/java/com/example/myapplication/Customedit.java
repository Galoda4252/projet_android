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

public class Customedit extends Dialog {
    EditText edit_numVist, edit_nom, edit_nb_jours, edit_tarifJours;
    Button buttonModifier, buttonAnnuler_edit;
Custompopup.OnTaskCompleted listener;
    public Customedit(Context context, Custompopup.OnTaskCompleted listener, String numVistValue, String nomValue, String nbJoursValue, String tarifJoursValue) {
        super(context);
        this.listener = listener;
        setContentView(R.layout.popup_edit);

        edit_numVist = findViewById(R.id.edit_numVist);
        edit_nom = findViewById(R.id.edit_nom);
        edit_nb_jours = findViewById(R.id.edit_nb_jours);
        edit_tarifJours = findViewById(R.id.edit_tarifJours);

        edit_numVist.setText(numVistValue);
        edit_nom.setText(nomValue);
        edit_nb_jours.setText(nbJoursValue);
        edit_tarifJours.setText(tarifJoursValue);

        buttonModifier = findViewById(R.id.buttonModifier);

        buttonModifier.setOnClickListener(view -> {
            // Obtenez le texte des EditText et affichez-le dans un Toast
            String numText = edit_numVist.getText().toString();
            String nomText = edit_nom.getText().toString();
            String nbText = edit_nb_jours.getText().toString();
            String tarifText = edit_tarifJours.getText().toString();
            new Customedit.UpdateVisiteurTask().execute(numText, nomText, nbText, tarifText);
        });
    }
    public interface OnTaskCompleted {
        void onTaskCompleted(String result);
    }
    private class UpdateVisiteurTask extends AsyncTask<String, Void, String> {

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
                conn.setRequestMethod("PUT");
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
