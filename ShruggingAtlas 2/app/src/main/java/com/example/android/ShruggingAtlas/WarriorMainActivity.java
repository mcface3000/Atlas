package com.example.android.ShruggingAtlas;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class WarriorMainActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Warrior>> {

    private static final String LOG_TAG = WarriorMainActivity.class.getName();

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    private static final String test_url = "https://content.guardianapis.com/search";

    /**
     * Constant value for the warrior loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int WARRIOR_LOADER_ID = 1;

    /**
     * Adapter for the list of warriors
     */
    private WarriorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warrior_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView warriorListView = findViewById(R.id.list);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        warriorListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of warriors as input
        mAdapter = new WarriorAdapter(this, new ArrayList<Warrior>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        warriorListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected warrior.
        warriorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current warrior that was clicked on
                Warrior currentWarrior = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri warriorUri = Uri.parse(currentWarrior.getUrl());

                // Create a new intent to view the warrior URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, warriorUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(WARRIOR_LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<List<Warrior>> onCreateLoader(int i, Bundle bundle) {
        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(test_url);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `format=geojson`
        uriBuilder.appendQueryParameter("q", "golden state warriors");

        uriBuilder.appendQueryParameter("api-key", "bfc40f82-156c-4946-8c58-1016c5e87d8c");

        // Create a new loader for the given URL
        return new WarriorLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Warrior>> loader, List<Warrior> warriors) {
        // Set empty state text to display "No warriors found."
        mEmptyStateTextView.setText("No text found");

        // Clear the adapter of previous warrior data
        mAdapter.clear();

        // If there is a valid list of {@link Warrior}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (warriors != null && !warriors.isEmpty()) {
            mAdapter.addAll(warriors);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Warrior>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}
