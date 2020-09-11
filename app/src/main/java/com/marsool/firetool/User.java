package com.marsool.firetool;

public class User {

    private int id;
    private String phone;
    boolean state;

    public User(int id, String phone, boolean state) {
        this.id = id;
        this.phone = phone;
        this.state = state;
    }

    public int getId() {
        return id;
    }


    public String getphone() {
        return phone;
    }

    public boolean getstate() {
        return state;
    }
}