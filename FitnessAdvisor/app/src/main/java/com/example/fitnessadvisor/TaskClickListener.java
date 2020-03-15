package com.example.fitnessadvisor;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class TaskClickListener implements View.OnClickListener {
    private Workout workout;
    private Context context;
    private boolean added;
    private String database_id;

    public TaskClickListener(Workout w, Context c) {
        workout = w;
        context = c;
        added = false;
        database_id = "";
    }

    public TaskClickListener(Workout w, Context c, boolean added, String database_id) {
        this.context = c;
        this.workout = w;
        this.added = added;
        this.database_id = database_id;
    }

    @Override
    public void onClick(View v) {
        // Navigate to task page, send workout information
        Intent intent = new Intent(context, CreateTask.class);
        intent.putExtra("Weather", workout.getWeather());
        intent.putExtra("Gym", workout.getGym());
        intent.putExtra("Title", workout.getName());
        intent.putExtra("FocusArea", workout.getFocusArea());
        intent.putExtra("Difficulty", workout.getDifficulty());
        intent.putExtra("Instruction", workout.getInstruction());
        intent.putExtra("Added", added);
        intent.putExtra("ID", database_id);
        context.startActivity(intent);
    }
}
