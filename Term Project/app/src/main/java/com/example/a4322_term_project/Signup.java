package com.example.a4322_term_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {
    Intent intent;
    Button button;
    ImageView back;
    TextView signup, gotoLogin;
    EditText name, email, password, confirmPassword;
    Animation fromleft, fromright, fromtop;

    // When user clicks on signup, display progress bar
    ProgressBar progressBar;

    // Firebase
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        fromleft = AnimationUtils.loadAnimation(this, R.anim.fromleft);
        fromright = AnimationUtils.loadAnimation(this, R.anim.fromright);
        fromtop = AnimationUtils.loadAnimation(this, R.anim.fromtop);

        gotoLogin = findViewById(R.id.gotoLoginTextView);
        signup = findViewById(R.id.signupTextView);
        back = findViewById(R.id.backButtonDrawable);
        button = findViewById(R.id.registerButton);
        name = findViewById(R.id.nameEditText);
        email = findViewById(R.id.usernameTextEdit);
        password = findViewById(R.id.passwordTextEdit);
        confirmPassword = findViewById(R.id.confirmPasswordTextEdit);
        progressBar = findViewById(R.id.progressBar);

        // Animation
        back.setAnimation(fromtop);
        signup.setAnimation(fromleft);
        name.setAnimation(fromright);
        email.setAnimation(fromleft);
        password.setAnimation(fromright);
        confirmPassword.setAnimation(fromleft);
        button.setAnimation(fromright);
        gotoLogin.setAnimation(fromleft);

        // Establish connection to firebase
        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String nameStr = name.getText().toString();
                String emailStr = email.getText().toString();
                String passwordStr = password.getText().toString();
                String passwordConfirmStr = confirmPassword.getText().toString();

                if (nameStr.isEmpty()) {
                    Toast.makeText(Signup.this, "Name is empty.", Toast.LENGTH_SHORT).show();
                    name.requestFocus();
                }
                if (emailStr.isEmpty()) {
                    Toast.makeText(Signup.this, "Email is empty.", Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                }

                if (passwordStr.equals(passwordConfirmStr)) {

                    // All good, sign up the user
                    if (passwordStr.length() > 5) {

                        signupUser();

                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Signup.this, "Password length must be more than 5 characters."
                                , Toast.LENGTH_SHORT).show();
                        password.requestFocus();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(Signup.this, "Passwords do not equal each other."
                            , Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                }
            }
        });

        // If user wants to login while on the signup activity, go to Login activity
        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

        // User clicks on back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });
    }

    private void signupUser() {
        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),
                confirmPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {

                            databaseStorage();

                            intent = new Intent(getApplicationContext(), UserProfile.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            Toast.makeText(getApplicationContext(), "Congrats! You're in.", Toast.LENGTH_SHORT)
                                    .show();
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
    }
    private void databaseStorage() {
        // get current user id
        userID = firebaseAuth.getCurrentUser().getUid();
        String dbName = name.getText().toString();
        String dbEmail = email.getText().toString();



        // Storing in database
        DocumentReference documentReference = fStore.collection("users").document(userID);
        // insert data to map
        Map <String, Object> user = new HashMap<>();
        user.put("name", dbName);
        user.put("email", dbEmail);

        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("DB", "onSuccess: user profile is created -> " + userID);
            }
        });
    }
}
