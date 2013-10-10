package com.kylewbanks.database.orm;

/**
 * Created by kylewbanks on 2013-10-10.
 */
public class TagORM {

    public static final String TABLE_NAME = "tag";

    public static final String COLUMN_NAME_TYPE = "TEXT";
    public static final String COLUMN_NAME = "name";


    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME + " " + COLUMN_NAME_TYPE +
            ")";

    public static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}
