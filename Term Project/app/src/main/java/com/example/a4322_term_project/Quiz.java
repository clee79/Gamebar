package com.example.a4322_term_project;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Quiz extends AppCompatActivity {
    // TODO -> Will need to update this based on who is player
    int playerNumber = 1;
    int score = 0;
    int qid = 0;
    int qcount = 0;

    String url;
    TextView questionTextView, questionNumber, player;
    Button optionA, optionB, optionC, optionD;
    QuestionFormat currentQ, currentAnswerQ;
    ArrayList<QuestionFormat> question = new ArrayList<>();
    ArrayList<QuestionFormat> currentQuestion = new ArrayList<>();

    String key;
    int category;
    boolean questionType = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);


        questionTextView = findViewById(R.id.question);
        questionNumber = findViewById(R.id.questionNumberTextView);
        player = findViewById(R.id.playerTextView);

        optionA = findViewById(R.id.buttonA);
        optionB = findViewById(R.id.buttonB);
        optionC = findViewById(R.id.buttonC);
        optionD = findViewById(R.id.buttonD);

        Bundle bundle = getIntent().getExtras();
        category = bundle.getInt("topic");
        Log.i("TAG", "CATEGORY -> " + Integer.toString(category));


        LoadQ();

        optionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    handleButtonClick(1);
                    optionA.setAlpha(1);
                }
                catch (Exception err) {
                    networkError();
                }
            }
        });

        optionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    handleButtonClick(2);
                    optionB.setAlpha(1);
                }
                catch (Exception err) {
                    networkError();
                }
            }
        });

        optionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    handleButtonClick(3);
                    optionC.setAlpha(1);
                }
                catch (Exception err) {
                    networkError();
                }
            }

        });

        optionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    handleButtonClick(4);
                    optionD.setAlpha(1);
                }
                catch (Exception err)
                {
                    networkError();
                }
            }
        });
    }

    // when user presses on one of the answers
    void handleButtonClick(int selectedAnswer) {

        optionA.setEnabled(false);
        optionB.setEnabled(false);
        optionC.setEnabled(false);
        optionD.setEnabled(false);



        if(optionA.getText().toString().equals(currentQuestion.get(qid-1).getANSWER())) {
            optionA.setBackgroundResource(R.drawable.button_correct);
        }

        if(optionB.getText().toString().equals(currentQuestion.get(qid-1).getANSWER())) {
            optionB.setBackgroundResource(R.drawable.button_correct);
        }

        if(optionC.getText().toString().equals(currentQuestion.get(qid-1).getANSWER())) {
            optionC.setBackgroundResource(R.drawable.button_correct);
        }

        if(optionD.getText().toString().equals(currentQuestion.get(qid-1).getANSWER())) {
            optionD.setBackgroundResource(R.drawable.button_correct);
        }


        if (selectedAnswer == 1) {
            if (optionA.getText().toString().equals(currentQuestion.get(qid-1).getANSWER()))
                score++;
            else
                optionA.setBackgroundResource(R.drawable.button_wrong);
        }

        if (selectedAnswer == 2) {
            if (optionB.getText().toString().equals(currentQuestion.get(qid-1).getANSWER()))
                score++;
            else
                optionB.setBackgroundResource(R.drawable.button_wrong);
        }
        if (selectedAnswer == 3) {
            if (optionC.getText().toString().equals(currentQuestion.get(qid-1).getANSWER()))
                score++;
            else
                optionC.setBackgroundResource(R.drawable.button_wrong);
        }

        if (selectedAnswer == 4) {
            if (optionD.getText().toString().equals(currentQuestion.get(qid-1).getANSWER()))
                score++;
            else
                optionD.setBackgroundResource(R.drawable.button_wrong);
        }

        // TODO -> Make it look nicer?
        // TODO -> Multiple instances

        // if at the end, after the last question pass it off to
        // summary screen to show the stats
        if (qcount == 9) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent i = new Intent(getApplicationContext(), Summary.class);

                    qcount = 0;

                    i.putExtra("Score", score);
                    i.putExtra("Topic", category);
                    i.putExtra("Key", key);
                    startActivity(i);
                    finish();

                }
            }, 1000);


        }
        // if there are more questions, keep displaying them
        else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setQuestionView();
                }
            }, 1000);


        }


    }

    // function that, surprisingly, parses json
    void parseJson(String out) {


        try {

            JSONObject resJson = new JSONObject(out);

            String responseCode = resJson.getString("response_code");
            int resCode = Integer.parseInt(responseCode);

            if (resCode == 0) {
                String result=	resJson.getString("results");
                JSONArray resArray = new JSONArray(result);

                for (int i = 0; i < resArray.length(); i++) {

                    JSONObject qjson = resArray.getJSONObject(i);
                    String q = qjson.getString("question");
                    String cans = qjson.getString("correct_answer");
                    String ians = qjson.getString("incorrect_answers");
                    JSONArray ans = new JSONArray(ians);
                    String ians1 = ans.getString(0);
                    String ians2;
                    String ians3;
                    if(cans.equalsIgnoreCase("True") || cans.equals("False")) {
                        ians2 = "";
                        ians3="";
                    }

                    else {
                        ians2 = ans.getString(1);
                        ians3 = ans.getString(2);
                    }

                    Random rand = new Random();

                    int  n = rand.nextInt(4) + 1;

                    QuestionFormat que=null;

                    switch(n) {
                        case 1 : que = new QuestionFormat(q,ians1,ians2,ians3,cans);
                            break;
                        case 2 :  que = new QuestionFormat(q,ians1,ians2,cans,ians3);
                            break;
                        case 3 :  que = new QuestionFormat(q,ians1,cans,ians3,ians2);
                            break;
                        case 4 :  que = new QuestionFormat(q,cans,ians2,ians3,ians1);
                            break;

                    }
                    QuestionFormat cq = new QuestionFormat(q,cans);

                    question.add(que);
                    currentQuestion.add(cq);

                }

                setQuestionView();

            }

            else if(resCode==1) {

                ((Key)getApplication()).setToken(null);
                LoadQ();

            }
            else if(resCode==3) {

                ((Key)getApplication()).setToken(null);
                LoadQ();

            }
            else if(resCode==4) {

                ((Key)getApplication()).setToken(null);
                LoadQ();
            }

            else {

                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Bundle bundle = getIntent().getExtras();
        category = bundle.getInt("topic");

        LoadQ();


    }

    // Get the question
    void LoadQ() {
        String tempURL;

        score = 0;
        Log.i("TAG", "LoadQ: CATCHING VALUE");

        tempURL = "amount=10" + "&category=" + category;

        url = "https://opentdb.com/api.php?" + tempURL;


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject json = new JSONObject(response);

                            if(response != null) {
                                // if successful, parse the json into questions
                                parseJson(response);
                                Log.i("TAG", "onResponse: " + response);
                            }

                            else {
                                // else the response failed, so try to reconnect. ask user to do so
                                networkError();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(getApplicationContext(),"Loading... Please wait...",Toast.LENGTH_LONG).show();

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Loading... Please wait...",Toast.LENGTH_LONG).show();
            }
        });
// Add the request to the RequestQueue.
        requestQueue.add(stringRequest);

    }


    private void setQuestionView() {

        currentQ = question.get(qid);
        currentAnswerQ = currentQuestion.get(qid);

        questionTextView.setText(Html.fromHtml(currentQ.getQUESTION()));
        optionA.setText(Html.fromHtml(currentQ.getOPTA()));
        optionB.setText(Html.fromHtml(currentQ.getOPTB()));
        optionC.setText(Html.fromHtml(currentQ.getOPTC()));
        optionD.setText(Html.fromHtml(currentQ.getANSWER()));

        if(optionA.getText().toString().equals("")) {
            optionA.setVisibility(View.GONE);
            questionType = false;
        } else {
            optionA.setVisibility(View.VISIBLE);
        }

        if(optionB.getText().toString().equals("")) {
            optionB.setVisibility(View.GONE);
            questionType = false;
        } else {
            optionB.setVisibility(View.VISIBLE);
        }
        if (optionC.getText().toString().equals("")) {
            optionB.setVisibility(View.GONE);
            questionType = false;
        } else {
            optionB.setVisibility(View.VISIBLE);
        }

        if (optionD.getText().toString().equals("")) {
            optionD.setVisibility(View.GONE);
            questionType = false;
        } else {
            optionD.setVisibility(View.VISIBLE);
        }

        // Reset the background color to its original blue
        // or else other colors will remain (greens and reds)
        optionA.setBackgroundResource(R.drawable.buttonstyle);
        optionB.setBackgroundResource(R.drawable.buttonstyle);
        optionC.setBackgroundResource(R.drawable.buttonstyle);
        optionD.setBackgroundResource(R.drawable.buttonstyle);

        optionA.setEnabled(true);
        optionB.setEnabled(true);
        optionC.setEnabled(true);
        optionD.setEnabled(true);
        // Set question number view
        questionNumber.setText("Question " + (qcount + 1) + "/10");
        player.setText("Player #" + playerNumber);
        qcount++;
        qid++;
    }


    // If not connected to internet, user can't fetch questions
    private void networkError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Quiz.this);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        // try to reload questions
                        LoadQ();
                        break;

                        // do nothing
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };


        builder.setMessage("Network error. Try again?").setPositiveButton("Yes",dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }
}
