package com.example.android.guardiannewsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by georgeD on 07/07/2017.
 */

public class ArticleLoader extends AsyncTaskLoader<ArrayList<Article>> {

    /**
     * Loads a list of articles by using an AsyncTask to perform the
     * network request to the given URL.
     */

    /** Tag for log messages */
    private static final String LOG_TAG = ArticleLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link ArticleLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public ArrayList<Article> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of articles.
        ArrayList<Article> articles = QueryUtils.fetchArticleData(mUrl);
        return articles;
    }

}
