package com.beno.reclamator.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.beno.reclamator.Entry;

public class DatabaseWriter {
    public DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public DatabaseWriter(DatabaseHelper helper) {
        dbHelper = helper;
        open();
    }

    public void close() {
        db.close();
        dbHelper.close();
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void insert(Entry entry) {
        ContentValues values = new ContentValues();

        values.put(Contract.Entry.COLUMN_NAME_COMPANY, entry.company);
        values.put(Contract.Entry.COLUMN_NAME_PROBLEM, entry.problem);
        values.put(Contract.Entry.COLUMN_NAME_OPERATOR, entry.operator);
        values.put(Contract.Entry.COLUMN_NAME_PROTOCOL, entry.protocol);
        values.put(Contract.Entry.COLUMN_NAME_OBSERVATIONS, entry.observations);
        values.put(Contract.Entry.COLUMN_NAME_TIME, entry.getTime());

        entry.setId(db.insert(Contract.Entry.TABLE_NAME, "null", values));
    }

    public void update(Entry entry) {
        delete(entry);
        insert(entry);
    }

    public void delete(Entry entry) {
        String selection = Contract.Entry.COLUMN_NAME_TIME + "=?";
        String[] selectionArgs = { String.valueOf(entry.getTime()) };

        db.delete(Contract.Entry.TABLE_NAME, selection, selectionArgs);
    }
}
