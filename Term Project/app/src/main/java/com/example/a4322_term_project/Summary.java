package com.example.a4322_term_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Summary extends AppCompatActivity {

    String key;
    int topic, score;
    TextView topicName, questions, correct;
    Intent intent;
    Button continu, restart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        topicName = findViewById(R.id.topicTextView);
        questions = findViewById(R.id.questionTextView);
        correct = findViewById(R.id.correctTextView);
        continu = findViewById(R.id.continueButton);
        restart = findViewById(R.id.newTopicButton);

        // get score
        Bundle b = getIntent().getExtras();
        score = b.getInt("Score");
        topic = b.getInt("Topic");
        key = b.getString("Key");

        topicName.setText(getTopicName(topic));
        questions.setText("Question : " + 10);
        correct.setText("Score : "+score);

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), Topic.class);
                startActivity(intent);
                finish();
            }
        });

        continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), Hub.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
    }

    // get topic name from the topic id
    public String getTopicName(int topicID) {
        switch(topicID) {
            case 20:
                return "Mythology";
            case 22:
                return "Geography";
            case 9:
                return "General Knowledge";
            case 18:
                return "Computer Science";
            case 21:
                return "Sports";
            case 25:
                return "Art";
            case 23:
                return "History";
            case 12:
                return "Music";
            case 15:
                return "Video Games";
            case 24:
                return "Politics";
            default:
                return "Topic Error!";
        }
    }
}
