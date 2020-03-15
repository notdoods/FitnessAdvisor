package com.example.fitnessadvisor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UserHomePage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "UserHomePage";
    private TextView user_name;
    private Map<String, Workout> task_list;
    LinearLayout tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);

        task_list = new HashMap<String, Workout>();

        // Get Firebase currently logged in user
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser curr_user = mAuth.getCurrentUser();

        // Grab fields from view
        user_name = findViewById(R.id.user_full_name);
        TextView date = findViewById(R.id.date);

        // Date formatting
        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMMM d", Locale.US);
        String date_str = formatter.format(today);
        date.setText(date_str);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users/"+ curr_user.getUid());
        User u_info = new User("err", "err", 0);

        // Get the user from the database using user ID
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User u_info = dataSnapshot.getValue(User.class);
                Log.d(TAG, "Retrieved user " + u_info.getId() + " named " + u_info.getName() );
                user_name.setText(u_info.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "The read failed: " + databaseError.getCode());
            }
        });

        // Click listener for "Explore"
        Button explore_button = findViewById(R.id.explore);
        explore_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "Explore button clicked");
                // TODO: navigate to recommended activity (not yet implemented)
                navigate(Explore.class);
            }
        });

        // Click listener for "Search exercises"
        Button search = findViewById(R.id.search_button);
        search.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "Search button clicked");
                navigate(searchExercise.class);
            }
            });
        Button buttonPlaces = findViewById(R.id.places);
        buttonPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(), findPlaces.class);
                startActivity(intent);
            }
        });

        // Display task list
        tasks = findViewById(R.id.task_layout);
        ref.child("tasks").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        task_list = new HashMap<>();
                        tasks.removeAllViews();
                        // Get all info from database, generate Workout Objects to hold data
                        for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            Workout w = postSnapshot.getValue(Workout.class);
                            task_list.put(postSnapshot.getKey(), w);
                            Log.d(TAG, postSnapshot.getValue(Workout.class).toString());
                        }

                        for(Map.Entry<String, Workout> mapElement : task_list.entrySet()){
                            Button b = new Button(UserHomePage.this);
                            b.setText(mapElement.getValue().getName());
                            b.setBackgroundColor(Color.WHITE);

                            b.setOnClickListener(new TaskClickListener(mapElement.getValue(), UserHomePage.this, true, mapElement.getKey()));
                            tasks.addView(b);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                }
        );
    }

    // Navigate to a new activity
    void navigate(Object o) {
        Intent intent = new Intent(UserHomePage.this, (Class<?>) o);
        startActivity(intent);
    }
}
