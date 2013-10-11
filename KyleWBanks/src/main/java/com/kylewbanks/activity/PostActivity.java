package com.kylewbanks.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.kylewbanks.KWBApplication;
import com.kylewbanks.R;
import com.kylewbanks.model.Post;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Scanner;

/**
 * Created by kylewbanks on 2013-10-09.
 */
public class PostActivity extends Activity {

    private static final String TAG = "PostActivity";

    private Post post;
    private WebView contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_view);

        //Determine which post we are showing
        KWBApplication application = (KWBApplication) getApplication();
        Intent intent = getIntent();
        post = application.getPostById(intent.getLongExtra("postId", -1));

        //Customize the action bar
        ActionBar actionBar = getActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(post.getTitle());
            actionBar.setDisplayUseLogoEnabled(false);
        }

        //Get references to needed UI components
        contentView = (WebView) findViewById(R.id.post_content);
        WebSettings settings = contentView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        settings.setJavaScriptEnabled(true);
        contentView.loadData(getTemplateContent().replace("{{CONTENT}}", post.getURLEncodedBody()), "text/html", "UTF-8");
    }

    /**
     * Called when the physical 'Back Button' is pressed. Overridden to implement custom animation.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    /**
     * Called when the navigation 'Back Button' is pressed (header bar). Overridden to implement custom animation.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Reads in the contents of post_content.html and returns it as a String
     * @return
     */
    private String getTemplateContent() {
        AssetManager assetManager = getAssets();
        try {
            InputStream contentStream = assetManager.open("post_content.html");

            Scanner scanner = new Scanner(contentStream).useDelimiter("\\A");
            if(scanner.hasNext()) {
                return scanner.next();
            }
        } catch (IOException ex) {
            Log.i(TAG, "Failed to get Template content due to: " + ex);
        }

        return null;
    }
}
