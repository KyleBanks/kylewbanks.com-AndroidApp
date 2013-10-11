package com.kylewbanks.model;

import android.util.Log;
import com.google.gson.annotations.SerializedName;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * Created by kylewbanks on 2013-10-09.
 */
public class Post implements Comparable<Post> {

    private static final String TAG = "Post";

    @SerializedName("id")
    private long _id;

    @SerializedName("title")
    private String _title;
    @SerializedName("preview")
    private String _preview;
    @SerializedName("body")
    private String _body;
    @SerializedName("url")
    private String _url;

    @SerializedName("date")
    private Date _date;

    @SerializedName("tags")
    private List<Tag> _tags;

    public Post() {

    }

    public Post(long id, String title, String preview, String body, String url, Date date, List<Tag> tags) {
        this._id = id;
        this._title = title;
        this._preview = preview;
        this._body = body;
        this._url = url;
        this._date = date;
        this._tags = tags;
    }

    /**
     * Returns the body of the Post in a way that is better handled by WebViews.
     * @return
     */
    public String getURLEncodedBody() {
        try {
            return URLEncoder.encode(_body, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException ex) {
            Log.i(TAG, "Failed to encode Post body due to: " + ex);
            return null;
        }
    }


    public long getId() {
        return _id;
    }

    public void setId(long id) {
        this._id = id;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        this._title = title;
    }

    public String getPreview() {
        return _preview;
    }

    public void setPreview(String preview) {
        this._preview = preview;
    }

    public String getBody() {
        return _body;
    }

    public void setBody(String body) {
        this._body = body;
    }

    public String getUrl() {
        return _url;
    }

    public void setUrl(String url) {
        this._url = url;
    }

    public Date getDate() {
        return this._date;
    }

    public void setDate(Date date) {
        this._date = date;
    }

    public List<Tag> getTags() {
        return _tags;
    }

    public void setTags(List<Tag> tags) {
        this._tags = tags;
    }

    @Override
    public int compareTo(Post post) {
        return post.getDate().compareTo(_date);
    }
}
