package com.example.myapplication;

public class User {
    private int id;
    private String name;
    private String dyName;
    private int status;//0代表未关注，1代表已关注
    
    public User(int id, String name, int status,String dyName) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.dyName = dyName;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int newUserStatus) {
        this.status = newUserStatus;
    }

    public String getDyName() {
        return dyName;
    }
}
