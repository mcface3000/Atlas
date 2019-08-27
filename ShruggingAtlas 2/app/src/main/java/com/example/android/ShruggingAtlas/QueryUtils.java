package com.example.android.ShruggingAtlas;

import android.text.TextUtils;
import android.util.Log;

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

/**
 * Helper methods related to requesting and receiving golden state warrior data from Guardian API
 */
public final class QueryUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the Guardian Api dataset and return a list of Warrior objects.
     */
    public static List<Warrior> fetchWarriorData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Warrior}s
        List<Warrior> warriors = extractFeatureFromJson(jsonResponse);

        // Return the list of Warriors
        return warriors;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
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
            Log.e(LOG_TAG, "Problem retrieving the warrior JSON results.", e);
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
     * Convert the InputStream into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
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
     * Return a list of Warrior objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Warrior> extractFeatureFromJson(String warriorJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(warriorJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding warriors to
        List<Warrior> warriorArrayList = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(warriorJSON);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or warriors).
            JSONObject warriorResponse = baseJsonResponse.getJSONObject("response");

            JSONArray warriorArray = warriorResponse.getJSONArray("results");

            for (int i = 0; i < warriorArray.length(); i++) {
                Warrior warrior;
                //validate that I'm going through each object
                JSONObject eachObject = warriorArray.getJSONObject(i);
                String webPubDate;
                String authorName = "";

                if (warriorArray.toString().contains("webPublicationDate")); {
                    webPubDate = eachObject.getString("webPublicationDate");
                }
                if (warriorArray.toString().contains("authorName")) {
                    authorName = eachObject.getString("authorName");
                }
                String webTitle = eachObject.getString("webTitle");
                String sectionName = eachObject.getString("sectionName");
                String webUrl = eachObject.getString("webUrl");

                if (!webPubDate.isEmpty() && !authorName.equals("")) {
                    warrior = new Warrior(webTitle, sectionName, webUrl, webPubDate, authorName);
                    warriorArrayList.add(warrior);
                }

                if (authorName.equals("") && !webPubDate.isEmpty()) {
                    warrior = new Warrior(webTitle, sectionName, webUrl, webPubDate);
                    warriorArrayList.add(warrior);
                } else {
                    //Validate that it is creating an object with each of these
                    warrior = new Warrior(webTitle, sectionName, webUrl);
                    warriorArrayList.add(warrior);
                }
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the warrior JSON results", e);
        }

        // Return the list of warriors
        return warriorArrayList;
    }

}
