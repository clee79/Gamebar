package com.example.a4322_term_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Hub extends AppCompatActivity {
    Button profile, stats, trivia, qrCode, logout;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        profile = findViewById(R.id.profileButton);
        stats = findViewById(R.id.statsButton);
        trivia = findViewById(R.id.triviaButton);
        qrCode = findViewById(R.id.qrButton);
        logout = findViewById(R.id.logout);


        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), UserProfile.class);
                startActivity(intent);
                finish();
            }
        });

        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), StatisticsActivity.class);
                startActivity(intent);
                finish();
            }
        });


        trivia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Topic.class);
                startActivity(intent);
                finish();
            }
        });

        qrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), QRCodeProcessor.class);
                startActivity(intent);
                finish();
                // Go to QR code screen
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Build alert dialog in the future
                UserProfile.getUserProfileInstance().signoutUser();
            }
        });
    }
}
