package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class shooping extends AppCompatActivity {
    boolean reductionSuccess = false;
    EditText score;
    int new_reduction;
    int total;
    String id;
    String mail;
    static JSONParser parser = new JSONParser();
    ImageView dash;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shooping);
        score = findViewById(R.id.editTextText7);
        dash=findViewById(R.id.imageView32);
        dash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(shooping.this, Dashbord.class);
                startActivity(intent);

            }
        });



        // Récupérer l'ID de l'utilisateur et son adresse e-mail depuis les SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        id = sharedPreferences.getString("user_id", "");
        mail = sharedPreferences.getString("mail", "");


        // Afficher le score de l'utilisateur
        total = sharedPreferences.getInt("score", 0);
        score.setText(String.valueOf(total));

        // Cliquer sur l'image pour envoyer l'ID et l'adresse e-mail au serveur
        View zara = findViewById(R.id.zara);
        if (zara != null) {
            ImageView img = zara.findViewById(R.id.imageView36);
            ImageView zar = zara.findViewById(R.id.imageView35);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Lancer la tâche asynchrone pour envoyer l'ID et l'adresse e-mail au serveur
                    new SendUserIdTask().execute(id, mail,"Shein");
                }
            });
            zar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Créer une Intent implicite avec l'action ACTION_VIEW et l'URI de la page web
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://fr.shein.com/"));

                   // Démarrer l'Intent
                    startActivity(intent);

                }
            });
        }
        // Cliquer sur l'image pour envoyer l'ID et l'adresse e-mail au serveur
        View hamadi = findViewById(R.id.hamadi);

        if (hamadi != null) {
            ImageView ham = hamadi.findViewById(R.id.imageView35);
            ImageView img2 = hamadi.findViewById(R.id.imageView36);
            img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Lancer la tâche asynchrone pour envoyer l'ID et l'adresse e-mail au serveur
                    new SendUserIdTask().execute(id, mail,"Hamadi");



                }
            });
            ham.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Créer une Intent implicite avec l'action ACTION_VIEW et l'URI de la page web
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ha.com.tn/"));

                    // Démarrer l'Intent
                    startActivity(intent);

                }
            });
        }
        // Cliquer sur l'image pour envoyer l'ID et l'adresse e-mail au serveur
        View geant = findViewById(R.id.geant);
        if (geant != null) {
            ImageView gean = geant.findViewById(R.id.imageView35);
            ImageView img3 =geant.findViewById(R.id.imageView36);
            img3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Lancer la tâche asynchrone pour envoyer l'ID et l'adresse e-mail au serveur
                    new SendUserIdTask().execute(id, mail,"Geant");



                }
            });
            gean.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Créer une Intent implicite avec l'action ACTION_VIEW et l'URI de la page web
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.geant.tn/"));

                    // Démarrer l'Intent
                    startActivity(intent);

                }
            });
        }
        // Cliquer sur l'image pour envoyer l'ID et l'adresse e-mail au serveur
        View huwei = findViewById(R.id.huwei);
        if (huwei != null) {
            ImageView huwe = huwei.findViewById(R.id.imageView35);
            ImageView img4 =huwei.findViewById(R.id.imageView36);
            img4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Lancer la tâche asynchrone pour envoyer l'ID et l'adresse e-mail au serveur
                    new SendUserIdTask().execute(id, mail,"Huwei");



                }
            });
            huwe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Créer une Intent implicite avec l'action ACTION_VIEW et l'URI de la page web
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://consumer.huawei.com/tn/"));

                    // Démarrer l'Intent
                    startActivity(intent);

                }
            });
        }

    }


    // AsyncTask pour envoyer l'ID de l'utilisateur et son adresse e-mail au serveur
    class SendUserIdTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            // Récupérer l'ID de l'utilisateur et son adresse e-mail depuis les paramètres
            String id = strings[0];
            String mail = strings[1];
            String marque = strings[2]; // Récupérer la marque depuis les paramètres

            // Créer une HashMap pour stocker les paramètres de la requête
            HashMap<String, String> map = new HashMap<>();
            map.put("id", id); // Ajouter l'ID de l'utilisateur à la HashMap
            map.put("mail", mail); // Ajouter l'adresse e-mail à la HashMap
            map.put("marque", marque); // Ajouter la marque à la HashMap

            // Faire une requête HTTP POST pour envoyer l'ID de l'utilisateur au serveur
            JSONObject object = parser.makeHttpRequest("  https://9973-102-173-92-180.ngrok-free.app/php/reduction.php", "POST", map);

            // Afficher la réponse du serveur
            android.util.Log.d("ServerResponse", object.toString());

            // Traitement de la réponse pour récupérer les catégories de l'utilisateur
            try {
                boolean isSuccess = object.getBoolean("success");
                if (isSuccess) {
                    JSONObject data = object.getJSONObject("data");
                    new_reduction = data.getInt("new_reduction");
                    // Récupérer le message de toast depuis la réponse JSON





                    // Afficher les valeurs récupérées
                    android.util.Log.d(" new_reduction", " new_reduction: " + new_reduction);
                } else {
                    // Aucune donnée trouvée pour l'utilisateur actuel
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return object;
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            super.onPostExecute(object);

            if (object != null) {
                try {
                    boolean isSuccess = object.getBoolean("success");
                    if (isSuccess) {
                        JSONObject data = object.getJSONObject("data");
                        int new_reduction = data.getInt("new_reduction");

                        // Stocker les valeurs dans les préférences partagées
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("new_reduction", new_reduction);
                        editor.apply(); // Appliquer les modifications

                        // Afficher un Toast pour tester si les données sont bien récupérées
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String toastMessage = " new_reduction: " + new_reduction;
                                Toast.makeText(shooping.this, toastMessage, Toast.LENGTH_SHORT).show();
                                score.setText(String.valueOf(new_reduction));
                            }
                        });
                    } else {
                        // Gérer le cas où isSuccess est faux
                        // Aucune donnée trouvée pour l'utilisateur actuel
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // Gérer le cas où la réponse est null
                Toast.makeText(shooping.this, "Réponse JSON nulle", Toast.LENGTH_SHORT).show();
            }
        }

    }
}