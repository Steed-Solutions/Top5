package com.mars_tech.shehriyar.top5.pojo;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class User implements Serializable {
    public String uid;
    public String name;
    public String email;
    public String password;
    public String gender;
    public String bio;
    public ArrayList<String> categories;
    public boolean isAuthenticated;
    public boolean isNew;

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String name, String email, String password, String gender, String bio) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.bio = bio;
    }

    public HashMap<String, String> toJson(){
        HashMap<String, String> json = new HashMap<>();
        json.put("name", name);
        json.put("email", email);
        json.put("gender", gender);
        json.put("bio", bio);
        return json;
    }
}
