package com.kylewbanks.network;

import android.os.AsyncTask;
import android.util.Log;
import com.kylewbanks.network.response.PostListResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kylewbanks on 2013-10-09.
 */
public class RESTController {

    private static final String TAG = "RESTController";

    private static final String REST_URL = "http://kylewbanks.com/rest.json";


    /**
     * Fetches a list of Posts from the remote server.
     * @param response
     */
    public static void retrievePostList(PostListResponse response, long[] knownPostIds) {
        Log.i(TAG, "Loading Post List...");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("knownPostIds", Arrays.toString(knownPostIds));

        RESTPerformer restPerformer = new RESTPerformer();
        restPerformer.execute(getPackedParameters("/postList", params, response));
    }

    /**
     * Bundles any additional parameters you want to pass to the server with the authentication parameters.
     * Also adds the required RESTResponse callback class, and server URL to the dictionary.
     */
    private static HashMap<String, Object> getPackedParameters(String serverURL, Map<String, Object> additionalParams, RESTResponse response) {
        HashMap<String, Object> packedParams = new HashMap<String, Object>();

        if(!serverURL.startsWith("/")) {
            serverURL = "/" + serverURL;
        }
        packedParams.put(RESTPerformer.SERVER_URL, REST_URL + serverURL);

        if(additionalParams != null) {
            packedParams.putAll(additionalParams);
        }
        packedParams.put(RESTPerformer.CALLBACK_CLASS, response);

        return packedParams;
    }


    /**
     * Performs the REST calls asynchronously, and executes the appropriate callback functionality.
     */
    private static class RESTPerformer extends AsyncTask<HashMap<String, Object>, Void, String>
    {
        private static final String TAG = "RESTPerformer";

        public static final String CALLBACK_CLASS = "CallbackClass";
        public static final String SERVER_URL = "ServerURL";

        /**
         * Takes and InputStream and reads it's contents into a String.
         * @param is
         * @return
         */
        private static String convertStreamToString(java.io.InputStream is) {
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }

        /**
         * Performs the actual HTTP request, in a background thread.
         * @param params
         * @return
         */
        @Override
        protected String doInBackground(HashMap<String, Object>... params) {
            //Retrieve the callback class and remove it from the map
            RESTResponse callback;
            if(params[0].containsKey(CALLBACK_CLASS)) {
                callback = (RESTResponse) params[0].get(CALLBACK_CLASS);
                params[0].remove(CALLBACK_CLASS);
            } else {
                callback = new RESTResponse() {
                    @Override
                    public void success(String json) {
                        Log.e(TAG, "Callback not implemented!");
                    }
                    @Override
                    public void fail(Exception ex) {
                        Log.e(TAG, "Callback not implemented!");
                    }
                };
            }

            //Retrieve the Server URL and remove it from the map
            String serverURL;
            if(params[0].containsKey(SERVER_URL)) {
                serverURL = params[0].get(SERVER_URL).toString();
                params[0].remove(SERVER_URL);
            } else {
                Log.e(TAG, "No server URL provided.");
                callback.fail(new Exception("No server URL provided"));
                return null;
            }

            //Generate the POST parameters
            StringBuilder builder = new StringBuilder(serverURL + "?");
            MultipartEntity mpEntity = new MultipartEntity();
            try {
                for(Map.Entry<String, Object> entry : params[0].entrySet()) {
                    builder.append(entry.getKey() + "=" + entry.getValue() + "&");
                    mpEntity.addPart(entry.getKey(), new StringBody(entry.getValue().toString()));
                }
            } catch (Exception ex) {
                callback.fail(ex);
                return null;
            }
            Log.i(TAG, "Requesting: " + builder.toString());

            try {
                //POST the parameters to the server, and retrieve the response
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(serverURL);
                post.setEntity(mpEntity);

                HttpResponse response = client.execute(post);
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();

                    try {
                        callback.success(RESTPerformer.convertStreamToString(content));
                    } catch (Exception ex) {
                        Log.e(TAG, "Failed to parse JSON due to: " + ex);
                        callback.fail(ex);
                    }
                    content.close();
                } else {
                    Log.e(TAG, "Server responded with status code: " + statusLine.getStatusCode());
                    callback.fail(new Exception("Server responded with response code: " + statusLine.getStatusCode()));
                }
            } catch(Exception ex) {
                Log.e(TAG, "Failed to send HTTP POST request due to: " + ex);
                callback.fail(ex);
            }
            return null;
        }

    }

}
