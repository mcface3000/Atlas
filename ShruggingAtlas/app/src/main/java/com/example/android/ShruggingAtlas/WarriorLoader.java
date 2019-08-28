package com.example.android.ShruggingAtlas;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Loads a list of warriors by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class WarriorLoader extends AsyncTaskLoader<List<Warrior>> {

    /** Tag for log messages */
    private static final String LOG_TAG = WarriorLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link WarriorLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public WarriorLoader(Context context, String url) {
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
    public List<Warrior> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of warriors.
        List<Warrior> warriors = QueryUtils.fetchWarriorData(mUrl);
        return warriors;
    }
}
