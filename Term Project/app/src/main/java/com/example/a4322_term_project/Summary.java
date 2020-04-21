package com.example.a4322_term_project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Summary extends AppCompatActivity {

    String key;
    int currentGamesPlayed = 0;
    int topic, score;
    TextView topicName, questions, correct;
    Intent intent;
    Button continu, restart;

    // Establish connection to firebase
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;
    DocumentReference documentReference;
    DatabaseReference databaseReference;
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

        // Database instances
        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        documentReference = fStore.collection("quiz").document(userID);


        // Save data
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

        String dbTopic = getTopicName(topic);
        String dbCorrect = Integer.toString(right);
        String dbQuizDate = getDate();
        //int dbTotalGames = getCurrentGames() + 1;
        // Storing in database
        DocumentReference documentReference = fStore.collection("quiz").document(userID);

        // insert data to map
        Map<String, Object> quiz = new HashMap<>();
        quiz.put("topic", dbTopic);
        quiz.put("correct", dbCorrect);
        quiz.put("date", dbQuizDate);
        quiz.put("totalGames", 10);

        documentReference.update("topic", dbTopic);
        documentReference.update("correct", dbCorrect);
        documentReference.update("date", dbQuizDate);
        documentReference.update("totalGames", 10);



        documentReference.set(quiz).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("DB", "onSuccess: quiz collection is created -> " + userID);
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
                if (documentSnapshot != null && documentSnapshot.getString("name") != null &&
                        documentSnapshot.getString("email") != null) {

                } else {
                    return;
                }
            }
        });

        return currentGamesPlayed;

    }*/

}
