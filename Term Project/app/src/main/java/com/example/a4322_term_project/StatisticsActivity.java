package com.example.a4322_term_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {
    final String TAG = "DB";
    ListView listView;
    ArrayList<String> array = new ArrayList<>();
    TextView statsTV;
    ImageView back;
    int totalGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        listView = findViewById(R.id.listView);
        statsTV = findViewById(R.id.tvUserInfo);
        back = findViewById(R.id.backButtonDrawable);

        setWelcome();
        getStats();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Hub.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getStats() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference quizCollectionReference = db.collection("quiz");


        Query query = quizCollectionReference
                .whereEqualTo("userID",
                        FirebaseAuth.getInstance().getCurrentUser().getUid());

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Stat newStat = new Stat();

                        newStat.setUserID(document.getData().get("userID").toString());
                        newStat.setTopic(document.getData().get("topic").toString());
                        newStat.setScore(document.getData().get("correct").toString());
                        newStat.setDate(document.getData().get("data").toString());

                        Log.i(TAG, "TEST OBJECT: userID -> " + newStat.getUserID());
                        Log.i(TAG, "TEST OBJECT: topic -> " + newStat.getTopic());
                        Log.i(TAG, "TEST OBJECT: score -> " + newStat.getScore());
                        Log.i(TAG, "TEST OBJECT: date -> " + newStat.getDate());


                        //array.add(newStat);
                        array.add("DATE: " + newStat.getDate());
                        array.add("TOPIC: " + newStat.getTopic());
                        array.add("SCORE: " + newStat.getScore());

                        ArrayAdapter adapter = new ArrayAdapter<String>(StatisticsActivity.this, android.R.layout.simple_list_item_1, array);
                        listView.setAdapter(adapter);
                        Log.i(TAG, "getStats: array: " + array);

                        totalGames++;
                        Log.i(TAG, "GAMES PLAYED: " + totalGames);
                    }

                } else {
                    Log.i(TAG, "getStats: oops error -> dumbass lmao ");

                }
            }
        });
    }
    private void setWelcome () {
        statsTV.setText("Stats");
    }
}