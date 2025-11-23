package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "list.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "users";
    private static final String TABLE_REMARKS = "remarks";
    private static final String USERS_ID = "id";
    private static final String USERS_NAME = "name";
    private static final String USERS_DY_NAME = "dy_name";
    private static final String USERS_STATUS = "status";
    private static final String REMARKS_ID = "id";
    private static final String REMARKS_CONTENT = "content";
    private static final String REMARKS_IF_SPECIAL = "if_special";
    private static final String REMARKS_USER_ID = "user_id";
    private static final String REMARKS_P_ID = "p_id";
    
    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + "( " +
                USERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USERS_NAME + " TEXT NOT NULL, " +
                USERS_DY_NAME + " TEXT NOT NULL, " +
                USERS_STATUS + " INTEGER NOT NULL);";
        db.execSQL(createUsersTable);
        String createRemarksTable = "CREATE TABLE " + TABLE_REMARKS + "( " +
                REMARKS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                REMARKS_CONTENT + " TEXT NOT NULL, " +
                REMARKS_IF_SPECIAL + " INTEGER NOT NULL, " +
                REMARKS_USER_ID + " INTEGER NOT NULL, " +
                REMARKS_P_ID + " INTEGER NOT NULL);";
        db.execSQL(createRemarksTable);
                
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMARKS);
        onCreate(db);
    }
}
