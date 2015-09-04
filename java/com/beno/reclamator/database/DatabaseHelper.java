package com.beno.reclamator.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "EntriesDatabase";

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "Entries.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE VIRTUAL TABLE " + Contract.Entry.TABLE_NAME +  " USING fts3 (" +
            Contract.Entry.COLUMN_NAME_COMPANY + "," +
            Contract.Entry.COLUMN_NAME_PROBLEM + "," +
            Contract.Entry.COLUMN_NAME_OPERATOR + "," +
            Contract.Entry.COLUMN_NAME_PROTOCOL + "," +
            Contract.Entry.COLUMN_NAME_OBSERVATIONS + "," +
            Contract.Entry.COLUMN_NAME_TIME + ")";

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
        Log.w(TAG, "Upgrading database from " + oldVersion + " to " + newVersion);
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
