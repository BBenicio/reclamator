package com.beno.reclamator.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.beno.reclamator.Company;
import com.beno.reclamator.Entry;

import java.util.ArrayList;

public class DatabaseReader {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private final String[] entriesProjection = {
            Contract.Entry.COLUMN_NAME_COMPANY,
            Contract.Entry.COLUMN_NAME_PROBLEM,
            Contract.Entry.COLUMN_NAME_OPERATOR,
            Contract.Entry.COLUMN_NAME_PROTOCOL,
            Contract.Entry.COLUMN_NAME_OBSERVATIONS,
            Contract.Entry.COLUMN_NAME_TIME
    };

    private final String[] companiesProjection = {
            Contract.Company.NAME_COLUMN,
            Contract.Company.PHONE_COLUMN,
            Contract.Company.EMAIL_COLUMN,
            Contract.Company.ADDRESS_COLUMN,
            Contract.Company.WEBSITE_COLUMN
    };

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

    public ArrayList<Entry> queryEntries() {
        return queryEntries(null, null);
    }

    public ArrayList<Entry> searchEntries(String query) {
        String selection = Contract.Entry.TABLE_NAME + " MATCH ?";

        return executeOnEntries(selection, new String[] { "'*" + query + "*'" });
    }

    public ArrayList<Entry> queryEntries(String company, String problem) {
        String selection = "";
        ArrayList<String> selectionArgs = new ArrayList<>();

        if (company != null) {
            selection += Contract.Entry.COLUMN_NAME_COMPANY + " MATCH ?";
            selectionArgs.add("'" + company + "'");
        }
        if (problem != null) {
            selection += " AND " + Contract.Entry.COLUMN_NAME_PROBLEM + " MATCH ?";
            selectionArgs.add("'" + problem + "'");
        }

        return executeOnEntries(selection, selectionArgs.toArray(new String[selectionArgs.size()]));
    }

    private ArrayList<Entry> executeOnEntries(String selection, String[] selectionArgs) {
        ArrayList<Entry> entries = new ArrayList<>();

        final String sortOrder = Contract.Entry.COLUMN_NAME_COMPANY + "," +
                                 Contract.Entry.COLUMN_NAME_PROBLEM + "," +
                                 Contract.Entry.COLUMN_NAME_TIME + " DESC";

        Cursor cursor = db.query(Contract.Entry.TABLE_NAME, entriesProjection, selection, selectionArgs,
                                 null, null, sortOrder);

        if (cursor == null) {
            return entries;
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

    public ArrayList<Company> searchCompanies(String query) {
        return executeOnCompanies(Contract.Company.TABLE_NAME + " MATCH ?", new String[] { query });
    }

    public ArrayList<Company> queryCompanies() {
        return executeOnCompanies(null, null);
    }

    public ArrayList<Company> queryCompanies(String name) {
        return executeOnCompanies(Contract.Company.NAME_COLUMN + " MATCH ?", new String[] { name });
    }

    private ArrayList<Company> executeOnCompanies(String selection, String[] selectionArgs) {
        Cursor cursor = db.query(Contract.Company.TABLE_NAME, companiesProjection, selection, selectionArgs,
                                 null, null, Contract.Company.NAME_COLUMN + " DESC");

        ArrayList<Company> companies = new ArrayList<>();

        if (cursor == null) {
            return companies;
        }
        cursor.moveToFirst();

        for (int i = 0; i <cursor.getCount(); ++i) {
            companies.add(new Company(
                    cursor.getString(cursor.getColumnIndex(Contract.Company.NAME_COLUMN)),
                    cursor.getString(cursor.getColumnIndex(Contract.Company.PHONE_COLUMN)),
                    cursor.getString(cursor.getColumnIndex(Contract.Company.EMAIL_COLUMN)),
                    cursor.getString(cursor.getColumnIndex(Contract.Company.ADDRESS_COLUMN)),
                    cursor.getString(cursor.getColumnIndex(Contract.Company.WEBSITE_COLUMN))));

            cursor.moveToNext();
        }

        cursor.close();
        return companies;
    }
}
