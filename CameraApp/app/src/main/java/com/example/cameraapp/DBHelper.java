package com.example.cameraapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "Camera.db";

    public DBHelper(Context context) {
        super(context, NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE PICTURES ("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "IMAGE BLOB NOT NULL);");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // pass
    }


    public void insertPicture(SQLiteDatabase db, byte[] image) {
        ContentValues pictureValues = new ContentValues();
        pictureValues.put("IMAGE", image);
        db.insert("PICTURES", null, pictureValues);
    }
}


