package com.kylewbanks;

import android.app.Application;
import android.util.Log;
import com.kylewbanks.activity.PostListInterface;
import com.kylewbanks.database.DatabaseWrapper;
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

                DatabaseWrapper wrapper = new DatabaseWrapper(KWBApplication.this);
                for (Post post : _postList) {
                    wrapper.insertPost(post);
                }
            }
        });
    }

    public void registerPostListInterface(PostListInterface postListInterface) {
        this._postListInterface = postListInterface;

        DatabaseWrapper databaseWrapper = new DatabaseWrapper(this);
        this._postList = databaseWrapper.getPosts();

        if(this._postList != null) {
            Collections.sort(_postList);
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
