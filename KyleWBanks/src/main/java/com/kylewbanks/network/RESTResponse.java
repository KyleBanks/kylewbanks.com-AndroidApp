package com.kylewbanks.network;

/**
 * Created by kylewbanks on 2013-10-09.
 */
public interface RESTResponse {

    void success(String json);

    void fail(Exception ex);

}
