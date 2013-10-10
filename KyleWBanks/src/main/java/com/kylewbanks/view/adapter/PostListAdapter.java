package com.kylewbanks.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.kylewbanks.model.Post;

import java.util.List;

/**
 * Created by kylewbanks on 2013-10-09.
 */
public class PostListAdapter extends ArrayAdapter<Post> {

    private int viewResourceId;
    private LayoutInflater inflater;
    private List<Post> postList;

    public PostListAdapter(Context context, int viewResourceId, List<Post> postList) {
        super(context, viewResourceId, postList);

        this.viewResourceId = viewResourceId;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.postList = postList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position > postList.size()) {
            return null;
        }

        // Don't reuse existing views because the WebView widget doesn't seem to reduce
        // its size properly once its been used for a longer preview
        View view = inflater.inflate(viewResourceId, parent, false);

        PostListItem postListItem = new PostListItem(postList.get(position));
        postListItem.bindToView(view);

        return view;
    }
}
