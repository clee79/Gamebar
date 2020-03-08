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
import com.google.firebase.firestore.auth.User;

public class UserProfile extends AppCompatActivity {
    Intent intent;
    Button edit, signout, done;
    EditText name, email, phone, restaurantID, tableID;
    ImageView back;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore fStore;
    DocumentReference documentReference;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Database declarations
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        currentUser = mAuth.getCurrentUser();
        documentReference = fStore.collection("users").document(userID);


        // UserProfile declarations
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
                dialogBuilderSignOut();
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
        if (currentUser== null) {
            intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }
    }


    private void dialogBuilderSignOut() {
        // Create alert dialog to prompt user if they are sure they want to
        // sign out
        AlertDialog.Builder signoutWarning = new AlertDialog.Builder(UserProfile.this);
        signoutWarning.setMessage("Are you sure you want to sign out?");
        signoutWarning.setCancelable(true);
        Log.i("USER ID", "loadUserInformation: " + userID);

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
        // Sign user out
        FirebaseAuth.getInstance().signOut();

        // Go to home page
        intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
        finish();
    }

    private void loadUserInformation() {

            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null && documentSnapshot.getString("name") != null &&
                            documentSnapshot.getString("email") != null) {
                        name.setText(documentSnapshot.getString("name"));
                        email.setText(documentSnapshot.getString("email"));
                    } else {
                        return;
                    }
                }
            });


    }

    // Will currently only update 1 field at a time
    // Ex: You click Edit and you change the email and name fields
    // Only 1 field will update
    private void saveUserInformation() {


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


        if (currentUser != null) {
            // Update email
            currentUser.updateEmail(emailStr)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserProfile.this, "Profile updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UserProfile.this, "Oops! Something went wrong.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            // Update collection db
            documentReference.update("name", nameStr);
            documentReference.update("email", emailStr);
        }
    }
}
