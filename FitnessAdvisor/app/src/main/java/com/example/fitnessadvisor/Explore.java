package com.example.fitnessadvisor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
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
import java.util.Comparator;


public class Explore extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "Explore";
    private ArrayList<Workout> potential_workout_list;
    private ArrayList<Pair<String,Integer>> order_helper;
    private Workout workout;
    private boolean gym_goer, running, walking;
    private int total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        potential_workout_list = new ArrayList<>();
        order_helper = new ArrayList<>();
        gym_goer = false;
        running = false;
        walking = false;

        // Get current logged in user from Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser curr_user = mAuth.getCurrentUser();
        String id = curr_user.getUid();

        // store preferences
        DatabaseReference pref = FirebaseDatabase.getInstance().getReference("users/"+id+"/preferences/");
        pref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d(TAG, "Attempting to store preferences");
                        UserPreferences user_pref = dataSnapshot.getValue(UserPreferences.class);
                        if (user_pref.isGym()) {
                            gym_goer = true;
                        }
                        if (user_pref.isRunning()) {
                            running = true;
                        }
                        if (user_pref.isWalking())
                        {
                            walking = true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );

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

                            /*
                             * Filter Recommended Workouts by Preferences
                             */

                            // User goes to gym and workout is gym workout
                            if ( gym_goer == true && (gym.equalsIgnoreCase("yes")) )  {
                                // If user does not walk/run but workout focuses on cardio/sport,
                                // skip it
                                if ( (walking == false && running == false) && (focusArea.equalsIgnoreCase("cardio") ||
                                        focusArea.equalsIgnoreCase("sport"))) {
                                        continue;
                                } else {
                                    Workout w = new Workout(weather, diff, gym, focusArea, instructions, name);
                                    potential_workout_list.add(w);
                                }
                            }
                            // User does not go to gym and workout is not gym workout
                            else if (gym_goer == false && gym.equalsIgnoreCase("no")) {
                                Workout w = new Workout(weather, diff, gym, focusArea, instructions, name);
                                potential_workout_list.add(w);
                            }
                            // User prefers to walk or run and workout is Cardio/Sport
                            else if ( (walking == true || running == true) && (focusArea.equalsIgnoreCase("cardio") ||
                                    focusArea.equalsIgnoreCase("sport")) ) {
                                Workout w = new Workout(weather, diff, gym, focusArea, instructions, name);
                                potential_workout_list.add(w);
                            } else {
                                continue;
                            }
                        }
                        //createButtons(potential_workout_list,6);
                        DatabaseReference completed_ref = FirebaseDatabase.getInstance().getReference("users/"+id+"/completed/");
                        completed_ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String focus = snapshot.getKey();
                                    Integer num_comp = snapshot.getValue(Integer.class);

                                    if (num_comp != null) {
                                        Pair<String, Integer> p = new Pair<>(focus, num_comp);
                                        order_helper.add(p);
                                    }
                                }
                                // sort order_helper in descending order (greatest->least)
                                Collections.sort(order_helper, new Comparator<Pair<String, Integer>>() {
                                    @Override
                                    public int compare(Pair<String, Integer> o1, Pair<String, Integer> o2) {
                                        return o2.second - o1.second;
                                    }
                                });

                                // no completed tasks, recommend 6 random tasks
                                if (order_helper.size() <= 0 ) {
                                    Log.d(TAG, "order_helper is size 0");
                                    Collections.shuffle(potential_workout_list);
                                    createButtons(potential_workout_list,6);
                                }
                                // one completed task type, recommend 4 tasks with
                                // same type, 2 random tasks
                                else if (order_helper.size() == 1) {
                                    Log.d(TAG, "order_helper is size 1");
                                    Collections.shuffle(potential_workout_list);

                                    ArrayList<Workout> primary = new ArrayList<>();
                                    for (Workout w : potential_workout_list) {
                                        if (w.getFocusArea().equalsIgnoreCase(order_helper.get(0).first)) {
                                            primary.add(w);
                                        }
                                    }

                                    if (primary.size() >= 4) {
                                        createButtons(primary, 4);
                                        createButtons(potential_workout_list, 2);
                                    } else {
                                        createButtons(primary, primary.size());
                                        createButtons(potential_workout_list,6-primary.size());
                                    }
                                }
                                // multiple task types recommend 3 tasks with same type as
                                // most completed type, 2 tasks with second most completed, and
                                // 1 random task
                                else {
                                    Collections.shuffle(potential_workout_list);
                                    createButtons(potential_workout_list, 6);

                                    //------------------------------ERROR SOMEWHERE HERE---------------------------//
                                    /*
                                    Log.d(TAG, "order_helper is size 1");
                                    ArrayList<Workout> primary = new ArrayList<>();
                                    ArrayList<Workout> second = new ArrayList<>();
                                    for (Workout w : potential_workout_list) {
                                        if (w.getFocusArea().equalsIgnoreCase(order_helper.get(0).first)) {
                                            primary.add(w);
                                        }
                                        if (w.getFocusArea().equalsIgnoreCase(order_helper.get(1).first)) {
                                            second.add(w);
                                        }

                                        createButtons(primary, 3);
                                        createButtons(second, 2);
                                        createButtons(potential_workout_list, 1);
                                    }
                                    */
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
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
                navigate(History.class);
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
                Collections.shuffle(potential_workout_list);
                int len = potential_workout_list.size();
                for(int i=0; i<4; i++) {
                    Button b = new Button(Explore.this);
                    b.setText(potential_workout_list.get(i).getName());
                    workout = potential_workout_list.get(i);

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

    // Creates a button in recTasks linearLayout
    void createButtons(ArrayList<Workout> workout_list, int num_buttons) {
        if (workout_list.size() == 0) {
            return;
        }
        for (int i=0; i<num_buttons; i++) {
            LinearLayout linearLayout = findViewById(R.id.recTasks);
            Button b = new Button(Explore.this);
            b.setText(workout_list.get(i).getName());
            workout = workout_list.get(i);

            b.setOnClickListener(new TaskClickListener(workout, Explore.this));
            linearLayout.addView(b);
        }
    }

}
