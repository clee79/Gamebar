package com.example.a4322_term_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserProfile extends AppCompatActivity {
    final String TAG = "DB";
    int totalGames = 0, totalCorrect = 0;
    double percent;
    Intent intent;
    Button edit, signout, done;
    EditText name, email, phone, restaurantID, tableID;
    TextView games, category, percentage;
    ImageView back;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore fStore;
    DocumentReference documentReference;

    String userID;

    // Reference variable
    private static UserProfile instance = null;

    private static final String KEY_TABLE = "Table";
    private static final String Key_REST = "Restaurant";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        // Reference variable
        instance = this;

        // Database declarations
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        currentUser = mAuth.getCurrentUser();
        documentReference = fStore.collection("users").document(userID);

        // UserProfile declarations
        edit = findViewById(R.id.editButton);
        done = findViewById(R.id.doneButton);
        back = findViewById(R.id.backButtonDrawable);
        signout = findViewById(R.id.signoutButton);
        games = findViewById(R.id.gamesPlayedTV);
        category = findViewById(R.id.categoryTV);
        percentage = findViewById(R.id.percentageTV);

        name = findViewById(R.id.nameEditText);
        email = findViewById(R.id.emailEditText);
        phone = findViewById(R.id.phoneEditText);

        // TODO -> QUESTION TO CHRIS: SAVE THIS IN DB? OR JUST CHANGE WHEN QR RECEIVES THIS INFO?
        restaurantID = findViewById(R.id.restaurantID);
        tableID = findViewById(R.id.tableID);

        // QR INFO
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("myQRPreference", Context.MODE_PRIVATE);
        restaurantID.setText(sharedPreferences.getString("Restaurant","??"));
        tableID.setText(sharedPreferences.getString("Table","??"));
        // Clear the passed info afterwards.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Loads all banner stats
        getAllBannerInformation();
        loadUserInformation();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.setVisibility(View.GONE);
                done.setVisibility(View.VISIBLE);

                // DONE BUTTON TO SAVE CHANGES
                name.setEnabled(true);
                email.setEnabled(true);
                phone.setEnabled(true);

                // Focus on email when edit button is clicked
                email.requestFocus();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();

                name.setEnabled(false);
                email.setEnabled(false);
                phone.setEnabled(false);

                done.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
            }
        });

        // Sign out button
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilderSignOut();
            }
        });

        // Brings user back to Home activity for now, can be change
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Hub.class);
                startActivity(intent);
                finish();
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }
    @Override
    protected void onStart () {
        super.onStart();

        // If user is already logged in, go straight to UserProfile activity
        if (currentUser== null) {
            intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }
    }


    // Reference getter
    public static UserProfile getUserProfileInstance() {
        if (instance != null) {
            return instance;
        }
        return null;
    }

    public void dialogBuilderSignOut() {
        // Create alert dialog to prompt user if they are sure they want to
        // sign out
        AlertDialog.Builder signoutWarning = new AlertDialog.Builder(UserProfile.this);
        signoutWarning.setMessage("Are you sure you want to sign out?");
        signoutWarning.setCancelable(true);
        Log.i("USER ID", "loadUserInformation: " + userID);

        signoutWarning.setPositiveButton(
                "Sign out",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // SignoutUser function passed here
                        signoutUser();
                    }
                });

        signoutWarning.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = signoutWarning.create();
        alert.show();
    }

    public void signoutUser() {
        // Sign user out
        FirebaseAuth.getInstance().signOut();

        // Go to home page
        intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
        finish();
    }

    private void loadUserInformation() {

            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot != null && documentSnapshot.getString("name") != null &&
                            documentSnapshot.getString("email") != null) {
                        name.setText(documentSnapshot.getString("name"));
                        email.setText(documentSnapshot.getString("email"));
                        phone.setText(documentSnapshot.getString("phone"));
                    } else {
                        return;
                    }
                }
            });


    }

    // Will currently only update 1 field at a time
    // Ex: You click Edit and you change the email and name fields
    // Only 1 field will update
    private void saveUserInformation() {

        String nameStr =  name.getText().toString();
        String emailStr =  email.getText().toString();
        String phoneStr =  phone.getText().toString();

        if (nameStr.isEmpty()) {
            Toast.makeText(this, "Name is required.", Toast.LENGTH_SHORT).show();
            name.requestFocus();
            return;
        }

        if (emailStr.isEmpty()) {
            Toast.makeText(this, "Email is required.", Toast.LENGTH_SHORT).show();
            email.requestFocus();
            return;
        }


        if (currentUser != null) {
            // Update email
            currentUser.updateEmail(emailStr)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserProfile.this, "Profile updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UserProfile.this, "Oops! Something went wrong.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            // Update collection db
            documentReference.update("name", nameStr);
            documentReference.update("email", emailStr);
            documentReference.update("phone", phoneStr);
        }
    }

    private void getAllBannerInformation() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference quizCollectionReference = db.collection("quiz");

        Query query = quizCollectionReference
                .whereEqualTo("userID", userID);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                // Get total Games
                if (task.isSuccessful()) {

                    // for fav category
                    ArrayList<String> categoryList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // track for percentage correct

                        Stat game = document.toObject(Stat.class);

                        // for percent
                        totalCorrect += Integer.parseInt(game.getScore());

                        // track for fav category
                        categoryList.add(game.getTopic());

                        // track for percentage
                        totalGames++;
                    }

                    // Set how many games text
                    games.setText(Integer.toString(totalGames));

                    // Set favorite category
                    if (categoryList.isEmpty()) {
                        category.setText("No favorites yet.");
                    } else {
                        getMostCommonCategory(categoryList);
                        category.setText(getMostCommonCategory(categoryList));
                    }

                    // Set percentage correct
                    if (totalGames > 0) {
                        percent = totalCorrect * 1.0;
                        percent = percent / (totalGames * 10.0);
                        percent *= 100.0;
                        percent = formatDecimal(percent);
                        percentage.setText(percent + "%");
                    } else {
                        percentage.setText("No data");
                    }

                } else {
                    Log.i(TAG, "getStats: oops error");
                }
            }
        });
    }
    public String getMostCommonCategory(ArrayList<String> arrayList) {
        Map<String, Integer> stringsCount = new HashMap<>();

        for(String s: arrayList) {

            Integer c = stringsCount.get(s);

            if(c == null) c = new Integer(0);
            c++;
            stringsCount.put(s,c);
        }

        Map.Entry<String,Integer> mostRepeated = null;

        for(Map.Entry<String, Integer> e: stringsCount.entrySet()) {
            if(mostRepeated == null || mostRepeated.getValue()<e.getValue())
                mostRepeated = e;
        }

        return mostRepeated.getKey();
    }

    public double formatDecimal (double d) {
        double formatted;
        NumberFormat formatter = new DecimalFormat("#0.00");
        return formatted = Double.parseDouble(formatter.format(d));
    }

}
