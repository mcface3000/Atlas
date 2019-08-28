package com.example.android.ShruggingAtlas;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

/**
 * An {@link WarriorAdapter} knows how to create a list item layout for each warrior
 * in the data source (a list of {@link Warrior} objects).
 *
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class WarriorAdapter extends ArrayAdapter<Warrior> {
    /**
     * Constructs a new {@link WarriorAdapter}.
     *
     * @param context of the app
     * @param warriors is the list of warriors, which is the data source of the adapter
     */
    public WarriorAdapter(Context context, List<Warrior> warriors) {
        super(context, 0, warriors);
    }

    /**
     * Returns a list item view that displays information about the warrior at the given position
     * in the list of warriors.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {

            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.warrior_list_item, parent, false);
        }

        // Find the warrior at the given position in the list of warriors
        Warrior currentWarrior = getItem(position);

        TextView webPubDate = listItemView.findViewById(R.id.webPubDate);
        String webPubDate1 = currentWarrior.getWebPubDate();
        if (webPubDate1 != null) {
            webPubDate.setText("Pub. Date: " + webPubDate1);
        }
        webPubDate.setText(currentWarrior.getAuthorName());
        String webPub = currentWarrior.getWebPubDate();
        webPubDate.setText("Web pub: " + webPub);

        // Find the TextView with view ID web title
        TextView webTitle = listItemView.findViewById(R.id.webTitle);
        String webTitleString = "Title: " + currentWarrior.getWebTitle();
        // Display the Web title of the current warrior in that TextView
        webTitle.setText(webTitleString);

        TextView sectionName = listItemView.findViewById(R.id.sectionName);
        String sectionName1 = "Section: " + currentWarrior.getSectionName();
        sectionName.setText(sectionName1);
        return listItemView;
    }
}
