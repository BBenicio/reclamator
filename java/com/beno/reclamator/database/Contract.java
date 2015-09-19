package com.beno.reclamator.database;

import android.provider.BaseColumns;

public final class Contract {
    public Contract() {}

    public static abstract class Entry implements BaseColumns {
        public static final String TABLE_NAME = "entries";

        public static final String COMPANY_COLUMN      = "company";
        public static final String PROBLEM_COLUMN      = "problem";
        public static final String OPERATOR_COLUMN     = "operator";
        public static final String PROTOCOL_COLUMN     = "protocol";
        public static final String OBSERVATIONS_COLUMN = "observations";
        public static final String TIME_COLUMN         = "time";
    }

    public static abstract class Company implements BaseColumns {
        public static final String TABLE_NAME = "companies";

        public static final String NAME_COLUMN    = "name";
        public static final String PHONE_COLUMN   = "phone";
        public static final String EMAIL_COLUMN   = "email";
        public static final String ADDRESS_COLUMN = "address";
        public static final String WEBSITE_COLUMN = "website";
    }
}
