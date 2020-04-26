package com.example.a4322_term_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class Multiplayer extends AppCompatActivity {

    Button p1, p2, p3, p4;
    Intent gamestate;
    int category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        // Get the information passed from the topic intent
        // Pass that info along to new intent for game state.
        Bundle bundle = getIntent().getExtras();
        category = bundle.getInt("topic");
        Log.i("TAG", "CATEGORY -> " + Integer.toString(category));

        gamestate = new Intent(getApplicationContext(), Quiz.class);
        gamestate.putExtra("topic", category);
        gamestate.putExtra("Index", 1);

        final GameplayManager gameplayManager = new GameplayManager();

        // Buttons
        p1 = findViewById(R.id.p1);
        p1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gamestate.putExtra("players", 1);
                startActivity(gamestate);
            }
        });
        p2 = findViewById(R.id.p2);
        p2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gamestate.putExtra("players", 2);
                startActivity(gamestate);
            }
        });
        p3 = findViewById(R.id.p3);
        p3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gamestate.putExtra("players", 3);
                startActivity(gamestate);
            }
        });
        p4 = findViewById(R.id.p4);
        p4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gamestate.putExtra("players", 4);
                startActivity(gamestate);
            }
        });
    }
}
