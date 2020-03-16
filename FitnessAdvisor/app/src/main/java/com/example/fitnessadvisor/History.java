package com.example.fitnessadvisor;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

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
import java.util.Map;

public class History extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ArrayList<Workout> history_list;
    private Workout workout;

    static public Map<String, Integer> tracker_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        history_list = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String id = user.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + id + "/history/");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String focus_area = workout.getFocusArea();

                            // add workout to history list
                            workout = postSnapshot.getValue(Workout.class);
                            history_list.add(workout);

                            // Update count of completed task type. If a new type
                            // of task is completed, sets value = 1. Else, updates
                            // value of key by 1.
                            Integer val = tracker_map.get(focus_area);
                            if(val == null) {
                                tracker_map.put(focus_area,1);
                            } else {
                                int old_value = tracker_map.get(focus_area);
                                int new_value = old_value + 1;
                                tracker_map.put(focus_area,new_value);
                            }
                        }

                        // initialize/update personal_model

                        LinearLayout linearLayout = findViewById(R.id.historyLayout);
                        for (Workout w : history_list) {
                            Button b = new Button(History.this);
                            b.setText(w.getName());
                            workout = w;

                            b.setOnClickListener(new TaskClickListener(workout, History.this));
                            linearLayout.addView(b);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }

    static public Map<String, Integer> getTracker() {
        return tracker_map;
    }
}
