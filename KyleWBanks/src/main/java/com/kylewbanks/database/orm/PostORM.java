package com.kylewbanks.database.orm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.kylewbanks.database.DatabaseWrapper;
import com.kylewbanks.model.Post;
import com.kylewbanks.model.Tag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by kylewbanks on 2013-10-10.
 */
public class PostORM {

    private static final String TAG = "PostORM";

    private static final String TABLE_NAME = "post";

    private static final String COMMA_SEP = ", ";

    private static final String COLUMN_ID_TYPE = "INTEGER PRIMARY KEY";
    private static final String COLUMN_ID = "id";

    private static final String COLUMN_TITLE_TYPE = "TEXT";
    private static final String COLUMN_TITLE = "title";

    private static final String COLUMN_PREVIEW_TYPE = "TEXT";
    private static final String COLUMN_PREVIEW = "preview";

    private static final String COLUMN_BODY_TYPE = "TEXT";
    private static final String COLUMN_BODY = "body";

    private static final String COLUMN_URL_TYPE = "TEXT";
    private static final String COLUMN_URL = "url";

    private static final String COLUMN_DATE_TYPE = "TEXT";
    private static final String COLUMN_DATE = "pubdate";


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

    private static final SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ", Locale.ENGLISH);

    /**
     * Fetches the full list of Posts stored in the local Database
     * @param context
     * @return
     */
    public static List<Post> getPosts(Context context) {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();

        if(database != null) {
            Cursor cursor = database.rawQuery("SELECT * FROM " + PostORM.TABLE_NAME, null);

            Log.i(TAG, "Loaded " + cursor.getCount() + " Posts...");
            if(cursor.getCount() > 0) {
                List<Post> postList = new ArrayList<Post>();

                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Post post = cursorToPost(cursor);
                    post.setTags(TagORM.getTagsForPost(context, post));

                    postList.add(post);
                    cursor.moveToNext();
                }
                Log.i(TAG, "Posts loaded successfully.");
                return postList;
            }
        }

        return null;
    }

    /**
     * Inserts a Post object into the local database
     * @param context
     * @param post
     * @return
     */
    public static boolean insertPost(Context context, Post post) {
        ContentValues values = postToContentValues(post);

        try {
            DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
            SQLiteDatabase database = databaseWrapper.getWritableDatabase();

            if (database != null) {
                long postId = database.insert(PostORM.TABLE_NAME, "null", values);
                Log.i(TAG, "Inserted new Post with ID: " + postId);

                for (Tag tag : post.getTags()) {
                    TagORM.insertTag(context, tag, post.getId());
                }
                return true;
            }
        } catch (NullPointerException ex) {
            Log.e(TAG, "Failed to insert Post[" + post.getId() + "] due to: " + ex);
        }

        return false;
    }

    /**
     * Packs a Post object into a ContentValues map for use with SQL inserts.
     * @param post
     * @return
     */
    private static ContentValues postToContentValues(Post post) {
        ContentValues values = new ContentValues();
        values.put(PostORM.COLUMN_ID, post.getId());
        values.put(PostORM.COLUMN_TITLE, post.getTitle());
        values.put(PostORM.COLUMN_PREVIEW, post.getPreview());
        values.put(PostORM.COLUMN_BODY, post.getBody());
        values.put(PostORM.COLUMN_URL, post.getUrl());
        values.put(PostORM.COLUMN_DATE, _dateFormat.format(post.getDate()));

        return values;
    }

    /**
     * Populates a Post object with data from a Cursor
     * @param cursor
     * @return
     */
    private static Post cursorToPost(Cursor cursor) {
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
