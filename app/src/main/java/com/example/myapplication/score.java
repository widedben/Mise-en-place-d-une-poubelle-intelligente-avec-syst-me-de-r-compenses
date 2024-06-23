package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class score extends AppCompatActivity {
    int battrie;
    int bouteille;
    int carton;
    int total;
    String userId;
    ImageView img, img2, img3;

    // JSONParser pour faire des requêtes HTTP
    JSONParser parser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        // Récupérer l'ID de l'utilisateur depuis les préférences partagées
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        userId = sharedPreferences.getString("user_id", ""); // "" est la valeur par défaut si la clé n'existe pas

        // Utilisez l'ID de l'utilisateur comme vous le souhaitez
        Log.d("UserId", "UserID: " + userId); // Affiche l'ID de l'utilisateur dans le logcat

        // Appeler l'AsyncTask pour envoyer les données au serveur
        new GetCategories().execute(userId);

        View shadowBattrieLayout = findViewById(R.id.shadow_battrie);
        if (shadowBattrieLayout != null) {
            img = shadowBattrieLayout.findViewById(R.id.imageView24);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(score.this, shooping.class);
                    startActivity(intent);
                }
            });
        }

        View shadowbouteilleLayout = findViewById(R.id.shadow_bouteille);
        if (shadowbouteilleLayout != null) {
            img2 = shadowbouteilleLayout.findViewById(R.id.imageView24);
            img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(score.this, shooping.class);
                    startActivity(intent);
                }
            });
        }

        View shadowcartonLayout = findViewById(R.id.shadow_carton);
        if (shadowcartonLayout != null) {
            img3 = shadowcartonLayout.findViewById(R.id.imageView21);
            img3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(score.this, shooping.class);
                    startActivity(intent);
                }
            });
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("total", total);
        editor.apply(); // Appliquer les modifications
    }

    class GetCategories extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> map = new HashMap<>();
            map.put("id", strings[0]);

            JSONObject object = parser.makeHttpRequest("  https://9973-102-173-92-180.ngrok-free.app/php/api_get_categorie.php", "POST", map);

            // Afficher la réponse du serveur
            android.util.Log.d("ServerResponse", object.toString());

            // Traitement de la réponse pour récupérer les catégories de l'utilisateur
            try {
                boolean isSuccess = object.getBoolean("success");
                if (isSuccess) {
                    JSONObject data = object.getJSONObject("data");
                    battrie = data.getInt("battrie");
                    bouteille = data.getInt("bouteille");
                    carton = data.getInt("carton");

                    // Afficher les valeurs récupérées
                    android.util.Log.d("Categories", "Battrie: " + battrie + ", Bouteille: " + bouteille + ", Carton: " + carton);
                } else {
                    // Aucune donnée trouvée pour l'utilisateur actuel
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return object.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                try {
                    JSONObject object = new JSONObject(s);
                    boolean isSuccess = object.getBoolean("success");
                    if (isSuccess) {
                        JSONObject data = object.getJSONObject("data");
                        battrie = data.getInt("battrie");
                        bouteille = data.getInt("bouteille");
                        carton = data.getInt("carton");

                        // Stocker les valeurs dans les préférences partagées
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("battrie", battrie);
                        editor.putInt("bouteille", bouteille);
                        editor.putInt("carton", carton);
                        editor.apply(); // Appliquer les modifications

                        // Mettre à jour les vues UI dans le thread principal
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Mettre à jour les EditTexts avec les nouvelles valeurs
                                View shadowBattrieLayout = findViewById(R.id.shadow_battrie);
                                if (shadowBattrieLayout != null) {
                                    EditText editTextBattrie = shadowBattrieLayout.findViewById(R.id.editTextText6);
                                    editTextBattrie.setText(String.valueOf(battrie));
                                }

                                View shadowbouteilleLayout = findViewById(R.id.shadow_bouteille);
                                if (shadowbouteilleLayout != null) {
                                    EditText editTextbouteille = shadowbouteilleLayout.findViewById(R.id.editTextText6);
                                    editTextbouteille.setText(String.valueOf(bouteille));
                                }

                                View shadowcartonLayout = findViewById(R.id.shadow_carton);
                                if (shadowcartonLayout != null) {
                                    EditText editTextcarton = shadowcartonLayout.findViewById(R.id.editTextText6);
                                    editTextcarton.setText(String.valueOf(carton));
                                }
                            }
                        });
                    } else {
                        // Gérer le cas où isSuccess est faux
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // Gérer le cas où la réponse est null
                Toast.makeText(score.this, "Réponse JSON nulle", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
