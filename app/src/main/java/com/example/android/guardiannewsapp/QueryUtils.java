package com.example.android.guardiannewsapp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.media.MediaFormat.KEY_LANGUAGE;

/**
 * Created by georgeD on 07/07/2017.
 */

public class QueryUtils {


    /** Keys for JSON parsing */
    private static final String RESPONSE = "response";
    private static final String RESULTS = "results";
    private static final String SECTION = "sectionName";
    private static final String DATE = "webPublicationDate";
    private static final String TITLE = "webTitle";
    private static final String WEB_URL = "webUrl";

    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the Google dataset and return a list of {@link Article} objects.
     */
    public static ArrayList<Article> fetchArticleData(String requestUrl) {
        Log.i(LOG_TAG, "fetching Article data");
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link articles}s
        ArrayList<Article> articles = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link article}s
        return articles;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        Log.i(LOG_TAG, "create URL");
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        Log.i(LOG_TAG, "make HTTP request");
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Guardian JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        Log.i(LOG_TAG, "Respone to string");
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Article} objects that has been built up from
     * parsing the given JSON response.
     */
    private static ArrayList<Article> extractFeatureFromJson(String ArticleJSON) {
        Log.i(LOG_TAG, "extract from JSON");
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(ArticleJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding articles to
        ArrayList<Article> articles = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(ArticleJSON);
            JSONObject responseJSONObject = baseJsonResponse.getJSONObject(RESPONSE);
            JSONArray ArticleResults = responseJSONObject.getJSONArray(RESULTS);

            // Variables for JSON parsing
            String section;
            String publicationDate;
            String title;
            String webUrl;

            for (int i = 0; i < ArticleResults.length(); i++ ) {
                JSONObject newsArticle = ArticleResults.getJSONObject(i);
                // Check if a sectionName exists
                if (newsArticle.has(SECTION)) {
                    section = newsArticle.getString(SECTION);
                } else section = null;

                // Check if a webPublicationDate exists
                if (newsArticle.has(DATE)) {
                    publicationDate = newsArticle.getString(DATE);
                } else publicationDate = null;

                // Check if a webTitle exists
                if (newsArticle.has(TITLE)) {
                    title = newsArticle.getString(TITLE);
                } else title = null;

                // Check if a sectionName exists
                if (newsArticle.has(WEB_URL)) {
                    webUrl = newsArticle.getString(WEB_URL);
                } else webUrl = null;

                // Create the NewsItem object and add it to the articles List.
                Article newsItem = new Article(title, section, publicationDate, webUrl);
                articles.add(newsItem);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.

            Log.e(LOG_TAG, "Problem parsing the Guardian JSON results", e);
        }

        // Return the list of articles
        return articles;
    }

}
