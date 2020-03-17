package com.example.fitnessadvisor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

// INCOMPLETE
public class CreateTask extends AppCompatActivity {
    private String title, focus, instruct, diff_str, weather, gym;
    private FirebaseAuth mAuth;
    private Workout w;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        TextView task_title = findViewById(R.id.task_title);
        TextView difficulty = findViewById(R.id.difficulty);
        TextView focus_area = findViewById(R.id.focus_area);
        TextView instructions = findViewById(R.id.instructions);

        Intent intent = this.getIntent();
        title = intent.getStringExtra("Title");
        focus = "Focus Area: " + intent.getStringExtra("FocusArea");
        instruct = "Instructions:\n" + intent.getStringExtra("Instruction");
        int diff = intent.getIntExtra("Difficulty", 0);
        diff_str = "Difficulty: " + Integer.toString(diff);

        weather = intent.getStringExtra("Weather");
        gym = intent.getStringExtra("Gym");

        w = new Workout(weather, diff, gym, intent.getStringExtra("FocusArea"), intent.getStringExtra("Instruction"), intent.getStringExtra("Title"));

        task_title.setText(title);
        difficulty.setText(diff_str);
        focus_area.setText(focus);
        instructions.setText(instruct);

        if (intent.getBooleanExtra("Added", false)) {
            Button b = new Button(this);
            b.setText("Mark as completed");
            ViewGroup.LayoutParams buttonparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            b.setLayoutParams(buttonparams);

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    mAuth = FirebaseAuth.getInstance();
                    FirebaseUser user = mAuth.getCurrentUser();
                    String id = user.getUid();
                    String task_id = intent.getStringExtra("ID");
                    DatabaseReference ref = db.getReference("users/" + id + "/tasks/" + task_id);

                    // Add task to history
                    DatabaseReference ref2 = db.getReference("users/" + id );

                    //update completed
                    ref2.child("completed").child(w.getFocusArea()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Integer completed_num = dataSnapshot.getValue(Integer.class);
                            int update_num;
                            if (completed_num == null) {
                                update_num = 1;
                            } else {
                                update_num = completed_num+1;
                            }
                            ref2.child("completed").child(w.getFocusArea()).setValue(update_num);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    // add workout to history
                    ref2.child("history").push().setValue(w);

                    ref.removeValue();
                    finish();

                }
            });

            LinearLayout buttonLayout = findViewById(R.id.create_task_buttons);
            ViewGroup.LayoutParams params = buttonLayout.getLayoutParams();
            params.width = params.width*2 +10;
            buttonLayout.setLayoutParams(params);
            buttonLayout.addView(b);
        }

        Button add = findViewById(R.id.add_to_task_list);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                String id = user.getUid();

                DatabaseReference ref = database.getReference("users/" + id);

                ref.child("tasks").push().setValue(w);
                Toast.makeText(CreateTask.this, title + " added to task list.",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }

    public void onStop() {
        super.onStop();
        finish();
    }
}
