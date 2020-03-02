package com.example.a4322_term_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class UserProfile extends AppCompatActivity {
    Intent intent;
    Button edit;
    ImageView back;
    TextView signout;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        edit = findViewById(R.id.editButton);
        back = findViewById(R.id.backButtonDrawable);
        signout = findViewById(R.id.signoutTextView);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change the uneditable EditText (email, name, phone, etc.) to editable

            }
        });

        // Signout button
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create alert dialog to prompt user if they are sure they want to
                // sign out
                AlertDialog.Builder signoutWarning = new AlertDialog.Builder(UserProfile.this);
                signoutWarning.setMessage("Are you sure you want to sign out?");
                signoutWarning.setCancelable(true);

                signoutWarning.setPositiveButton(
                        "Sign out",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Sign user out
                                FirebaseAuth.getInstance().signOut();
                                // Go to home page
                                intent = new Intent(getApplicationContext(), Home.class);
                                startActivity(intent);
                                dialog.cancel();
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
        });

        // Brings user back to Login for now, can be change
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);

            }
        });

    }
}
