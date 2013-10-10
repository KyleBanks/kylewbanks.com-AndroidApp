package com.kylewbanks.database.orm;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.kylewbanks.model.Post;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kylewbanks on 2013-10-10.
 */
public class PostORM {

    private static final String TAG = "PostORM";

    public static final String TABLE_NAME = "post";

    public static final String COMMA_SEP = ", ";

    public static final String COLUMN_ID_TYPE = "INTEGER PRIMARY KEY";
    public static final String COLUMN_ID = "id";

    public static final String COLUMN_TITLE_TYPE = "TEXT";
    public static final String COLUMN_TITLE = "title";

    public static final String COLUMN_PREVIEW_TYPE = "TEXT";
    public static final String COLUMN_PREVIEW = "preview";

    public static final String COLUMN_BODY_TYPE = "TEXT";
    public static final String COLUMN_BODY = "body";

    public static final String COLUMN_URL_TYPE = "TEXT";
    public static final String COLUMN_URL = "url";

    public static final String COLUMN_DATE_TYPE = "TEXT";
    public static final String COLUMN_DATE = "pubdate";


    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " " + COLUMN_ID_TYPE + COMMA_SEP +
                COLUMN_TITLE  + " " + COLUMN_TITLE_TYPE + COMMA_SEP +
                COLUMN_PREVIEW + " " + COLUMN_PREVIEW_TYPE + COMMA_SEP +
                COLUMN_BODY + " " + COLUMN_BODY_TYPE + COMMA_SEP +
                COLUMN_URL + " " + COLUMN_URL_TYPE + COMMA_SEP +
                COLUMN_DATE + " " + COLUMN_DATE_TYPE +
            ")";

    public static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private SimpleDateFormat _dateFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);

    public PostORM() {

    }

    public ContentValues postToContentValues(Post post) {
        ContentValues values = new ContentValues();
        values.put(PostORM.COLUMN_ID, post.getId());
        values.put(PostORM.COLUMN_TITLE, post.getTitle());
        values.put(PostORM.COLUMN_PREVIEW, post.getPreview());
        values.put(PostORM.COLUMN_BODY, post.getBody());
        values.put(PostORM.COLUMN_URL, post.getUrl());
        values.put(PostORM.COLUMN_DATE, _dateFormat.format(post.getDate()));

        return values;
    }

    public Post cursorToPost(Cursor cursor) {
        Post post = new Post();
        post.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
        post.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
        post.setPreview(cursor.getString(cursor.getColumnIndex(COLUMN_PREVIEW)));
        post.setBody(cursor.getString(cursor.getColumnIndex(COLUMN_BODY)));
        post.setUrl(cursor.getString(cursor.getColumnIndex(COLUMN_URL)));

        String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
        try {
            post.setDate(_dateFormat.parse(date));
        } catch (ParseException ex) {
            Log.e(TAG, "Failed to parse date " + date + " for Post " + post.getId());
            post.setDate(null);
        }

        return post;
    }
}
