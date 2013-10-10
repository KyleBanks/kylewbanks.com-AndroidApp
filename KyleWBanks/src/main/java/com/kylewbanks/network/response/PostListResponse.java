package com.kylewbanks.network.response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kylewbanks.model.Post;
import com.kylewbanks.network.RESTResponse;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kylewbanks on 2013-10-09.
 */
public class PostListResponse implements RESTResponse {

    protected List<Post> postList;

    @Override
    public void success(String json) {
        //"02/18/13 09:42 AM
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        Gson gson = gsonBuilder.create();

        postList = Arrays.asList(gson.fromJson(json, Post[].class));
    }

    @Override
    public void fail(Exception ex) {

    }
}
