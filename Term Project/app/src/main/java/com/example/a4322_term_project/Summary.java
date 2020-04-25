package com.example.a4322_term_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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
    Button continu, restart;

    // Establish connection to firebase
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;
    DocumentReference documentReference;
    FirebaseDatabase firebaseDatabase;
    String userID;


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


        // Save data to database
        storeGameScore(topic, score);

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
        // get current user id
        userID = firebaseAuth.getCurrentUser().getUid();
        // Get random key for document
        String key = getKey();
        String dbTopic = getTopicName(topic);
        String dbCorrect = Integer.toString(right);
        String dbQuizDate = getDate();
        int dbTotalGames = 1;

        // Storing in database
        DocumentReference documentReference = fStore.collection("quiz").document(key);

        Log.d("TAG", "GENERATED KEY " + key);

        // insert data to map
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
        });
    }

    public String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String strDate = dateFormat.format(date).toString();

        return strDate;
    }
    /*
    public int getCurrentGames () {
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null && documentSnapshot.getLong("totalGames").intValue() > 0) {
                    Log.i("TAG", "onEvent: GOT TO CURRENT GAMES");
                } else {
                    return;
                }
            }
        });

        return currentGamesPlayed;

    }*/

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
