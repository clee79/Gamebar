package com.example.a4322_term_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Summary extends AppCompatActivity {

    String key;
    int topic, score;
    TextView topicName, questions, correct;
    Intent intent;
    Button continu, restart, nextplayer;

    // Establish connection to firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore fStore;
    String userID;

    GameplayManager gameplayManager;

    int currentPlayer, index;
    int[] playerArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        topicName = findViewById(R.id.topicTextView);
        questions = findViewById(R.id.questionTextView);
        correct = findViewById(R.id.correctTextView);
        continu = findViewById(R.id.continueButton);
        restart = findViewById(R.id.newTopicButton);
        nextplayer = findViewById(R.id.nextPlayer);
        nextplayer.setVisibility(View.GONE);


        // get score
        Bundle b = getIntent().getExtras();
        score = b.getInt("Score");
        topic = b.getInt("Topic");
        key = b.getString("Key");
        currentPlayer = b.getInt("players");
        index = b.getInt("Index");

        // check if multiplayer and inside loop state
        if(currentPlayer > 1 && index + 1 < currentPlayer + 1){
            nextplayer.setVisibility(View.VISIBLE);
            nextplayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Logic to loop to next game player.
                    Intent gameLoop = new Intent(getApplicationContext(), Quiz.class);
                    gameLoop.putExtra("topic", topic);
                    index++;
                    gameLoop.putExtra("Index", index);
                    gameLoop.putExtra("players", currentPlayer);
                    startActivity(gameLoop);
                }
            });
        }

        topicName.setText(getTopicName(topic));
        questions.setText("Question : " + 10);
        correct.setText("Score : "+score);

        // Firebase declarations
        // Make sure we're not updating the db with multiplayer data.
        // Only update if 1 player.
        if(currentPlayer < 2) {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();
            userID = firebaseUser.getUid();

            // Save data to database
            storeGameScore(topic, score);
        }

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

    public void storeGameScore (int topic, int right) {
        // Get random key for document
        String key = getKey();

        String dbTopic = getTopicName(topic);
        String dbCorrect = Integer.toString(right);
        String dbQuizDate = getDate();
        String dbUserID = userID;

        // Storing in database
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference newGameRef = firestore.collection("quiz").document();
        Stat game = new Stat();

        game.setDate(dbQuizDate);
        game.setTopic(dbTopic);
        game.setScore(dbCorrect);
        game.setUserID(dbUserID);

        newGameRef.set(game).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("tag", "onComplete: COMPLETE");
                } else {
                    Log.d("tag", "onComplete: FAILURE");

                }
            }
        });


        Log.d("TAG", "GENERATED KEY " + key);

        // insert data to map
        /*
        Map <String, Object> quiz = new HashMap<>();
        quiz.put("userID", userID);
        quiz.put("topic", dbTopic);
        quiz.put("correct", dbCorrect);
        quiz.put("data", dbQuizDate);


        documentReference.set(quiz).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("DB", "onSuccess: user profile is created -> " + userID);
            }
        });*/
    }

    public String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String strDate = dateFormat.format(date).toString();

        return strDate;
    }

    public String getKey () {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(16);

        for (int i = 0; i < 16; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index = (int)(AlphaNumericString.length() * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }
}
