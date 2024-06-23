package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class new_password extends AppCompatActivity {
    EditText new_password, opt;
    ImageView button;
    String mail;
    ProgressDialog dialog;
    JSONParser parser = new JSONParser();
    int success;

    boolean passwordVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);
        mail = getIntent().getStringExtra("mail");
        new_password = findViewById(R.id.editTextText13);
        opt = findViewById(R.id.code);
        button = findViewById(R.id.imageView42);

        new_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int Right=2;
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if (event.getRawX()>=new_password.getRight()-new_password.getCompoundDrawables()[Right].getBounds().width()){
                        int Selection=new_password.getSelectionEnd();
                        if(passwordVisible){
                            //set drawable image here
                            new_password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.visibilite_off,0);
                            //for hide password
                            new_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible=false;
                        }

                        else {

                            //set drawable image here
                            new_password.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.password_icone,0);
                            //for hide password
                            new_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible=true;
                        }
                        new_password.setSelection(Selection);
                        return true;

                    }
                }
                return false;
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Add().execute();
            }
        });
    }

    // Classe interne Add
    class Add extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(new_password.this);
            dialog.setMessage("Patience SVP");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("opt", opt.getText().toString());
            map.put("mail", mail);
            map.put("new_password", new_password.getText().toString());

            JSONObject object = parser.makeHttpRequest( "   https://9973-102-173-92-180.ngrok-free.app/php/new_password.php", "POST", map);

            int startIndex = object.toString().indexOf("{");
            String jsonString = object.toString().substring(startIndex);

            Log.d("ServerResponse", jsonString);

            try {
                JSONObject jsonResult = new JSONObject(jsonString);
                success = jsonResult.getInt("success");
            } catch (JSONException e) {
                e.printStackTrace();
                return "Erreur lors de la conversion de la réponse en objet JSON.";
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.cancel();

            if (success == 1) {
                Toast.makeText(new_password.this, "Ajout effectué", Toast.LENGTH_SHORT).show();
                // Passer à l'activité suivante
                Intent intent = new Intent(new_password.this, SING_IN.class);
                startActivity(intent);
            }
            else {
                Toast.makeText(new_password.this, "Echec !!", Toast.LENGTH_SHORT).show();
            }

        }

    }
}
