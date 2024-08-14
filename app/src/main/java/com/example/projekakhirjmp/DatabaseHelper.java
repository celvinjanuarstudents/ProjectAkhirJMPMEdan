package com.example.projekakhirjmp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Constants for the User table
    public static final String USER_DATABASE_NAME = "AppMHS";
    public static final String USER_TABLE_NAME = "tbluser";
    public static final String USER_COL_1 = "ID";
    public static final String USER_COL_2 = "USER";
    public static final String USER_COL_3 = "PASSWORD";

    private static final String TABLE_NAME = "tbldata";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "NAMA";
    private static final String COL_3 = "UMUR";
    private static final String COL_4 = "MOTO";

    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, USER_DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create user table
        db.execSQL("CREATE TABLE " + USER_TABLE_NAME + " (" +
                USER_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_COL_2 + " TEXT, " +
                USER_COL_3 + " TEXT)");

        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_2 + " TEXT, " +
                COL_3 + " INTEGER," +
                COL_4 + " TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade user table
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String nama, int umur, String moto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, nama);
        contentValues.put(COL_3, umur);
        contentValues.put(COL_4, moto);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }
    public boolean updateData(String id, String nama, int umur, String moto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, nama);
        contentValues.put(COL_3, umur);
        contentValues.put(COL_4, moto);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }


    // User table methods
    public boolean insertUser(String user, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL_2, user);
        contentValues.put(USER_COL_3, password);
        long result = db.insert(USER_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public boolean checkUser(String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE USER = ?", new String[]{user});
        return cursor.getCount() > 0;
    }

    public boolean checkUserPassword(String user, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " WHERE USER = ? AND PASSWORD = ?", new String[]{user, password});
        return cursor.getCount() > 0;
    }



}