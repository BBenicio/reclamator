package com.beno.reclamator.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.beno.reclamator.Company;
import com.beno.reclamator.Entry;

public class DatabaseWriter {
    private DatabaseHelper dbHelper;
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

    private ContentValues createContentValues(Entry entry) {
        ContentValues values = new ContentValues();

        values.put(Contract.Entry.COMPANY_COLUMN, entry.company);
        values.put(Contract.Entry.PROBLEM_COLUMN, entry.problem);
        values.put(Contract.Entry.OPERATOR_COLUMN, entry.operator);
        values.put(Contract.Entry.PROTOCOL_COLUMN, entry.protocol);
        values.put(Contract.Entry.OBSERVATIONS_COLUMN, entry.observations);
        values.put(Contract.Entry.TIME_COLUMN, entry.getTime());

        return values;
    }

    private ContentValues createContentValues(Company company) {
        ContentValues values = new ContentValues();

        values.put(Contract.Company.NAME_COLUMN, company.name);
        values.put(Contract.Company.PHONE_COLUMN, company.phoneNumber);
        values.put(Contract.Company.EMAIL_COLUMN, company.email);
        values.put(Contract.Company.ADDRESS_COLUMN, company.address);
        values.put(Contract.Company.WEBSITE_COLUMN, company.website);

        return values;
    }

    public void insert(Entry entry) {
        db.insert(Contract.Entry.TABLE_NAME, "null", createContentValues(entry));
    }

    public void insert(Company company) {
        db.insert(Contract.Company.TABLE_NAME, "null", createContentValues(company));
    }

    public void update(Entry entry) {
        db.update(Contract.Entry.TABLE_NAME, createContentValues(entry),
                Contract.Entry.TIME_COLUMN + " MATCH ?",
                new String[] { "'" + String.valueOf(entry.getTime()) + "'" });
    }

    public void update(Company company) {
        db.update(Contract.Company.TABLE_NAME, createContentValues(company),
                  Contract.Company.NAME_COLUMN + " MATCH ?",
                  new String[] { "'" + company.name + "'" });
    }

    public void delete(Entry entry) {
        db.delete(Contract.Entry.TABLE_NAME, Contract.Entry.TIME_COLUMN + " MATCH ?",
                  new String[] { "'" + String.valueOf(entry.getTime()) + "'" });
    }

    public void delete(Company company) {
        db.delete(Contract.Company.TABLE_NAME, Contract.Company.NAME_COLUMN + " MATCH ?",
                  new String[] { "'" + company.name + "'" });
    }
}
