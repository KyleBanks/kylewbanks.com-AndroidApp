package com.kylewbanks.database.orm;

import android.content.ContentValues;
import android.database.Cursor;
import com.kylewbanks.model.Tag;

/**
 * Created by kylewbanks on 2013-10-10.
 */
public class TagORM {

    public static final String TABLE_NAME = "tag";
    public static final String COMMA_SEP = ", ";

    public static final String COLUMN_NAME_TYPE = "TEXT";
    public static final String COLUMN_NAME = "name";

    public static final String COLUMN_POST_ID_TYPE = "INTEGER";
    public static final String COLUMN_POST_ID = "post_id";


    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME + " " + COLUMN_NAME_TYPE + COMMA_SEP +
                COLUMN_POST_ID + " " + COLUMN_POST_ID_TYPE +
            ")";

    public static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;


    public TagORM() {

    }

    public ContentValues tagToContentValues(Tag tag, long postId) {
        ContentValues values = new ContentValues();
        values.put(TagORM.COLUMN_NAME, tag.getName());
        values.put(TagORM.COLUMN_POST_ID, postId);

        return values;
    }

    public Tag cursorToTag(Cursor cursor) {
        Tag tag = new Tag();
        tag.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));

        return tag;
    }
}
