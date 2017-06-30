package com.firebasetest.octagono.firebasetest2.Models;

/**
 * Created by OCTAGONO on 6/23/2017.
 */

public class User {

    private String Email;
    private String Name;

    public User() {
    }

    public User(String email, String name) {
        Email = email;

        Name = name;
    }

    public String getName() {

        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public User(String email) {
        Email = email;
    }

    public String getEmail() {

        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
