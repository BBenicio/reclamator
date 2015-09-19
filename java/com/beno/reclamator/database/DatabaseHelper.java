package com.beno.reclamator.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "reclamator.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE VIRTUAL TABLE " + Contract.Entry.TABLE_NAME +  " USING fts3 (" +
            Contract.Entry.COMPANY_COLUMN + "," +
            Contract.Entry.PROBLEM_COLUMN + "," +
            Contract.Entry.OPERATOR_COLUMN + "," +
            Contract.Entry.PROTOCOL_COLUMN + "," +
            Contract.Entry.OBSERVATIONS_COLUMN + "," +
            Contract.Entry.TIME_COLUMN + ")";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Contract.Entry.TABLE_NAME;

    private static final String SQL_CREATE_COMPANIES =
            "CREATE VIRTUAL TABLE " + Contract.Company.TABLE_NAME + " USING fts3 (" +
            Contract.Company.NAME_COLUMN + "," +
            Contract.Company.PHONE_COLUMN + "," +
            Contract.Company.EMAIL_COLUMN + "," +
            Contract.Company.ADDRESS_COLUMN + "," +
            Contract.Company.WEBSITE_COLUMN + ")";

    private static final String SQL_DELETE_COMPANIES = "DROP TABLE IF EXISTS " + Contract.Company.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_COMPANIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from " + oldVersion + " to " + newVersion);
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_COMPANIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
