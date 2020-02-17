package com.example.a4322_term_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Signup extends AppCompatActivity {
    Button button;
    EditText username, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        button = findViewById(R.id.registerButton);
        username = findViewById(R.id.usernameTextEdit);
        password = findViewById(R.id.passwordTextEdit);
        confirmPassword = findViewById(R.id.confirmPasswordTextEdit);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals(confirmPassword.getText().toString())) {

                    if (password.getText().toString().length() > 5) {
                        // Do database stuff
                        Log.d("PASSWORD VERIF", "Password passes tests.");
                    } else {
                        Toast.makeText(Signup.this, "Password length must be more than 5 characters."
                                , Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Signup.this, "Passwords do not equal each other."
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
