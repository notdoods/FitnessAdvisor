package com.example.fitnessadvisor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class AllTasks extends AppCompatActivity {
    private ArrayList<Workout> workout_list;
    private static final String TAG = "AllTasks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);

        workout_list = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("workout_info");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            //System.out.println("AllTasks " + postSnapshot.child("Weather").getValue());
                            String weather = (String) postSnapshot.child("Weather").getValue();
                            String name = (String) postSnapshot.child("Name").getValue();
                            String gym = (String) postSnapshot.child("Gym").getValue();
                            String focusArea = (String) postSnapshot.child("FocusArea").getValue();
                            String instructions = (String) postSnapshot.child("Instructions").getValue();
                            long difficulty =  (long) postSnapshot.child("Difficulty").getValue();
                            int diff = (int) difficulty;

                            Workout w = new Workout(weather, diff, gym, focusArea, instructions, name);

                            //Log.d(TAG, "Retrieve workout " + w);
                            workout_list.add(w);
                        }

                        for(Workout w: workout_list){
                            //Button b = new Button();
                            LinearLayout linearLayout = findViewById(R.id.task_buttons);
                            Button b = new Button(AllTasks.this);
                            b.setText(w.getName());

                            // TODO: add click listener to navigate to Create Task activity

                            linearLayout.addView(b);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                }
        );
    }
}
