package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Dashbord extends AppCompatActivity {
    View view4, view3, view1, view2;
    ImageView img , ajoute;
    String id;
    static JSONParser parser = new JSONParser();
    int total;
    String totalScoreString;
    int battrie;
    int bouteille;
    int carton;

    int success;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbord);
        view4 = findViewById(R.id.view4);
        view3 = findViewById(R.id.view3);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        img = findViewById(R.id.imageView39);
        ajoute = findViewById(R.id.ajoute);
       /* ajoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Dashbord.this, parrinage.class);
                startActivity(intent);

            }
        }); */

        registerForContextMenu(ajoute);




        view4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Dashbord.this, score.class);
                startActivity(intent);
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                id = sharedPreferences.getString("user_id", ""); // Récupérer l'ID de l'utilisateur
                // Utilisez l'ID de l'utilisateur comme vous le souhaitez
                Log.d("UserId", "UserID: " + id); // Affiche l'ID de l'utilisateur dans le logcat
                // Envoi de l'ID de l'utilisateur à get_categorie.php


            }
        });
        view3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Dashbord.this, profile.class);
                startActivity(intent);

            }
        });
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                id = sharedPreferences.getString("user_id", ""); // Récupérer l'ID de l'utilisateur
                // Utilisez l'ID de l'utilisateur comme vous le souhaitez
                Log.d("UserId", "UserID: " + id); // Affiche l'ID de l'utilisateur dans le logcat
                // Lancer la tâche asynchrone pour récupérer les catégories de l'utilisateur
                new SendUserIdTask().execute(id);

                // Lancer l'intent pour afficher la première layout
                Intent intent1 = new Intent(Dashbord.this, alert.class);
                startActivity(intent1);

                // Utiliser un Handler pour déclencher l'intent pour la deuxième layout après 3 secondes
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Lancer l'intent pour afficher la deuxième layout
                        Intent intent2 = new Intent(Dashbord.this, shooping.class);
                        startActivity(intent2);
                        // Fermer l'activité actuelle pour éviter un retour inattendu
                        finish();
                    }
                }, 3000); // Délai de 3 secondes en millisecondes
            }
        });
        view2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Dashbord.this, maps.class);
                startActivity(intent);

            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Dashbord.this, acceuille.class);
                startActivity(intent);

            }
        });


    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_ajout) {
            Toast.makeText(Dashbord.this, "invite!!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Dashbord.this, parrinage.class);
            startActivity(intent);
            // Code à exécuter si la deuxième condition est remplie
            return true;
        }


        // Gérez les éléments de menu contextuel ici
        return super.onContextItemSelected(item);
    }

    // AsyncTask pour récupérer le score de l'utilisateur
    // AsyncTask pour envoyer l'ID de l'utilisateur au serveur
    class SendUserIdTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            // Créer une HashMap pour stocker les paramètres de la requête
            HashMap<String, String> map = new HashMap<>();
            map.put("id", strings[0]); // Ajouter l'ID de l'utilisateur à la HashMap

            // Faire une requête HTTP POST pour envoyer l'ID de l'utilisateur au serveur
            JSONObject object = parser.makeHttpRequest("   https://9973-102-173-92-180.ngrok-free.app/php/resultat_reduction.php", "POST", map);

            // Afficher la réponse du serveur
            android.util.Log.d("ServerResponse", object.toString());

            // Traitement de la réponse pour récupérer le score total
            try {
                boolean isSuccess = object.getBoolean("success");
                if (isSuccess) {
                    // Extraire le score total en tant que chaîne de caractères
                    totalScoreString = object.getString("total_score");

                    // Convertir la chaîne en entier
                    total = Integer.parseInt(totalScoreString);

                    // Afficher le score total
                    android.util.Log.d("votre score", "total: " + total);
                } else {
                    // Aucune donnée trouvée pour l'utilisateur actuel
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // Afficher un message ou effectuer d'autres actions après l'envoi de l'ID
            Toast.makeText(Dashbord.this, "ID envoyé avec succès", Toast.LENGTH_SHORT).show();

            // Stocker les valeurs dans les préférences partagées
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("score", total);
            editor.apply(); // Appliquer les modifications




        }
    }


}



