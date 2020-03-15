package com.example.fitnessadvisor;

import androidx.annotation.NonNull;

/** Workout class to hold info from database
 *
 */
public class Workout {
    private String name, focusArea, instruction, weather, gym;
    private int difficulty;

    public Workout(){
        this.name = "";
        this.difficulty = 0;
        this.focusArea = "";
        this.instruction = "";
        this.weather = "";
    }

    public Workout(String weather, int difficulty, String gym, String focusArea, String instruction, String name) {
        this.name = name;
        this.focusArea = focusArea;
        this.difficulty = difficulty;
        this.instruction = instruction;
        this.weather = weather;
    }

    public Workout(Workout w) {
        this.name = w.name;
        this.focusArea = w.focusArea;
        this.difficulty = w.difficulty;
        this.instruction = w.instruction;
        this.weather = w.weather;
    }

    @NonNull
    @Override
    public String toString() {
        return name + ": " + focusArea + "; " + difficulty;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public String getFocusArea() {
        return focusArea;
    }

    public String getGym() {
        return gym;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getName() {
        return name;
    }

    public String getWeather() {
        return weather;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setFocusArea(String focusArea) {
        this.focusArea = focusArea;
    }

    public void setGym(String gym) {
        this.gym = gym;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

}
