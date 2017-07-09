package com.example.android.guardiannewsapp;

/**
 * Created by georgeD on 07/07/2017.
 */

public class Article {

    /** title of the article */
    private String mTitle;

    /** section of the article */
    private String mSection;

    /** published date and time of the article */
    private String mTime;

    /** Website URL of the article */
    private String mUrl;

    /**
     * Constructs a new {@link Article} object.
     *
     * @param title is the title of the article
     * @param section is the section the article belongs
     * @param time is the date and time that article is published
     * @param url is the website URL to find more details about the article
     */

    public Article(String title, String section, String time, String url) {
        mTitle = title;
        mSection = section;
        mTime = time;
        mUrl = url;
    }

    /**
     * Returns the title of the article
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Returns the section of the article
     */
    public String getSection() {
        return mSection;
    }


    /**
     * Returns the date and time of the article
     */
    public String getTime() {
        return mTime;
    }

    /**
     * Returns the website URL to find more information about the article
     */
    public String getUrl() {
        return mUrl;
    }
}
