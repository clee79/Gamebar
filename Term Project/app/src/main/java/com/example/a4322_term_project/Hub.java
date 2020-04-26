package com.example.a4322_term_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

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
                dialogBuilderSignOut();
            }
        });
    }

    public void dialogBuilderSignOut() {
        // Create alert dialog to prompt user if they are sure they want to
        // sign out
        AlertDialog.Builder signoutWarning = new AlertDialog.Builder(Hub.this);
        signoutWarning.setMessage("Are you sure you want to sign out?");
        signoutWarning.setCancelable(true);

        signoutWarning.setPositiveButton(
                "Sign out",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // SignoutUser function passed here
                        signoutUser();
                    }
                });

        signoutWarning.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = signoutWarning.create();
        alert.show();
    }

    private void signoutUser() {
        FirebaseAuth.getInstance().signOut();
        intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
        finish();
    }

}
