package com.kylewbanks.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.kylewbanks.KWBApplication;
import com.kylewbanks.R;
import com.kylewbanks.animlv.AnimatedListView;
import com.kylewbanks.animlv.AnimatedListViewAdapter;
import com.kylewbanks.animlv.AnimatedListViewObjectMapper;
import com.kylewbanks.event.PostListUpdateListener;
import com.kylewbanks.model.Post;

import java.util.List;

public class MainActivity extends Activity implements PostListUpdateListener {

    private static final String TAG = "MainActivity";

    private List<Post> postList;

    private AnimatedListView postListView;
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
        postListView = (AnimatedListView) findViewById(R.id.post_list);
        postListView.setOnItemClickListener(postItemSelectedListener);

        progressBar = (ProgressBar) findViewById(R.id.loader);

        //Register as a Post List update listener
        KWBApplication application = (KWBApplication) getApplication();
        application.registerPostUpdateListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Check for any updated Posts
        KWBApplication application = (KWBApplication) getApplication();
        application.checkForUpdates();
    }

    /**
     * Inherited from PostListUpdateListener to be notified when the Post list changes
     * @param posts
     */
    @Override
    public void onPostListLoaded(List<Post> posts) {
        this.postList = posts;

        runOnUiThread(reloadPostList);
    }

    /**
     * Triggered when the post list is updated in order to update the UI
     */
    private Runnable reloadPostList = new Runnable() {
        @Override
        public void run() {
            AnimatedListViewAdapter postListAdapter = new AnimatedListViewAdapter(MainActivity.this, R.layout.post_list_item, postList, objectMapper);
            postListView.setAdapter(postListAdapter);

            progressBar.setVisibility(View.GONE);
        }
    };

    /**
     * Called to bind a Post object to a View for the AnimatedListView
     */
    private AnimatedListViewObjectMapper objectMapper = new AnimatedListViewObjectMapper() {
        @Override
        public void bindObjectToView(Object object, View view) {
            Post post = (Post) object;

            TextView txtTitle = (TextView) view.findViewById(R.id.post_item_title);
            txtTitle.setText(post.getTitle());

            TextView txtPreview = (TextView) view.findViewById(R.id.post_item_preview);
            txtPreview.setText(post.getPreview());
        }
    };

    /**
     * Triggered when a Post is selected from the ListView
     */
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
