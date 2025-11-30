package com.example.myapplication;


import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("dyName")
    private String dyName;

    @SerializedName("avatarUrl")
    private String avatarUrl;

    @SerializedName("status")
    private int status;//0代表未关注，1代表已关注
    public User() {}
    
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
    public String getAvatarUrl() {
        return avatarUrl;
    }
}
