package com.kylewbanks.view.adapter;

import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import com.kylewbanks.R;
import com.kylewbanks.model.Post;

import java.io.InputStream;

/**
 * Created by kylewbanks on 2013-10-09.
 */
public class PostListItem {

    private Post post;

    public PostListItem(Post post) {
        this.post = post;
    }

    /**
     * Populates a View with the Title and Preview of a Post. The view must have TextViews with the IDs post_item_title
     * and post_item_preview in order to be populated.
     *
     * @param view
     */
    public void bindToView(View view) {
        TextView txtTitle = (TextView) view.findViewById(R.id.post_item_title);
        if(txtTitle != null) {
            txtTitle.setText(post.getTitle());
        }

        TextView txtPreview = (TextView) view.findViewById(R.id.post_item_preview);
        if(txtPreview != null) {
            txtPreview.setText(Html.fromHtml(post.getPreview()));
        }
    }
}
