package com.kylewbanks.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kylewbanks on 2013-10-09.
 */
public class Tag {

    @SerializedName("name")
    private String _name;

    public Tag() {

    }

    public Tag(String name) {
        this._name = name;
    }

    public String getName() {
        return this._name;
    }
    public void setName(String name) {
        this._name = name;
    }
}
