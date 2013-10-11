package com.kylewbanks.network;

/**
 * Created by kylewbanks on 2013-10-09.
 */
public interface RESTResponse {

    /**
     * Called upon successful completion of a HTTP request with the JSON that the server responded with.
     *
     * @param json
     */
    void success(String json);

    /**
     * Called if the HTTP request fails for any reason.
     *
     * @param ex
     */
    void fail(Exception ex);

}
