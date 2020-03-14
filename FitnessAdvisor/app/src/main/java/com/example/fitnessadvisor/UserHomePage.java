package com.example.fitnessadvisor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UserHomePage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "UserHomePage";
    private TextView user_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);

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
            }
        });

        // Click listener for "add new task"
        Button fab = findViewById(R.id.add_new_task);
        fab.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "Add new task button clicked");
                // TODO: navigate to create a new todo item (not yet implemented)

                navigate(AllTasks.class);
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
    }

    // Navigate to a new activity
    void navigate(Object o) {
        Intent intent = new Intent(UserHomePage.this, (Class<?>) o);
        startActivity(intent);
    }
}
