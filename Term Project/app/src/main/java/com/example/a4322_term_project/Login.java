package com.example.a4322_term_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    ImageView back;
    TextView login;
    EditText email, password;
    Button button;
    Intent intent;
    Animation fromleft, fromright, fromtop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fromleft = AnimationUtils.loadAnimation(this, R.anim.fromleft);
        fromright = AnimationUtils.loadAnimation(this, R.anim.fromright);
        fromtop = AnimationUtils.loadAnimation(this, R.anim.fromtop);

        login = findViewById(R.id.loginTextView);
        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordTextEdit);
        button = findViewById(R.id.loginButton);
        back = findViewById(R.id.backButtonDrawable);

        // Animation
        back.setAnimation(fromtop);
        login.setAnimation(fromleft);
        email.setAnimation(fromright);
        password.setAnimation(fromright);
        button.setAnimation(fromleft);

        // Back button on click
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
            }
        });
    }
}
