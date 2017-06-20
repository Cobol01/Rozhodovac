package com.example.cobol.rozhodovac;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MujAdapter extends ArrayAdapter<Vyhodnoceni> {
    public MujAdapter(Context context, ArrayList<Vyhodnoceni> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Vyhodnoceni vyhodnoceni = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_vyhodnoceni, parent, false);
        }

        // Lookup view for data population
        TextView vyNazev = (TextView) convertView.findViewById(R.id.vyNazev);
        TextView vyScore = (TextView) convertView.findViewById(R.id.vyScore);

        // Populate the data into the template view using the data object
        vyNazev.setText(vyhodnoceni.polozka);
        vyScore.setText(vyhodnoceni.score);

        // Return the completed view to render on screen
        return convertView;
    }
}
