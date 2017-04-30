package com.example.cobol.rozhodovac;

/**
 * Created by Cobol on 30.4.2017.
 */

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



public class Tab1Activity extends Fragment  {

    TextView textView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1, container, false);

        textView = (TextView) rootView.findViewById(R.id.tab1TextView);

        textView.setText(String.valueOf(DetailActivity.pokus));

        return rootView;
    }

}
