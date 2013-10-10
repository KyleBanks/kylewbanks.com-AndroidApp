package com.kylewbanks.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.kylewbanks.database.orm.PostORM;
import com.kylewbanks.database.orm.PostTagORM;
import com.kylewbanks.database.orm.TagORM;
import com.kylewbanks.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kylewbanks on 2013-10-10.
 */
public class DatabaseWrapper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseWrapper";

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "KWB.db";

    public DatabaseWrapper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "Creating database [" + DATABASE_NAME + " v." + DATABASE_VERSION + "]...");

        sqLiteDatabase.execSQL(PostORM.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(PostTagORM.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(TagORM.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.i(TAG, "Upgrading database ["+DATABASE_NAME+" v." + oldVersion+"] to ["+DATABASE_NAME+" v." + newVersion+"]...");

        sqLiteDatabase.execSQL(PostORM.SQL_DROP_TABLE);
        sqLiteDatabase.execSQL(PostTagORM.SQL_DROP_TABLE);
        sqLiteDatabase.execSQL(TagORM.SQL_DROP_TABLE);
        onCreate(sqLiteDatabase);
    }


    public boolean insertPost(Post post) {
        PostORM postORM = new PostORM();
        ContentValues values = postORM.postToContentValues(post);

        try {
            SQLiteDatabase database = getWritableDatabase();

            long postId = database.insert(PostORM.TABLE_NAME, "null", values);
            Log.i(TAG, "Inserted new Post with ID: " + postId);
            return true;
        } catch (NullPointerException ex) {
            Log.e(TAG, "Failed to insert Post[" + post.getId() + "] due to: " + ex);
            return false;
        }
    }

    public List<Post> getPosts() {
        SQLiteDatabase database = getReadableDatabase();


        if(database != null) {
            Cursor cursor = database.rawQuery("SELECT * FROM " + PostORM.TABLE_NAME, null);

            Log.i(TAG, "Loaded " + cursor.getCount() + " Posts...");
            if(cursor.getCount() > 0) {
                List<Post> postList = new ArrayList<Post>();

                cursor.moveToFirst();
                PostORM postORM = new PostORM();
                while (!cursor.isAfterLast()) {
                    postList.add(postORM.cursorToPost(cursor));
                    cursor.moveToNext();
                }
                Log.i(TAG, "Posts loaded successfully.");
                return postList;
            }
        }

        return null;
    }
}
