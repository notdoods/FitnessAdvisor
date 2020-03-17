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
                            // add workout to history list
                            workout = postSnapshot.getValue(Workout.class);
                            history_list.add(workout);

                        }

                        // initialize/update personal_model
                        DatabaseReference history_ref = FirebaseDatabase.getInstance().getReference("users/" + id);


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

}