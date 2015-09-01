package com.beno.reclamator.database;

import android.provider.BaseColumns;

public final class Contract {
    public Contract() {}

    public static abstract class Entry implements BaseColumns {
        public static final String TABLE_NAME = "entries";
        public static final String COLUMN_NAME_COMPANY = "company";
        public static final String COLUMN_NAME_PROBLEM = "problem";
        public static final String COLUMN_NAME_OPERATOR = "operator";
        public static final String COLUMN_NAME_PROTOCOL = "protocol";
        public static final String COLUMN_NAME_OBSERVATIONS = "observations";
        public static final String COLUMN_NAME_TIME = "time";
    }
}
