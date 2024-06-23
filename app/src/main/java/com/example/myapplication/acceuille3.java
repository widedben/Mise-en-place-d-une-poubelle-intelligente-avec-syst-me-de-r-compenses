package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class acceuille3 extends AppCompatActivity {
    private float startX, endX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuille3);
        ConstraintLayout constraintLayout = findViewById(R.id.mainLayout);
        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        if (endX < startX) {
                            // Swipe vers la gauche, naviguer vers l'activité suivante
                            Intent intent = new Intent(acceuille3.this, MainActivity.class);
                            startActivity(intent);
                        } else if (endX > startX) {
                            // Swipe vers la droite, naviguer vers l'activité précédente
                            finish();
                        }
                        break;
                }
                return true;
            }
        });
    }
}