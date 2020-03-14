package com.example.fitnessadvisor;

public class User {
    private String name, id;
    private int age;

    public User(String name, String id, int age){
        this.name = name;
        this.id = id;
        this.age = age;
    }

    public User(){
        this.name = "";
        this.id = "";
        this.age = 0;
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

}
