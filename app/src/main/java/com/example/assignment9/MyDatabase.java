package com.example.assignment9;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabase extends SQLiteOpenHelper {

    public static final String DB_NAME = "College";
    public static final String TABLE_NAME = "Students";
    public static final int VERSION = 1;
    Context ctx;

    SQLiteDatabase mySqLiteDatabase;

    public MyDatabase(Context ctx){
        super(ctx,DB_NAME,null,VERSION);
        this.ctx = ctx;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + " (_id integer primary key autoincrement, name text, email text," +
                "course text, gender text, contact text, birthdate text, address text, bloodgroup text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void insertData(String name, String email, String course, String gender, String contact, String birthdate, String address, String bloodgroup){
        mySqLiteDatabase = getWritableDatabase();
        mySqLiteDatabase.execSQL("insert into " + TABLE_NAME + " (name,email,course,gender,contact,birthdate,address,bloodgroup)" +
                " values('"+name+"','"+email+"','"+course+"','"+gender+"','"+contact+"','"+birthdate+"','"+address+"','"+bloodgroup+"');");
    }

    public Cursor toGetData(String sql){
        mySqLiteDatabase = getReadableDatabase();
        return mySqLiteDatabase.rawQuery(sql,null);
    }

    public void toDeleteOrUpdateData(String sql){
        mySqLiteDatabase = getWritableDatabase();
        mySqLiteDatabase.execSQL(sql);
    }
}
