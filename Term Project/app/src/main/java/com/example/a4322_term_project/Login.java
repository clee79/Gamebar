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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    ImageView back;
    TextView login, gotoSignup;
    EditText email, password;
    Button button;
    Intent intent;
    Animation fromleft, fromright, fromtop;
    ProgressBar progress;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();

        fromleft = AnimationUtils.loadAnimation(this, R.anim.fromleft);
        fromright = AnimationUtils.loadAnimation(this, R.anim.fromright);
        fromtop = AnimationUtils.loadAnimation(this, R.anim.fromtop);

        login = findViewById(R.id.loginTextView);
        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordTextEdit);
        button = findViewById(R.id.loginButton);
        back = findViewById(R.id.backButtonDrawable);
        gotoSignup = findViewById(R.id.goToSignupTextView);
        progress = findViewById(R.id.progressBar);

        // Animation
        back.setAnimation(fromtop);
        login.setAnimation(fromleft);
        email.setAnimation(fromright);
        password.setAnimation(fromright);
        button.setAnimation(fromleft);
        gotoSignup.setAnimation(fromleft);

        // Back button on click
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });

        //
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
 
                if (firebaseUser != null) {
                    intent = new Intent(getApplicationContext(), UserProfile.class);
                } else {
                    Toast.makeText(Login.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);

                loginUser();

            }
        });

        // If user wants to signup while on the login activity, go to Signup activity
        gotoSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                intent = new Intent(getApplicationContext(), Signup.class);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onStart () {
        super.onStart();

        // If user is already logged in, go straight to UserProfile activity
        if (mFirebaseAuth.getCurrentUser() != null) {
            finish();
            intent = new Intent(this, UserProfile.class);
            startActivity(intent);
        }
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    private void loginUser() {
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();

        if (emailStr.isEmpty()) {
            Toast.makeText(Login.this, "Email is empty."
                    , Toast.LENGTH_SHORT).show();
            email.requestFocus();
        } else if (passwordStr.length() < 5) {
            Toast.makeText(Login.this, "Your password looks a little short... Give it another shot."
                    , Toast.LENGTH_SHORT).show();
            password.requestFocus();
        } else {

            // Login logic
            mFirebaseAuth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(Login.this,
                    new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progress.setVisibility(View.GONE);

                            if (!task.isSuccessful()) {
                                Toast.makeText(Login.this, "Login failed! Try again.", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d("FIREBASE USER LOGIN", FirebaseAuth.getInstance().getCurrentUser().toString());
                                finish();
                                intent = new Intent(getApplicationContext(), UserProfile.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            }
                        }
                    });
        }
    }
}
