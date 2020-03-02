package com.example.a4322_term_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class UserProfile extends AppCompatActivity {
    Intent intent;
    Button edit, signout;
    EditText email, phone, other;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        edit = findViewById(R.id.editButton);
        back = findViewById(R.id.backButtonDrawable);
        signout = findViewById(R.id.signoutButton);
        email = findViewById(R.id.emailEditText);
        phone = findViewById(R.id.phoneEditText);
        other = findViewById(R.id.otherEditText);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // NOW THESE ARE ENABLED, NEED TO GET NEW VALUES AND PUSH TO DB
                // DONE BUTTON TO SAVE CHANGES
                email.setEnabled(true);
                phone.setEnabled(true);
                other.setEnabled(true);

                email.requestFocus();
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
                                Log.d("FIREBASE USER", FirebaseAuth.getInstance().getCurrentUser().toString());
                                FirebaseAuth.getInstance().signOut();
                                Log.d("FIREBASE USER", FirebaseAuth.getInstance().getCurrentUser().toString());

                                // Go to home page
                                intent = new Intent(getApplicationContext(), Home.class);
                                startActivity(intent);
                                finish();
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
