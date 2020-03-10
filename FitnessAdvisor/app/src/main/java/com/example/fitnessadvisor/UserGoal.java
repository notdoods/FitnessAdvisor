package com.example.fitnessadvisor;

import androidx.annotation.NonNull;

/** Goal: options from drop down
 *      - Calories burned
 *      - Active minutes
 *      - Other
 */

public class UserGoal {
    private String description;
    private int value;

    public UserGoal(String description, int value){
        this.description = description;
        this.value = value;
    }

    // Accessors
    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    // Mutators
    public void setDescription(String description) {
        this.description = description;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @NonNull
    @Override
    public String toString() {
        if (value != 0)
            return "Goal for " + description + ": " + value;
        return "Other Goal: " + description;
    }
}
