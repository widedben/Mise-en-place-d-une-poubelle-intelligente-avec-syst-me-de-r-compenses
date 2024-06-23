package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;

public class profile extends AppCompatActivity {
    String userId;
    String name;
    String mail;
    EditText email,phone,Id,Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        email=findViewById(R.id.editTextText9);
        Name=findViewById(R.id.editTextText7);
        Id=findViewById(R.id.editTextText8);
        phone=findViewById(R.id.editTextText10);
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        mail = sharedPreferences.getString("mail", "");
        name = sharedPreferences.getString("name", "");
        userId = sharedPreferences.getString("user_id", "");
        Id.setText(String.valueOf(userId));
        email.setText(mail);
        Name.setText(name);


    }
}