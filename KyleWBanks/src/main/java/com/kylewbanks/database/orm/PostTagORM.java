package com.kylewbanks.database.orm;

/**
 * Created by kylewbanks on 2013-10-10.
 */
public class PostTagORM {

    public static final String TABLE_NAME = "post_tag";

    public static final String COMMA_SEP = ", ";

    public static final String COLUMN_POST_ID_TYPE = "INTEGER";
    public static final String COLUMN_POST_ID = "post_id";

    public static final String COLUMN_TAG_ID_TYPE = "INTEGER";
    public static final String COLUMN_TAG_ID = "tag_id";


    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_POST_ID + " " + COLUMN_POST_ID_TYPE + COMMA_SEP +
                COLUMN_TAG_ID + " " + COLUMN_TAG_ID_TYPE +
            ")";

    public static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
}
