package com.example.a4322_term_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Hub extends AppCompatActivity {
    Button profile, qrCode;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hub);

        profile = findViewById(R.id.profileButton);
        qrCode = findViewById(R.id.qrButton);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), UserProfile.class);
                startActivity(intent);
                finish();
            }
        });

        qrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to QR code screen
            }
        });
    }
}
