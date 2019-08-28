
package com.example.android.ShruggingAtlas;

import android.util.Log;

public class Warrior {

    private String url;

    private String webTitle;

    private String sectionName;

    private String webPubDate;

    private String authorName;

    public Warrior(String webTitle, String sectionName, String url) {
        this.webTitle = webTitle;
        this.sectionName = sectionName;
        this.url = url;
    }

    public Warrior(String webTitle, String sectionName, String url, String webPubDate) {
        this.webTitle = webTitle;
        this.sectionName = sectionName;
        this.url = url;
        this.webPubDate = webPubDate;
    }

    public Warrior(String webTitle, String sectionName, String url, String webPubDate, String authorName) {
        this.webTitle = webTitle;
        this.sectionName = sectionName;
        this.url = url;
        this.webPubDate = webPubDate;
        this.authorName = authorName;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getWebPubDate() {
        Log.i("Web pub", "Web pub from object: " + webPubDate);
        return webPubDate;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getUrl() {
        return url;
    }
}
