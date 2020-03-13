package com.example.fitnessadvisor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

// INCOMPLETE
public class CreateTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        TextView task_title = findViewById(R.id.task_title);
        TextView difficulty = findViewById(R.id.difficulty);
        TextView focus_area = findViewById(R.id.focus_area);
        TextView instructions = findViewById(R.id.instructions);

        Intent intent = this.getIntent();
        String title = intent.getStringExtra("Title");
        String focus = "Focus Area: " + intent.getStringExtra("FocusArea");
        String instruct = "Instructions:\n" + intent.getStringExtra("Instruction");
        int diff = intent.getIntExtra("Difficulty", 0);
        String diff_str = "Difficulty: " + Integer.toString(diff);

        task_title.setText(title);
        difficulty.setText(diff_str);
        focus_area.setText(focus);
        instructions.setText(instruct);

        //TODO: Implement click listener to add task to task list
    }

    public void onStop() {
        super.onStop();
        finish();
    }
}
