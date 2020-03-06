package com.example.fitnessadvisor;

public class UserPreferences {
    private boolean walking, running, gym;
    //private UserGoal[] goals;

    public UserPreferences(boolean walking, boolean running, boolean gym) {
        this.walking = walking;
        this.running = running;
        this.gym = gym;
        //this.goals = goals;
    }

    // Accessors
    public boolean isGym() {
        return gym;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isWalking() {
        return walking;
    }

    /*public UserGoal[] getGoals() {
        return goals;
    }*/

    // Mutators
    /*public void setGoals(UserGoal[] goals) {
        this.goals = goals;
    }*/

    public void setGym(boolean gym) {
        this.gym = gym;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setWalking(boolean walking) {
        this.walking = walking;
    }
}
