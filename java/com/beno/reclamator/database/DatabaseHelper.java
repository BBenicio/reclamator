package com.beno.reclamator.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Entries.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + Contract.Entry.TABLE_NAME +
                                                     " ("  + Contract.Entry.COLUMN_NAME_COMPANY + TEXT_TYPE + "," +
                                                     Contract.Entry.COLUMN_NAME_PROBLEM + TEXT_TYPE + "," +
                                                     Contract.Entry.COLUMN_NAME_OPERATOR + TEXT_TYPE + "," +
                                                     Contract.Entry.COLUMN_NAME_PROTOCOL + TEXT_TYPE + "," +
                                                     Contract.Entry.COLUMN_NAME_OBSERVATIONS + TEXT_TYPE + "," +
                                                     Contract.Entry.COLUMN_NAME_TIME + " INTEGER PRIMARY KEY )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Contract.Entry.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
