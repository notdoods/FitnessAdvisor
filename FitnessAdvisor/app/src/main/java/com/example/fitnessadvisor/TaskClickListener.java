package com.example.fitnessadvisor;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class TaskClickListener implements View.OnClickListener {
    private Workout workout;
    private Context context;

    public TaskClickListener(Workout w, Context c) {
        workout = w;
        context = c;
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
        context.startActivity(intent);
    }
}
