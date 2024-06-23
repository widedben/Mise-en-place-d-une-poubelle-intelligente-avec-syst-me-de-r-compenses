package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SIGN_UP extends AppCompatActivity {

    private EditText codeEditText;
    EditText name;
    EditText ID;
    EditText Gmail;
    EditText Password;
    EditText pass2;
    Button bouton2;
    int battrie;
    int bouteille;
    int carton;

    ProgressDialog dialog;

    JSONParser parser = new JSONParser();

    int success;

    boolean passwordVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        bouton2 = findViewById(R.id.bouton2);
        name = findViewById(R.id.editTextText);
        ID = findViewById(R.id.editTextText2);
        Gmail = findViewById(R.id.editTextText1);
        Password = findViewById(R.id.editTextText3);
        pass2 = findViewById(R.id.editTextText4);
        codeEditText = new EditText(this);

        Password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int Right=2;
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if (event.getRawX()>=Password.getRight()-Password.getCompoundDrawables()[Right].getBounds().width()){
                        int Selection=Password.getSelectionEnd();
                        if(passwordVisible){
                            //set drawable image here
                            Password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.visibilite_off,0);
                            //for hide password
                            Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible=false;
                        }

                        else {

                            //set drawable image here
                            Password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.password_icone,0);
                            //for hide password
                            Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible=true;
                        }
                        Password.setSelection(Selection);
                        return true;

                    }
                }
                return false;
            }
        });

        pass2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int Right=2;
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if (event.getRawX()>=pass2.getRight()-pass2.getCompoundDrawables()[Right].getBounds().width()){
                        int Selection=pass2.getSelectionEnd();
                        if(passwordVisible){
                            //set drawable image here
                            pass2.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.visibilite_off,0);
                            //for hide password
                            pass2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible=false;
                        }

                        else {

                            //set drawable image here
                            pass2.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.password_icone,0);
                            //for hide password
                            pass2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible=true;
                        }
                        pass2.setSelection(Selection);
                        return true;

                    }
                }
                return false;
            }
        });

        /*bouton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Add().execute();

            }
        });
    }*/
        bouton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SIGN_UP.this);
                builder.setTitle("Verification du parrain")
                        .setMessage("Etes-vous un parrain ?")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AlertDialog.Builder codeBuilder = new AlertDialog.Builder(SIGN_UP.this);
                                codeBuilder.setTitle("Code du parrain")
                                        .setView(codeEditText)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String code = codeEditText.getText().toString();
                                                // Dans SIGN_UP
                                                SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = preferences.edit();
                                                editor.putString("codeParrain", code);
                                                editor.apply();

                                                new Add().execute(code);
                                            }
                                        })
                                        .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        })
                                        .show();
                            }
                        })
                        .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                new Add().execute("");
                            }
                        })
                        .show();
            }
        });
    }


    class Add extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(SIGN_UP.this);
            dialog.setMessage("Patience SVP");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("nom", name.getText().toString());
            map.put("id", ID.getText().toString());
            map.put("mail", Gmail.getText().toString());
            map.put("password", Password.getText().toString());
            map.put("pass2", pass2.getText().toString());
            map.put("code", strings[0]); // Ajout du code du parrain



            if (!map.get("password").equals(map.get("pass2"))) {
                // Les mots de passe ne correspondent pas, retourner un message d'erreur
                return "Les mots de passe ne correspondent pas !";
            }

            JSONObject object = parser.makeHttpRequest("   https://9973-102-173-92-180.ngrok-free.app/php/sign_up.php", "GET", map);

            // Trouver l'index du premier '{' pour extraire l'objet JSON
            int startIndex = object.toString().indexOf("{");
            String jsonString = object.toString().substring(startIndex);

            // Afficher la réponse du serveur
            Log.d("ServerResponse", jsonString);

            try {
                // Convertir la partie JSON en objet JSON
                JSONObject jsonResult = new JSONObject(jsonString);
                success = jsonResult.getInt("success");
            } catch (JSONException e) {
                e.printStackTrace();
                // Gestion de l'erreur JSON
                return "Erreur lors de la conversion de la réponse en objet JSON.";
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.cancel();

            if (s != null) {
                // Afficher un message d'erreur en cas de mots de passe non correspondants
                Toast.makeText(SIGN_UP.this, s, Toast.LENGTH_SHORT).show();
                // Effacer les champs de mot de passe pour que l'utilisateur puisse les remplir à nouveau
                Password.setText("");
                pass2.setText("");
                return;
            }

            if (success == 1) {
                Toast.makeText(SIGN_UP.this, "Ajout effectué", Toast.LENGTH_SHORT).show();

                // Passer à l'activité suivante
                Intent intent = new Intent(SIGN_UP.this, Dashbord.class);
                startActivity(intent);
            } else {
                Toast.makeText(SIGN_UP.this, "Echec !!", Toast.LENGTH_SHORT).show();
            }
        }

    }


}
