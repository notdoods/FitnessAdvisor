package com.example.fitnessadvisor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class CreateTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);


        TextView task_title = findViewById(R.id.task_title);
        TextView difficulty = findViewById(R.id.difficulty);
        TextView focus_area = findViewById(R.id.focus_area);
        TextView instructions = findViewById(R.id.instructions);

        // TODO: Get Task info

    }
}
