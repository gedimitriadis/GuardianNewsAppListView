package com.example.android.guardiannewsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static java.security.AccessController.getContext;

/**
 * Created by georgeD on 07/07/2017.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {

    // Constructs an ArticleAdapter.
    // articles is the list of articles, which is the data source of the adapter
    public ArticleAdapter(Context context, ArrayList<Article> articles) {
        super(context,0, articles);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new article list item layout.
        View articleView = convertView;
        if (articleView == null) {
            articleView = LayoutInflater.from(getContext()).inflate(
                    R.layout.article_list_item, parent, false);
        }

        // Find the article at the given position in the list of articles
        Article currentArticle = getItem(position);

        // Find the TextView for article title
        TextView title_TextView = (TextView) articleView.findViewById(R.id.articleTitle);
        // Find the TextView for article section
        TextView section_TextView = (TextView) articleView.findViewById(R.id.articleSection);
        // Find the TextView for article date
        TextView date_TextView = (TextView) articleView.findViewById(R.id.articleDate);
        // Find the TextView for article time
        TextView time_TextView = (TextView) articleView.findViewById(R.id.articleTime);


        // Display the article title in that TextView
        title_TextView.setText(currentArticle.getTitle());

        // Display the section of the article in that TextView
        section_TextView.setText(currentArticle.getSection());

        // gets the date and time string
        String sourceString = currentArticle.getTime();

        // sets the date of article
        date_TextView.setText(FormattedDate(sourceString));
        // sets the time of article
        time_TextView.setText(FormattedTime(sourceString));

        // Return the list item view that is now showing the appropriate data
        return articleView;
    }

    // gets the date from the string of date and time
    public String FormattedDate(String sourceString){
        String substrDate = sourceString.substring(0,10);
        return substrDate;
    }

    // gets the time from the string of date and time
    public String FormattedTime(String sourceString){
        String substrTime=sourceString.substring(sourceString.indexOf("T") + 4);
        substrTime = substrTime.substring(0, substrTime.length() - 1);
        return substrTime;
    }

}
