package com.example.myapplication;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;


import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;






public class SING_IN extends AppCompatActivity {
    EditText id;
    EditText Password;
    Button boutton;
    boolean passwordVisible;

    ProgressDialog dialog;
    TextView sign_up, forget_password;

    JSONParser parser = new JSONParser();

    int success;
    int battrie;
    int bouteille;
    int carton;

    String mail;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in);
        id = findViewById(R.id.editTextText);
        Password = findViewById(R.id.editTextText1);
        boutton = findViewById(R.id.mon_bouton);
        sign_up = findViewById(R.id.textView9);
        forget_password = findViewById(R.id.textView5);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SING_IN.this, SIGN_UP.class);
                startActivity(intent);

            }
        });
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SING_IN.this, forget_password.class);
                startActivity(intent);

            }
        });

        Password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int Right = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= Password.getRight() - Password.getCompoundDrawables()[Right].getBounds().width()) {
                        int Selection = Password.getSelectionEnd();
                        if (passwordVisible) {
                            //set drawable image here
                            Password.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibilite_off, 0);
                            //for hide password
                            Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible = false;
                        } else {

                            //set drawable image here
                            Password.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.password_icone, 0);
                            //for hide password
                            Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;
                        }
                        Password.setSelection(Selection);
                        return true;

                    }
                }
                return false;
            }
        });

        boutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Log().execute();

            }
        });
    }


    class Log extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(SING_IN.this);
            dialog.setMessage("Patientez SVP");
            dialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {
// Créez une nouvelle instance de HashMap appelée "map" pour stocker des paires clé-valeur.
            HashMap map = new HashMap<>();

            map.put("id", id.getText().toString());
            map.put("password", Password.getText().toString());
//envoie une requête HTTP à l'URL spécifiée avec la méthode spécifiée  POT et les paramètres fournis  dans  la HashMap "map".
            JSONObject object = parser.makeHttpRequest("   https://9973-102-173-92-180.ngrok-free.app/php/sign_in.php", "POST", map);

            // Afficher la réponse du serveur
            android.util.Log.d("ServerResponse", object.toString());

            try {
                // Tentative de récupération de la valeur associée à la clé "success" dans la réponse du serveur.
                success = object.getInt("success");
            } catch (JSONException e) {
                // En cas d'erreur lors de la récupération de la valeur, une exception JSONException est attrapée
                // et l'erreur est imprimée dans les logs.
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.cancel();

            android.util.Log.d("ServerResponse", "success: " + success); // Utilisation correcte de Log.d pour afficher un log

            if (success == 1) {
                Toast.makeText(SING_IN.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
                // Redirection vers une autre activité
                Intent intent = new Intent(SING_IN.this, Dashbord.class);
                startActivity(intent);


                // Envoi de l'ID de l'utilisateur à profile.php
                new profilee().execute(id.getText().toString());

                // Stocker l'ID de l'utilisateur dans les préférences partagées
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user_id", id.getText().toString());
                editor.apply(); // Appliquer les modifications

            } else {
                Toast.makeText(SING_IN.this, "Aucun utilisateur avec cet id ou mot de passe", Toast.LENGTH_SHORT).show();
            }
        }


        class profilee extends AsyncTask<String, String, String> {


            @Override
            protected String doInBackground(String... strings) {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", strings[0]);

                JSONObject object = parser.makeHttpRequest("    https://9973-102-173-92-180.ngrok-free.app/php/profile.php", "POST", map);

                // Afficher la réponse du serveur
                android.util.Log.d("ServerResponse", object.toString());

                // Traitement de la réponse pour récupérer les catégories de l'utilisateur
                try {
                    boolean isSuccess = object.getBoolean("success");
                    if (isSuccess) {
                        JSONObject data = object.getJSONObject("data");
                        mail = data.getString("mail");
                        name = data.getString("name");


                        // Afficher les valeurs récupérées
                        android.util.Log.d("les donnes", "mail: " + mail + ", name: " + name);
                    } else {
                        // Aucune donnée trouvée pour l'utilisateur actuel
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return object.toString();

            }


            // Dans votre onPostExecute de la classe profilee
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (s != null) {
                    try {
                        JSONObject object = new JSONObject(s);
                        String name = object.getString("name");
                        String mail = object.getString("mail");

                        // Stocker les valeurs dans les préférences partagées
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("mail", mail);
                        editor.putString("name", name);
                        editor.apply(); // Appliquer les modifications



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Gérer le cas où la réponse est null
                    Toast.makeText(SING_IN.this, "Réponse JSON nulle", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}

