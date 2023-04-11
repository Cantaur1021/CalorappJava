package com.example.calorappjava;

public class User {
    String id, Name, email;


    public User() {

    }



    public User(String id, String name, String email) {
        this.Name = name;
        this.email = email;
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}