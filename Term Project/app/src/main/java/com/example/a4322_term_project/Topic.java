package com.example.a4322_term_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Topic extends AppCompatActivity {
    Intent i;
    Button myth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        myth = findViewById(R.id.mythologyButton);

        myth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), Quiz.class);
                i.putExtra("topic", 20);

                Log.i("TAG", "onClick: PASSING VALUE");

                startActivity(i);
            }
        });

    }
}
