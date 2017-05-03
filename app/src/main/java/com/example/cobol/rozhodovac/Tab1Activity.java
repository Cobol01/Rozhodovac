package com.example.cobol.rozhodovac;

/**
 * Created by Cobol on 30.4.2017.
 */

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class Tab1Activity extends Fragment  {

    static ArrayAdapter arrayAdapter;

    // Pole pro předání pozice v tabulce
    static ArrayList<String> vyhodnoceniArray = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.vyhodnoceniListView);

        arrayAdapter = new ArrayAdapter(DetailActivity.context, android.R.layout.simple_list_item_1, vyhodnoceniArray);

        listView.setAdapter(arrayAdapter);

        nacteniDatabaze();

        return rootView;
    }

    public static void nacteniDatabaze (){
        try {
            Cursor cPol = MainActivity.databazeRozhodnuti.rawQuery(
                    "SELECT rowid, * FROM polozky WHERE idRoz =" + DetailActivity.idRozhodnuti, null);

            int idPolIndex = cPol.getColumnIndex("rowid");
            int nazevPolIndex = cPol.getColumnIndex("nazev");

            vyhodnoceniArray.clear();

            cPol.moveToFirst();
            while (cPol != null) {

                int idPol = cPol.getInt(idPolIndex);
                String nazev =  cPol.getString(nazevPolIndex);

                /*Cursor c = MainActivity.databazeRozhodnuti.rawQuery(
                        "SELECT nazev, vaha, vahaVlast " +
                                "FROM polozky " +
                                "INNER JOIN srovnani " +
                                "ON polozky.rowid = srovnani.idPol " +
                                "INNER JOIN vlastnosti " +
                                "ON srovnani.idVlast = vlastnosti.idVlast", null);*/

                Cursor c = MainActivity.databazeRozhodnuti.rawQuery(
                        "SELECT vaha, vahaVlast " +
                                "FROM srovnani " +
                                "INNER JOIN vlastnosti " +
                                "ON srovnani.idVlast = vlastnosti.idVlast " +
                                "WHERE idPol = " + idPol, null);

                // Nastavení indexů
                int vahaIndex = c.getColumnIndex("vaha");
                int vahaVlastIndex = c.getColumnIndex("vahaVlast");

                // Posunutí ukazatele na první položku
                c.moveToFirst();

                // Pročištění polí před naplněním

                int celkem = 0;

                while (c != null) {

                    int vaha = c.getInt(vahaIndex);
                    int vahaVlast = c.getInt(vahaVlastIndex);

                    celkem += vaha * vahaVlast;

                    Log.i ("celkem: ", String.valueOf(celkem));

                    if (c.moveToNext() == false) break;
                }

                vyhodnoceniArray.add(nazev + ": " + celkem);

                if (cPol.moveToNext() == false) break;

                //c.close();
            }
            cPol.close();

            arrayAdapter.notifyDataSetChanged();

        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
