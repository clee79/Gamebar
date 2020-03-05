package com.example.a4322_term_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {
    TextView gamebar;
    Button signinButton, registerButton;
    Animation frombottom, fromtop;
    Intent intent;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mFirebaseAuth = FirebaseAuth.getInstance();

        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        fromtop = AnimationUtils.loadAnimation(this, R.anim.fromtop);

        gamebar = findViewById(R.id.gamebarTextView);
        signinButton = findViewById(R.id.signinButton);
        registerButton = findViewById(R.id.registerButton);

        gamebar.setAnimation(fromtop);
        signinButton.setAnimation(frombottom);
        registerButton.setAnimation(frombottom);


        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Signup.class);
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
    }
}
