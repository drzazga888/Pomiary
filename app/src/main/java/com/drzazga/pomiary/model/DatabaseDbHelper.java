package com.drzazga.pomiary.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 14;
    public static final String DATABASE_NAME = "Database.db";

    public DatabaseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.SQL_CREATE_CATEGORIES);
        db.execSQL(DatabaseContract.SQL_CREATE_MEASURES);
        db.execSQL(DatabaseContract.SQL_CREATE_MEASURE_POINTS);
        db.execSQL(DatabaseContract.SQL_CREATE_MEASURE_LINES);
        db.execSQL(DatabaseContract.SQL_CREATE_MEASURE_ANGLES);
        db.execSQL(DatabaseContract.SQL_CREATE_VIEW_MEASURES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatabaseContract.SQL_DROP_VIEW_MEASURES);
        db.execSQL(DatabaseContract.SQL_DROP_MEASURE_ANGLES);
        db.execSQL(DatabaseContract.SQL_DROP_MEASURE_LINES);
        db.execSQL(DatabaseContract.SQL_DROP_MEASURE_POINTS);
        db.execSQL(DatabaseContract.SQL_DROP_MEASURES);
        db.execSQL(DatabaseContract.SQL_DROP_CATEGORIES);
        onCreate(db);
    }
}
