package com.example.a4322_term_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.lang.reflect.Array;
import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {
    final String TAG = "DB";
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseUser user;
    String userID;

    ListView listView;
    ArrayList<String> stringArrayList = new ArrayList<>();
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

        // Firebase
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userID = user.getUid();

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

        CollectionReference quizCollectionReference = firestore.collection("quiz");

        Query query = quizCollectionReference
                .whereEqualTo("userID", userID);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ArrayList<String> array = new ArrayList<>();

                        Stat game = document.toObject(Stat.class);

                        Log.i(TAG, "TEST OBJECT: userID -> " + game.getUserID());
                        Log.i(TAG, "TEST OBJECT: topic -> " + game.getTopic());
                        Log.i(TAG, "TEST OBJECT: score -> " + game.getScore());
                        Log.i(TAG, "TEST OBJECT: date -> " + game.getDate());

                        // concat strings for better looking stats output
                        String stringConcat = buildString(game.getDate(), game.getTopic(), "Score: " + game.getScore());

                        stringArrayList.add(stringConcat);

                        ArrayAdapter adapter = new ArrayAdapter<>(StatisticsActivity.this, android.R.layout.simple_list_item_1, stringArrayList);
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

    // Used to format stats output
    private String buildString(String one, String two, String three) {
        return "\n" + one + "\n" + two + "\n" + three + "\n";
    }
}