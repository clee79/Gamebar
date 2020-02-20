package com.example.a4322_term_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity {
    Button button;
    EditText username, password, confirmPassword;

    // When user clicks on signup, display progress bar
    ProgressBar progressBar;

    // Firebase
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        button = findViewById(R.id.registerButton);
        username = findViewById(R.id.usernameTextEdit);
        password = findViewById(R.id.passwordTextEdit);
        confirmPassword = findViewById(R.id.confirmPasswordTextEdit);
        progressBar = findViewById(R.id.progressBar);

        // Establish connection to firebase
        firebaseAuth = FirebaseAuth.getInstance();

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                if (password.getText().toString().equals(confirmPassword.getText().toString())) {

                    if (password.getText().toString().length() > 5) {

                        firebaseAuth.createUserWithEmailAndPassword(username.getText().toString(),
                                confirmPassword.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBar.setVisibility(View.GONE);

                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(), "Congrats! You're in.", Toast.LENGTH_SHORT)
                                                    .show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    }
                                });

                        Log.d("PASSWORD VERIF", "Password passes tests.");
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Signup.this, "Password length must be more than 5 characters."
                                , Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(Signup.this, "Passwords do not equal each other."
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
