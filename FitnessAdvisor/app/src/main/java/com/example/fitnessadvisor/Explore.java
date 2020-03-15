package com.example.fitnessadvisor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class Explore extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "Explore";
    private ArrayList<Workout> workout_list;
    private Workout workout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        workout_list = new ArrayList<>();

        // Get current logged in user from Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser curr_user = mAuth.getCurrentUser();


        // Access Workout Info table of database
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("workout_info");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {


                            String weather = (String) postSnapshot.child("Weather").getValue();
                            String name = (String) postSnapshot.child("Name").getValue();
                            String gym = (String) postSnapshot.child("Gym").getValue();
                            String focusArea = (String) postSnapshot.child("FocusArea").getValue();
                            String instructions = (String) postSnapshot.child("Instructions").getValue();
                            long difficulty = (long) postSnapshot.child("Difficulty").getValue();
                            int diff = (int) difficulty;

                            Workout w = new Workout(weather, diff, gym, focusArea, instructions, name);

                            workout_list.add(w);
                        }

                        // To Do: Add recommendation algorithm here
                        // For now simply returns first 5 items in the
                        // workout list sorted in random order

                        Collections.shuffle(workout_list);
                        int len = workout_list.size();
                        for(int i=0; i<4; i++){
                            LinearLayout linearLayout = findViewById(R.id.recTasks);
                            Button b = new Button(Explore.this);
                            b.setText(workout_list.get(i).getName());
                            workout = workout_list.get(i);

                            b.setOnClickListener(new TaskClickListener(workout, Explore.this));
                            linearLayout.addView(b);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );

        Button all_tasks = findViewById(R.id.addBtn);
        all_tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "View all tasks button clicked");
                navigate(AllTasks.class);
            }
        });

        // OPTIONAL[Low Priority]
        // Implement an activity where users can view
        // their task history
        Button history = findViewById(R.id.histBtn);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "View task history button clicked");
                // TO DO: Implement Task History activity
            }
        });

        Button goals = findViewById(R.id.goalBtn);
        goals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "View goals button clicked");
                // TO DO: Implement Goals activity
            }
        });


        /*
         * OPIONAL[Low Priority]
         * Suggest workouts that have not been done by the user,
         * i.e if a user mainly focuses on shoulder workouts,
         * recommend user try (a yet to be completed by user) back workout
         */
        Button randomize = findViewById(R.id.randomBtn);
        randomize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Randomize workout suggestion button clicked");
                // To Do: see comments above

                // Delete all tasks currently displayed on recTasks
                LinearLayout linearLayout = findViewById(R.id.recTasks);
                linearLayout.removeAllViews();

                // Displays new random tasks on recTasks
                Collections.shuffle(workout_list);
                int len = workout_list.size();
                for(int i=0; i<4; i++) {
                    Button b = new Button(Explore.this);
                    b.setText(workout_list.get(i).getName());
                    workout = workout_list.get(i);

                    b.setOnClickListener(new TaskClickListener(workout, Explore.this));
                    linearLayout.addView(b);
                }
            }
        });
    }
    // Navigate to a new activity
    void navigate(Object o) {
        Intent intent = new Intent(Explore.this, (Class<?>) o);
        startActivity(intent);
    }
}
