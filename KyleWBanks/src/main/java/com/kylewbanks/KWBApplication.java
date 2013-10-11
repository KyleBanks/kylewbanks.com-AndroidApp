package com.kylewbanks;

import android.app.Application;
import android.util.Log;
import com.kylewbanks.event.PostListUpdateListener;
import com.kylewbanks.database.orm.PostORM;
import com.kylewbanks.model.Post;
import com.kylewbanks.network.RESTController;
import com.kylewbanks.network.response.PostListResponse;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by kylewbanks on 2013-10-09.
 */
public class KWBApplication  extends Application {

    private static final String TAG = "KWBApplication";

    private PostListUpdateListener _postListUpdateListener;

    private Date _lastLoadedPostList;
    private static final int LOAD_POST_LIST_INTERVAL = 1000 * 60 * 10; //10 minutes

    /**
     * Register a listener for when the Post list becomes available, or is updated
     * @param postListUpdateListener
     */
    public void registerPostUpdateListener(PostListUpdateListener postListUpdateListener) {
        this._postListUpdateListener = postListUpdateListener;
    }

    /**
     * Refreshes the Posts from the database, and polls the remote server if enough time has passed
     */
    public void checkForUpdates() {
        if(_lastLoadedPostList == null || new Date().getTime() - _lastLoadedPostList.getTime() > LOAD_POST_LIST_INTERVAL) {
            this.loadPostList();
        } else {
            List<Post> posts = PostORM.getPosts(this);

            if(posts != null) {
                Collections.sort(posts);
                this._postListUpdateListener.onPostListLoaded(posts);
            }
        }
    }

    /**
     * Returns a Post object identified by the specified id, if available
     * @param postId
     * @return
     */
    public Post getPostById(long postId) {
        return PostORM.findPostById(this, postId);
    }

    /**
     * Fetches the list of Posts from the remove server
     */
    private void loadPostList() {
        Log.i(TAG, "Loading Post List....");

        RESTController.retrievePostList(postListResponse, new long[0]);
    }

    /**
     * Handler for the Post list being fetched remotely
     */
    private PostListResponse postListResponse = new PostListResponse() {
        @Override
        public void success(String json) {
            super.success(json);
            Collections.sort(postList);

            if(_postListUpdateListener != null) {
                _postListUpdateListener.onPostListLoaded(postList);
            }

            for (Post post : postList) {
                PostORM.insertPost(KWBApplication.this, post);
            }

            _lastLoadedPostList = new Date();
        }
    };
}
