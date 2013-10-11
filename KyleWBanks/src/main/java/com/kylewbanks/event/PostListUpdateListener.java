package com.kylewbanks.event;

import com.kylewbanks.model.Post;

import java.util.List;

/**
 * Created by kylewbanks on 2013-10-09.
 */
public interface PostListUpdateListener {

    /**
     * Called when the Post list is made available, or has been updated
     * @param posts
     */
    void onPostListLoaded(List<Post> posts);

}
