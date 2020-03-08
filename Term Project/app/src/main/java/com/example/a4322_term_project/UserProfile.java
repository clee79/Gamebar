package com.example.a4322_term_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class UserProfile extends AppCompatActivity {
    Intent intent;
    Button edit, signout, done;
    EditText name, email, phone, restaurantID, tableID;
    ImageView back;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = mAuth.getCurrentUser().getUid();

        edit = findViewById(R.id.editButton);
        done = findViewById(R.id.doneButton);
        back = findViewById(R.id.backButtonDrawable);
        signout = findViewById(R.id.signoutButton);

        name = findViewById(R.id.nameEditText);
        email = findViewById(R.id.emailEditText);
        phone = findViewById(R.id.phoneEditText);
        //restaurantID = findViewById(R.id.restaurantID);
        //tableID = findViewById(R.id.tableID);

        loadUserInformation();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.setVisibility(View.GONE);
                done.setVisibility(View.VISIBLE);
                // NOW THESE ARE ENABLED, NEED TO GET NEW VALUES AND PUSH TO DB
                // DONE BUTTON TO SAVE CHANGES
                name.setEnabled(true);
                email.setEnabled(true);
                phone.setEnabled(true);

                // Focus on email when edit button is clicked
                email.requestFocus();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();

                name.setEnabled(false);
                email.setEnabled(false);
                phone.setEnabled(false);

                done.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
            }
        });
        // Signout button
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signoutUser();
            }
        });

        // Brings user back to Home activity for now, can be change
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Hub.class);
                startActivity(intent);
                finish();
            }
        });

    }
    @Override
    protected void onStart () {
        super.onStart();

        // If user is already logged in, go straight to UserProfile activity
        if (mAuth.getCurrentUser() == null) {
            intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }
    }

    private void signoutUser() {
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

    // ON SIGN OUT CAUSES APP TO CRASH TEMPORARILY
    private void loadUserInformation() {

        if (!userID.isEmpty()) {

            DocumentReference documentReference = fStore.collection("users").document(userID);

            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    name.setText(documentSnapshot.getString("name"));
                    email.setText(documentSnapshot.getString("email"));
                }
            });

        } else {
            intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
            finish();
        }

    }

    // Will currently only update 1 field at a time
    // Ex: You click Edit and you change the email and name fields
    // Only 1 field will update
    private void saveUserInformation() {
        EditText name = findViewById(R.id.nameEditText);
        EditText email = findViewById(R.id.emailEditText);
        EditText phone = findViewById(R.id.phoneEditText);

        String nameStr =  name.getText().toString();
        String emailStr =  email.getText().toString();
        String phoneStr =  phone.getText().toString();

        if (nameStr.isEmpty()) {
            Toast.makeText(this, "Name is required.", Toast.LENGTH_SHORT).show();
            name.requestFocus();
            return;
        }

        if (emailStr.isEmpty()) {
            Toast.makeText(this, "Email is required.", Toast.LENGTH_SHORT).show();
            email.requestFocus();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            // Update display name
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nameStr)
                    .build();

            user.updateEmail(emailStr)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserProfile.this, "Email updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserProfile.this, "Display Updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }
}
