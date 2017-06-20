package com.example.cobol.rozhodovac;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Tab2Activity extends Fragment {

    private static final String TAG = "Tab2Activity";

    static ArrayAdapter arrayAdapter;

    EditText editText;

    // Pole pro výpis do ViewList
    static ArrayList<String> polozky = new ArrayList<>();

    // Pole pro předání pozice v tabulce
    static ArrayList<Integer> cisloPolozky = new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab2, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.tab2ListView);

        arrayAdapter = new ArrayAdapter(DetailActivity.context, R.layout.item_pol_vlast, polozky);

        listView.setAdapter(arrayAdapter);

        nacteniDatabaze(rootView);

        ImageButton imageButton = (ImageButton) rootView.findViewById(R.id.pridatPolozkuButton);

        editText = (EditText) rootView.findViewById(R.id.pridatPolozkuText);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editText.getText().toString().matches("")){
                    try {
                        ArrayList<String> idVlastnosti = new ArrayList<>();
                        //MainActivity.databazeRozhodnuti.execSQL("INSERT INTO polozky (nazev, idRoz) VALUES ('" + editText.getText() + "', " + DetailActivity.idRozhodnuti + ")");
                        ContentValues values = new ContentValues();
                        values.put("nazev", editText.getText().toString());
                        values.put("idRoz", DetailActivity.idRozhodnuti);
                        float ID;
                        ID = MainActivity.databazeRozhodnuti.insert("polozky",null,values);

                        if (ID != -1) {

                            // Vkládám vlastnosti do srovnávací tabulky
                            Cursor c = MainActivity.databazeRozhodnuti.rawQuery("SELECT idVlast FROM vlastnosti WHERE idRoz =" + DetailActivity.idRozhodnuti, null);

                            if (c.getCount() != 0) {

                                int rowidIndex = c.getColumnIndex("idVlast");

                                c.moveToFirst();

                                while (c != null) {
                                    MainActivity.databazeRozhodnuti.execSQL("INSERT INTO srovnani (idPol, idVlast, hodnoc) VALUES (" + ID + ", " + c.getInt(rowidIndex) + ", 5)");

                                    idVlastnosti.add(c.getString(rowidIndex));
                                    if (c.moveToNext() == false) break;
                                }

                            }

                            editText.setText(null);

                            MainActivity.arrayAdapter.notifyDataSetChanged();
                            nacteniDatabaze(rootView);
                            editText.setText(null);
                            Toast.makeText(DetailActivity.context, "Uloženo", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(DetailActivity.context, "Chyba při vlkádání", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent (DetailActivity.context, PolozkyDetailActivity.class);
                intent.putExtra("polozkaId", cisloPolozky.get(position));
                intent.putExtra("nazev", polozky.get(position));

                startActivity(intent);
            }
        });

        return rootView;
    }

    public static void nacteniDatabaze(View rootView) {
        try {
            // Načtení tabulky "rozhodnuti" včetně rowid
            Cursor c = MainActivity.databazeRozhodnuti.rawQuery("SELECT rowid, * FROM polozky WHERE idRoz =" + DetailActivity.idRozhodnuti, null);

            Log.i(TAG,"Kurzor" + c.toString());

            // Nastavení indexů
            int rowidIndex = c.getColumnIndex("rowid");
            int nazevIndex = c.getColumnIndex("nazev");

            // Posunutí ukazatele na první položku
            c.moveToFirst();

            // Pročištění polí před naplněním
            polozky.clear();
            cisloPolozky.clear();

            while (c != null){
                // Ukládá jméno do notes
                polozky.add(c.getString(nazevIndex));
                cisloPolozky.add(c.getInt(rowidIndex));

                if (c.moveToNext() == false) break;
            }

            arrayAdapter.notifyDataSetChanged();

            c.close();
            Tab1Activity.nacteniDatabaze();
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
