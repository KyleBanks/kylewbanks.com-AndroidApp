package com.kylewbanks.event;

import com.kylewbanks.model.Post;

import java.util.List;

/**
 * Created by kylewbanks on 2013-10-09.
 */
public interface PostListUpdateListener {

    void onPostListLoaded(List<Post> posts);

}
