package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAO {
    private SQLiteHelper helper;
    private SQLiteDatabase database;

    public DAO(Context context){
        helper = new SQLiteHelper(context);
        open();
    }


    public void open(){
        database = helper.getWritableDatabase();
    }

    public void close(){
        if(database != null){
            database.close();
            database = null;
        }
    }
    public void addUser(String name,String dyName,int status){
        String sql = "insert into users (name,dy_name,status) values ('" + name + "','" + dyName + "'," + status + ")";
        database.execSQL(sql);
    }
    public List<User> getUserList(){
        List<User> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("select * from users where status = 1", null);
        try{
            while (cursor.moveToNext()){
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String dyName = cursor.getString(2);
                int status = cursor.getInt(3);
                User user = new User(id,name,status,dyName);
                list.add(user);
            }
        }
        finally {
            cursor.close();
        }
        return list;
    }
    public List<Map<String,Object>> getList(){//后续应重构为该泛型方法
        List<Map<String,Object>> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("select * from users", null);
        try{
            while (cursor.moveToNext()){
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String dyName = cursor.getString(2);
                int status = cursor.getInt(3);
                String content = "";
                Cursor cursorRemark = database.rawQuery("select content from remarks where user_id = " + String.valueOf(id) +" and pId = 1", null);
                try{
                    if (!cursorRemark.isNull(0)){
                        cursorRemark.moveToFirst();
                        content = cursorRemark.getString(0);
                    }
                    Map<String,Object> map = new HashMap<>();
                    map.put("id",id);
                    map.put("name",name);
                    map.put("dyName",dyName);
                    map.put("status",status);
                    map.put("content",content);
                    list.add(map);
                }
                finally {
                    cursorRemark.close();
                }
            }
        }
        finally {
            cursor.close();
        }
        
        return list;
    }

    public void updateUser(User user) {
        String sql = "update users set name = '" + user.getName() + "',dy_name = '" + user.getDyName() + "',status = " + user.getStatus() + " where id = " + user.getId();
        database.execSQL(sql);
    }

    public String getRemarkContent(int userId, int pId) {
        String content = "";
        Cursor cursor = database.rawQuery("select content from remarks where user_id = " + userId + " and p_id = " + pId, null);
        try {
            if (cursor.moveToFirst()) {
                content = cursor.getString(0);
            }
        } finally {
            cursor.close();
        }
        return content;
    }

    public int getIfSpecial(int userId, int pId) {
        int ifSpecial = 0;
        Cursor cursor = database.rawQuery("select if_special from remarks where user_id = " + userId + " and p_id = " + pId, null);
        try {
            if (cursor.moveToFirst()) {
                ifSpecial = cursor.getInt(0);
            }
        } finally {
            cursor.close();
        }
        return ifSpecial;
    }

    public void updateSpecialAttention(int userId, int pId, int ifSpecial) {
        Cursor cursor = database.rawQuery("select id from remarks where user_id = " + userId + " and p_id = " + pId, null);
        try {
            if (cursor.moveToFirst()) {
                String sql = "update remarks set if_special = " + ifSpecial + " where user_id = " + userId + " and p_id = " + pId;
                database.execSQL(sql);
            } else {
                String sql = "insert into remarks (content, if_special, user_id, p_id) values ('', " + ifSpecial + ", " + userId + ", " + pId + ")";
                database.execSQL(sql);
            }
        } finally {
            cursor.close();
        }
    }

    public void saveOrUpdateRemark(int userId, int pId, String content) {
        Cursor cursor = database.rawQuery("select id from remarks where user_id = " + userId + " and p_id = " + pId, null);
        try {
            if (cursor.moveToFirst()) {
                String sql = "update remarks set content = '" + content + "' where user_id = " + userId + " and p_id = " + pId;
                database.execSQL(sql);
            } else {
                String sql = "insert into remarks (content, if_special, user_id, p_id) values ('" + content + "', 0, " + userId + ", " + pId + ")";
                database.execSQL(sql);
            }
        } finally {
            cursor.close();
        }
    }
}
