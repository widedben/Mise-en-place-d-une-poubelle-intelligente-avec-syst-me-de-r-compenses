package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class forget_password extends AppCompatActivity {
    EditText mail;
    ImageView button;

    private static final String TAG = "ForgetPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        mail=findViewById(R.id.editTextText11);
        button=findViewById(R.id.imageView40);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendResetPasswordRequest().execute(mail.getText().toString());
            }
        });
    }

    class SendResetPasswordRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String mail = strings[0];
            String url = "   https://9973-102-173-92-180.ngrok-free.app/php/forget_password.php";
            String data = "mail=" + mail;
            String result = "";

            try {
                Log.d(TAG, "Sending POST request to: " + url);
                Log.d(TAG, "POST data: " + data);

                URL serverUrl = new URL(url);
                // Ouvre une connexion HTTP vers l'URL spécifiée
                HttpURLConnection urlConnection = (HttpURLConnection) serverUrl.openConnection();
                // Spécifie que la requête est une requête POST
                urlConnection.setRequestMethod("POST");
                // Permet à la connexion de sortir des données
                urlConnection.setDoOutput(true);
                // Récupère le flux de sortie de la connexion pour écrire les données POST

                OutputStream out = urlConnection.getOutputStream();
                // Écrit les données POST dans le flux de sortie de la connexion
                out.write(data.getBytes());
                // Vide le flux de sortie pour s'assurer que toutes les données sont envoyées
                out.flush();
                out.close();

                int statusCode = urlConnection.getResponseCode();
                Log.d(TAG, "Response code: " + statusCode);
// Vérifie si le code de statut de la réponse est 200, ce qui signifie que la requête a réussi
                if (statusCode == 200) {
                    // Crée un BufferedReader pour lire la réponse de la requête
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    // Initialise un StringBuilder pour stocker la réponse
                    StringBuilder stringBuilder = new StringBuilder();
                    // Lit chaque ligne de la réponse et l'ajoute au StringBuilder
                    String line;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    reader.close();
                    result = stringBuilder.toString();
                    Log.d(TAG, "Response from server: " + result);
                } else {
                    result = "Failed to retrieve response from server.";
                    Log.e(TAG, "Failed to retrieve response from server. Status code: " + statusCode);
                }
            } catch (IOException e) {
                e.printStackTrace();
                result = "Error occurred while sending request.";
                Log.e(TAG, "Error occurred while sending request: " + e.getMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                boolean success = jsonObject.getBoolean("success");
                if (success) {
                    Intent intent = new Intent(forget_password.this, new_password.class);
                    intent.putExtra("mail", mail.getText().toString());
                    startActivity(intent);
                    finish();
                } else {
                    String error = jsonObject.getString("error");
                    Toast.makeText(forget_password.this, error, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(forget_password.this, "Failed to parse server response.", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
