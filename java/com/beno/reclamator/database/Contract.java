package com.beno.reclamator.database;

import android.provider.BaseColumns;

public final class Contract {
    public Contract() {}

    public static abstract class Entry implements BaseColumns {
        public static final String TABLE_NAME = "entries";

        public static final String COLUMN_NAME_COMPANY      = "company";
        public static final String COLUMN_NAME_PROBLEM      = "problem";
        public static final String COLUMN_NAME_OPERATOR     = "operator";
        public static final String COLUMN_NAME_PROTOCOL     = "protocol";
        public static final String COLUMN_NAME_OBSERVATIONS = "observations";
        public static final String COLUMN_NAME_TIME         = "time";
    }

    public static abstract class Company implements BaseColumns {
        public static final String TABLE_NAME = "companies";

        public static final String NAME_COLUMN = "name";
        public static final String PHONE_COLUMN = "phone";
        public static final String EMAIL_COLUMN = "email";
        public static final String ADDRESS_COLUMN = "address";
        public static final String WEBSITE_COLUMN = "website";
    }
}
