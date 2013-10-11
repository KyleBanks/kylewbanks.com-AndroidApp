package com.kylewbanks;

import android.app.Application;
import android.util.Log;
import com.kylewbanks.event.PostListUpdateListener;
import com.kylewbanks.database.orm.PostORM;
import com.kylewbanks.model.Post;
import com.kylewbanks.network.RESTController;
import com.kylewbanks.network.response.PostListResponse;

import java.util.Collections;
import java.util.List;

/**
 * Created by kylewbanks on 2013-10-09.
 */
public class KWBApplication  extends Application {

    private static final String TAG = "KWBApplication";

    private List<Post> _postList;
    private PostListUpdateListener _postListUpdateListener;

    /**
     * Register a listener for when the Post list becomes available, or is updated
     * @param postListUpdateListener
     */
    public void registerPostListUpdateListener(PostListUpdateListener postListUpdateListener) {
        this._postListUpdateListener = postListUpdateListener;

        this._postList = PostORM.getPosts(this);

        if(this._postList != null) {
            Collections.sort(_postList);
            this._postListUpdateListener.onPostListLoaded(this._postList);
        } else {
            this.loadPostList();
        }
    }

    /**
     * Returns a Post object identified by the specified id, if available
     * @param id
     * @return
     */
    public Post getPostById(long id) {
        for (Post post : _postList) {
            if (post.getId() == id) {
                return post;
            }
        }

        return null;
    }

    /**
     * Fetches the list of Posts from the remove server
     */
    private void loadPostList() {
        Log.i(TAG, "Loading Post List....");

        RESTController.retrievePostList(postListResponse);
    }

    /**
     * Handler for the Post list being fetched remotely
     */
    private PostListResponse postListResponse = new PostListResponse() {
        @Override
        public void success(String json) {
            super.success(json);
            _postList = postList;
            Collections.sort(_postList);

            if(_postListUpdateListener != null) {
                _postListUpdateListener.onPostListLoaded(_postList);
            }

            for (Post post : _postList) {
                PostORM.insertPost(KWBApplication.this, post);
            }
        }
    };
}
