package com.example.fitnessadvisor;

import androidx.annotation.NonNull;

import java.util.prefs.Preferences;

public class User {
    private String name, id;
    private int age;
    //private UserPreferences p;

    public User(String name, String id, int age){
        this.name = name;
        this.id = id;
        this.age = age;
        //this.p = p;
    }


    // Mutators
    public void setId(String id) {
        this.id = id;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public void setP(UserPreferences p) {
        this.p = p;
    }*/

    // Accessors
    public int getAge() {
        return age;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /*public UserPreferences getP() {
        return p;
    }*/
}
