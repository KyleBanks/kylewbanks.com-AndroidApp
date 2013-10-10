package com.kylewbanks.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.kylewbanks.KWBApplication;
import com.kylewbanks.R;
import com.kylewbanks.model.Post;
import com.kylewbanks.view.adapter.PostListAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends Activity implements PostListInterface {

    private static final String TAG = "MainActivity";

    private List<Post> postList;

    private ListView postListView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Customize the action bar
        ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);

            LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.header, null);

            Typeface scriptFont = Typeface.createFromAsset(getAssets(), "fonts/script.ttf");
            TextView title = (TextView) v.findViewById(R.id.title);
            title.setTypeface(scriptFont);

            actionBar.setCustomView(v);
        }

        //Initialize Views
        postListView = (ListView) findViewById(R.id.post_list);
        postListView.setOnItemClickListener(postItemSelectedListener);

        progressBar = (ProgressBar) findViewById(R.id.loader);

        //Register as a Post List Interface
        KWBApplication application = (KWBApplication) getApplication();
        application.registerPostListInterface(this);
    }


    @Override
    public void onPostListLoaded(List<Post> posts) {
        this.postList = posts;

        runOnUiThread(reloadPostList);
    }

    private Runnable reloadPostList = new Runnable() {
        @Override
        public void run() {
            PostListAdapter postListAdapter = new PostListAdapter(MainActivity.this, R.layout.post_list_item, postList);
            postListView.setAdapter(postListAdapter);

            progressBar.setVisibility(View.GONE);
        }
    };

    private ListView.OnItemClickListener postItemSelectedListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            Post selectedPost = postList.get(position);

            Intent toPostActivity = new Intent(MainActivity.this, PostActivity.class);
            toPostActivity.putExtra("postId", selectedPost.getId());
            startActivity(toPostActivity);
            overridePendingTransition(R.anim.start_right_to_left, R.anim.start_left_to_right);
        }
    };
    
}
