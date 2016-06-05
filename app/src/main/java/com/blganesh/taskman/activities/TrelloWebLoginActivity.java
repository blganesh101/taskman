package com.blganesh.taskman.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.blganesh.taskman.R;
import com.blganesh.taskman.TaskManMainActivity;
import com.blganesh.taskman.users.TrelloKey;
import com.blganesh.taskman.trello.ExclusiveLayout;

/**
 * Created by ganeshbanda on 05/06/16.
 */
public final class TrelloWebLoginActivity extends TaskManMainActivity {
    private ExclusiveLayout mExclusiveLayout;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trello_login_activity);
        setupToolbar();
        mExclusiveLayout = (ExclusiveLayout) findViewById(R.id.exclusive_layout);

        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new Client());
        String url = new Uri.Builder()
                .scheme("https")
                .authority("trello.com")
                .path("1/authorize")
                .appendQueryParameter("key", TrelloKey.getAppKey())
                .appendQueryParameter("name", "TaskMan")
                .appendQueryParameter("response_type", "token")
                .appendQueryParameter("scope", "read,write")
                .appendQueryParameter("return_url", "https://developers.trello.com")
                .appendQueryParameter("callback_method", "fragment")
                .appendQueryParameter("expiration", "never")
                .build()
                .toString();
        mWebView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private final class Client extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("https://developers.trello.com/#token=")) {
                String token = url.substring(url.indexOf("=") + 1);

                if (!TrelloKey.persistToken(view.getContext(), token)) {
                    Toast.makeText(view.getContext(), R.string.error_trello_login, Toast.LENGTH_SHORT).show();
                }

                if (TrelloLoginActivity.trelloFullySetup(TrelloWebLoginActivity.this)) {
                    startActivity(new Intent(TrelloWebLoginActivity.this, TrelloPomoActivity.class));
                } else {
                    startActivity(new Intent(TrelloWebLoginActivity.this, TrelloLoginActivity.class));
                }

                TrelloWebLoginActivity.this.finish();
            }

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mExclusiveLayout.showFirst();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            mExclusiveLayout.showLast();
            super.onPageFinished(view, url);
        }
    }
}

