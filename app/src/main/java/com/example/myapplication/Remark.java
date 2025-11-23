package com.example.myapplication;

public class Remark {
    private int id;
    private String content;
    private int ifSpecial;//是否特别关注，0表示否，1表示是
    private int userId;
    private int pId;//主体账号id，本demo默认为1
    public Remark(int id, String content,int ifSpecial, int userId, int pId) {
        this.id = id;
        this.content = content;
        this.userId = userId;
        this.pId = pId;
        this.ifSpecial = ifSpecial;
    }
    public int getId() {
        return id;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public int getIfSpecial() {
        return ifSpecial;
    }
    public void setIfSpecial(int ifSpecial) {
        this.ifSpecial = ifSpecial;
    }
    public int getUserId() {
        return userId;
    }
    public int getPId() {
        return pId;
    }
}
