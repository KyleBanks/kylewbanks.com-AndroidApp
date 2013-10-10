package com.kylewbanks.activity;

import com.kylewbanks.model.Post;

import java.util.List;

/**
 * Created by kylewbanks on 2013-10-09.
 */
public interface PostListInterface {

    void onPostListLoaded(List<Post> posts);

}
