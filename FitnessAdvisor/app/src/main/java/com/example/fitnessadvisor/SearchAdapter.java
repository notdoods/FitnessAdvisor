package com.example.fitnessadvisor;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>{
    private Context context;
    private ArrayList<String> namesList;
    private ArrayList<String> focusList;
    private ArrayList<String> instructionsList;
    private ArrayList<String> weatherList;
    private ArrayList<String> gymList;
    private ArrayList<Long> difficultyList;
    private static final String TAG = "SearchAdapter";

    public SearchAdapter(searchExercise searchExercise, ArrayList<String> namesList, ArrayList<String> focusList, ArrayList<Long> difficultyList,
                         ArrayList<String> instructionsList, ArrayList<String> weatherList, ArrayList<String> gymList) {
        this.namesList = namesList;
        this.focusList = focusList;
        this.difficultyList = difficultyList;
        this.instructionsList = instructionsList;
        this.weatherList = weatherList;
        this.gymList = gymList;
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView Name, FocusArea, Difficulty;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.Name);
            FocusArea = itemView.findViewById(R.id.FocusArea);
            Difficulty = itemView.findViewById(R.id.Difficulty);
        }
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_layout, parent, false);
        context = parent.getContext();
        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, final int position) {
        /*
        Presents the name, focus area, and difficulty for each item on the custom recyclerview, then
        allows for button click to lead to Create Task.
         */
        holder.Name.setText(namesList.get(position));
        holder.FocusArea.setText(focusList.get(position));
        holder.Difficulty.setText(String.valueOf(difficultyList.get(position)));

        int intDifficulty = difficultyList.get(position).intValue();
        Workout w = new Workout(weatherList.get(position), intDifficulty, gymList.get(position), focusList.get(position), instructionsList.get(position), namesList.get(position));

        holder.Name.setOnClickListener(new TaskClickListener(w, context));
    }


    @Override
    public int getItemCount() {
        return namesList.size();
    }
}
