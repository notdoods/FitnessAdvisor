package com.example.fitnessadvisor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRegisterDataActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register_data);

        Button finished = (Button) findViewById(R.id.finish);
        Log.d(TAG, "Entering RegisterActivity");

        finished.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "Finish button clicked");

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference();

                // Get user id
                String id;
                //String email;
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {
                    Log.d(TAG, "User exists");
                    id = user.getUid();
                    //email = user.getEmail();
                }
                else {
                    Log.d(TAG, "User does not exist");
                    return;
                }

                Log.d(TAG, "Got user id " + id);

                // Retrieve the elements from view
                EditText nameField = (EditText) findViewById(R.id.namefield);
                EditText ageField = (EditText) findViewById(R.id.agefield);

                CheckBox walkCheck = (CheckBox) findViewById(R.id.walking);
                CheckBox runCheck = (CheckBox) findViewById(R.id.running);
                CheckBox gymCheck = (CheckBox) findViewById(R.id.gym);

                // Collect User info
                String name = nameField.getText().toString().trim();
                int age = Integer.parseInt(ageField.getText().toString());

                Log.d(TAG, "Got name " + name + " and age " + age);

                // User Preferences
                boolean walking = walkCheck.isChecked();
                boolean running = runCheck.isChecked();
                boolean gym = gymCheck.isChecked();

                // TODO: implement "Add a new goal" feature
                //UserGoal[] goalList = new UserGoal[10];

                // Create the classes
                UserPreferences pref = new UserPreferences(walking, running, gym);
                User newUser = new User(name, id, age);

                Log.d(TAG, "New user + preferences created");

                // Push to Firebase
                //ref.child("users").child(id).setValue(newUser);
                ref.child("users").child(id).setValue(newUser);
                ref.child("users").child(id).child("preferences").setValue(pref);
                Log.d(TAG, "Pushing  " + id + " to db");

                // TODO: navigate to new activity (user homepage)
                navigate(UserHomePage.class);
                finish();
            }
        });
    }

    void navigate(Object o) {
        Intent intent = new Intent(UserRegisterDataActivity.this, (Class<?>) o);
        startActivity(intent);
    }
}
