package com.kylewbanks.database.orm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.kylewbanks.database.DatabaseWrapper;
import com.kylewbanks.model.Post;
import com.kylewbanks.model.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kylewbanks on 2013-10-10.
 */
public class TagORM {

    private static final String TAG = "TagORM";

    private static final String TABLE_NAME = "tag";
    private static final String COMMA_SEP = ", ";

    private static final String COLUMN_NAME_TYPE = "TEXT";
    private static final String COLUMN_NAME = "name";

    private static final String COLUMN_POST_ID_TYPE = "INTEGER";
    private static final String COLUMN_POST_ID = "post_id";


    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME + " " + COLUMN_NAME_TYPE + COMMA_SEP +
                COLUMN_POST_ID + " " + COLUMN_POST_ID_TYPE +
            ")";

    public static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;


    /**
     * Inserts a Tag object into the database with an association with the specified Post
     * @param context
     * @param tag
     * @param postId
     * @return
     */
    public static boolean insertTag(Context context, Tag tag, long postId) {
        ContentValues values = TagORM.tagToContentValues(tag, postId);
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getWritableDatabase();

        boolean success = false;
        try {
            if(database != null) {
                database.insert(TagORM.TABLE_NAME, "null", values);
                Log.i(TAG, "Inserted new Tag [" + tag.getName() + "] for Post [" + postId + "]");
                success = true;
            }
        } catch (NullPointerException ex) {
            Log.e(TAG, "Failed to insert Tag[" + tag.getName() + "] due to: " + ex);
        } finally {
            if (database != null) {
                database.close();
            }
        }

        return success;
    }

    /**
     * Gets a list of all cached Tags associated with the specified Post
     * @param context
     * @param post
     * @return
     */
    public static final List<Tag> getTagsForPost(Context context, Post post) {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();

        List<Tag> tagList = null;

        if (database != null) {
            Cursor cursor = database.rawQuery(
                    "SELECT * FROM " + TagORM.TABLE_NAME + " WHERE " + TagORM.COLUMN_POST_ID + " = " + post.getId(), null
            );

            Log.i(TAG, "Loaded " + cursor.getCount() + " Tags for Post["+post.getId()+"]...");
            if(cursor.getCount() > 0) {
                tagList = new ArrayList<Tag>();

                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    tagList.add(TagORM.cursorToTag(cursor));
                    cursor.moveToNext();
                }
                Log.i(TAG, "Tags loaded successfully for Post["+post.getId()+"]");
            }

            database.close();
        }
        return tagList;
    }

    /**
     * Packs a Tag object into a ContentValues map for use with SQL inserts.
     * @param tag
     * @param postId
     * @return
     */
    private static ContentValues tagToContentValues(Tag tag, long postId) {
        ContentValues values = new ContentValues();
        values.put(TagORM.COLUMN_NAME, tag.getName());
        values.put(TagORM.COLUMN_POST_ID, postId);

        return values;
    }

    /**
     * Populates a Tag object with data from a Cursor
     * @param cursor
     * @return
     */
    private static Tag cursorToTag(Cursor cursor) {
        Tag tag = new Tag();
        tag.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));

        return tag;
    }
}
