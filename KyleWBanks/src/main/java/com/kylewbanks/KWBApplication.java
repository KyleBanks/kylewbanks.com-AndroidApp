package com.kylewbanks;

import android.app.Application;
import android.util.Log;
import com.kylewbanks.activity.PostListInterface;
import com.kylewbanks.model.Post;
import com.kylewbanks.network.RESTController;
import com.kylewbanks.network.response.PostListResponse;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by kylewbanks on 2013-10-09.
 */
public class KWBApplication  extends Application {

    private static final String TAG = "KWBApplication";

    private List<Post> _postList;
    private PostListInterface _postListInterface;

    public void loadPostList() {
        Log.i(TAG, "Loading Post List....");
        RESTController.retrievePostList(new PostListResponse() {
            @Override
            public void success(String json) {
                super.success(json);
                _postList = postList;

                Collections.sort(_postList);

                if(_postListInterface != null) {
                    _postListInterface.onPostListLoaded(_postList);
                }
            }
        });
    }

    public void registerPostListInterface(PostListInterface postListInterface) {
        this._postListInterface = postListInterface;

        if(this._postList != null) {
            this._postListInterface.onPostListLoaded(this._postList);
        } else {
            this.loadPostList();
        }
    }

    public Post getPostById(long id) {
        for (Post post : _postList) {
            if (post.getId() == id) {
                return post;
            }
        }

        return null;
    }
}
