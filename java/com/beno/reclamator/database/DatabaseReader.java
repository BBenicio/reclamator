package com.beno.reclamator.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.beno.reclamator.Entry;

import java.util.ArrayList;

public class DatabaseReader {
    public DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private final String[] projection = { Contract.Entry.COLUMN_NAME_COMPANY,
                                    Contract.Entry.COLUMN_NAME_PROBLEM,
                                    Contract.Entry.COLUMN_NAME_OPERATOR,
                                    Contract.Entry.COLUMN_NAME_PROTOCOL,
                                    Contract.Entry.COLUMN_NAME_OBSERVATIONS,
                                    Contract.Entry.COLUMN_NAME_TIME };

    public DatabaseReader(DatabaseHelper helper) {
        dbHelper = helper;
        open();
    }

    public void open() {
        db = dbHelper.getReadableDatabase();
    }

    public void close() {
        db.close();
        dbHelper.close();
    }

    public ArrayList<Entry> query() {
        return query(null, null);
    }

    public ArrayList<Entry> search(String query) throws IllegalArgumentException {
        return search(query, 0, 0);
    }

    public ArrayList<Entry> search(String query, int offset, int limit) throws IllegalArgumentException {
        if (limit < 0) {
            throw new IllegalArgumentException("Argument 'limit' cannot be negative");
        } else if (offset < 0) {
            throw new IllegalArgumentException("Argument 'offset' cannot be negative");
        }

        final String SEARCH = " LIKE '%?%'";

        String selection = Contract.Entry.COLUMN_NAME_COMPANY + SEARCH + " OR " +
                           Contract.Entry.COLUMN_NAME_PROBLEM + SEARCH + " OR " +
                           Contract.Entry.COLUMN_NAME_OPERATOR + SEARCH + " OR " +
                           Contract.Entry.COLUMN_NAME_PROTOCOL + SEARCH + " OR " +
                           Contract.Entry.COLUMN_NAME_OBSERVATIONS + SEARCH;

        String[] selectionArgs = { query, query, query, query, query };

        if (limit == 0)
            return execute(selection, selectionArgs, null);
        else {
            return execute(selection, selectionArgs, offset + "," + limit);
        }

    }

    public ArrayList<Entry> query(String company, String problem) {
        String selection = "";
        ArrayList<String> selectionArgs = new ArrayList<>();

        if (company != null) {
            selection += Contract.Entry.COLUMN_NAME_COMPANY + " = '?'";
            selectionArgs.add(company);
        }
        if (problem != null) {
            selection += " AND " + Contract.Entry.COLUMN_NAME_PROBLEM + " = '?'";
            selectionArgs.add(problem);
        }

        return execute(selection, (String[])selectionArgs.toArray(), null);
    }

    private ArrayList<Entry> execute(String selection, String[] selectionArgs, String limit) {
        ArrayList<Entry> entries = new ArrayList<>();

        final String sortOrder = Contract.Entry.COLUMN_NAME_COMPANY + "," +
                                 Contract.Entry.COLUMN_NAME_PROBLEM + "," +
                                 Contract.Entry.COLUMN_NAME_TIME + " DESC";

        Cursor cursor;
        if (limit != null) {
            cursor = db.query(Contract.Entry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder, limit);
        } else {
            cursor = db.query(Contract.Entry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        }
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount(); ++i) {
            entries.add(new Entry(
                    cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_NAME_COMPANY)),
                    cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_NAME_PROBLEM)),
                    cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_NAME_OPERATOR)),
                    cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_NAME_PROTOCOL)),
                    cursor.getString(cursor.getColumnIndex(Contract.Entry.COLUMN_NAME_OBSERVATIONS)),
                    cursor.getLong(cursor.getColumnIndex(Contract.Entry.COLUMN_NAME_TIME))));

            cursor.moveToNext();
        }

        cursor.close();
        return entries;
    }
}
