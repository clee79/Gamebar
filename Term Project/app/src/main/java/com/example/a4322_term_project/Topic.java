package com.example.a4322_term_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Topic extends AppCompatActivity {
    Intent i;
    Button myth, art, geography, gk, cs, sports, history, music, videoGames, politics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        myth = findViewById(R.id.mythologyButton);
        geography = findViewById(R.id.geographyButton);
        gk = findViewById(R.id.generalKnowledgeButton);
        cs = findViewById(R.id.computerScienceButton);
        sports = findViewById(R.id.sportsButton);
        art = findViewById(R.id.artButton);
        history = findViewById(R.id.historyButton);
        music = findViewById(R.id.musicButton);
        videoGames = findViewById(R.id.videoGamesButton);
        politics = findViewById(R.id.politicsButton);

        myth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), Multiplayer.class);
                i.putExtra("topic", 20);

                Log.i("TAG", "onClick: PASSING VALUE");

                startActivity(i);
            }
        });

        geography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), Multiplayer.class);
                i.putExtra("topic", 22);

                Log.i("TAG", "onClick: PASSING VALUE");

                startActivity(i);
            }
        });

        gk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), Multiplayer.class);
                i.putExtra("topic", 9);

                Log.i("TAG", "onClick: PASSING VALUE");

                startActivity(i);
            }
        });

        cs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), Multiplayer.class);
                i.putExtra("topic", 18);

                Log.i("TAG", "onClick: PASSING VALUE");

                startActivity(i);
            }
        });

        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), Multiplayer.class);
                i.putExtra("topic", 21);

                Log.i("TAG", "onClick: PASSING VALUE");

                startActivity(i);
            }
        });

        art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), Multiplayer.class);
                i.putExtra("topic", 25);

                Log.i("TAG", "onClick: PASSING VALUE");

                startActivity(i);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), Multiplayer.class);
                i.putExtra("topic", 23);

                Log.i("TAG", "onClick: PASSING VALUE");

                startActivity(i);
            }
        });

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), Multiplayer.class);
                i.putExtra("topic", 12);

                Log.i("TAG", "onClick: PASSING VALUE");

                startActivity(i);
            }
        });

        videoGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), Multiplayer.class);
                i.putExtra("topic", 15);

                Log.i("TAG", "onClick: PASSING VALUE");

                startActivity(i);
            }
        });

        politics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), Multiplayer.class);
                i.putExtra("topic", 24);

                Log.i("TAG", "onClick: PASSING VALUE");

                startActivity(i);
            }
        });

    }
}
