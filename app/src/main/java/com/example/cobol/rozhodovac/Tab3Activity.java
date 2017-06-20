package com.example.cobol.rozhodovac;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import android.widget.Toast;

import java.util.ArrayList;

public class Tab3Activity extends Fragment {

    private static final String TAG = "Tab3Activity";

    static ArrayAdapter arrayAdapter;

    // Pole pro výpis do ViewList
    static ArrayList<String> vlastnosti = new ArrayList<>();

    // Pole pro předání pozice v tabulce
    static ArrayList<Integer> cisloVlastnosti = new ArrayList<>();

    // Pole pro předání pozice v tabulce
    static ArrayList<Integer> vahaVlastnosti = new ArrayList<>();

    // VYtvoření zdroje dat pro adapter
    static ArrayList<Vyhodnoceni> poleVyhodnoceni = new ArrayList<Vyhodnoceni>();

    // Vytvoření adaptéru
    static MujAdapter adapter = new MujAdapter(DetailActivity.context, poleVyhodnoceni);

    EditText editText;

    static View mRootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab3, container, false);

        /*
        ListView listView = (ListView) rootView.findViewById(R.id.tab3ListView);

        arrayAdapter = new ArrayAdapter(DetailActivity.context, R.layout.item_pol_vlast, vlastnosti);

        listView.setAdapter(arrayAdapter);*/

        mRootView = rootView;

        ListView listView = (ListView) rootView.findViewById(R.id.vlastnostiListView);
        listView.setAdapter(adapter);

        nacteniDatabaze(rootView);

        ImageButton imageButton = (ImageButton) rootView.findViewById(R.id.pridatVlastnostButton);

        editText = (EditText) rootView.findViewById(R.id.pridatVlastnostText);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editText.getText().toString().matches("")){
                    try {
                        Log.i(TAG,"Zacatek vlozeni vlastnosti");
                        ContentValues values = new ContentValues();
                        values.put("nazevVlast", editText.getText().toString());
                        values.put("idRoz", DetailActivity.idRozhodnuti);
                        values.put("vahaVlast", 5);
                        float ID;
                        ID = MainActivity.databazeRozhodnuti.insert("vlastnosti",null,values);

                        Log.i(TAG,"ID po vlozeni: " + ID);

                        /*MainActivity.databazeRozhodnuti.execSQL("INSERT INTO vlastnosti (nazevVlast, idRoz, vahaVlast) VALUES ('" + editText.getText() + "', " + DetailActivity.idRozhodnuti + ", 5)");*/

                        if (ID != -1) {

                            Cursor c = MainActivity.databazeRozhodnuti.rawQuery("SELECT rowid FROM polozky WHERE idRoz =" + DetailActivity.idRozhodnuti, null);

                            Log.i(TAG,"po nacteni polozek");

                            if (c.getCount() != 0) {

                                int rowidIndex = c.getColumnIndex("rowid");

                                c.moveToFirst();

                                while (c != null) {
                                    MainActivity.databazeRozhodnuti.execSQL("INSERT INTO srovnani (idPol, idVlast, hodnoc) VALUES (" + c.getInt(rowidIndex) + ", " + ID + ", 5)");

                                    if (c.moveToNext() == false) break;
                                }
                            }

                            Toast.makeText(DetailActivity.context, "Uloženo", Toast.LENGTH_SHORT).show();
                            nacteniDatabaze(rootView);
                            MainActivity.arrayAdapter.notifyDataSetChanged();
                            editText.setText(null);

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

                Intent intent = new Intent (DetailActivity.context, VlastnostDetailActivity.class);
                intent.putExtra("vlastnostId", cisloVlastnosti.get(position));
                intent.putExtra("nazevVlastnosti", vlastnosti.get(position));
                intent.putExtra("vahaVlastnosti", vahaVlastnosti.get(position));
                intent.putExtra("kdo", 1);

                startActivity(intent);
            }
        });

        return rootView;
    }

    public static void nacteniDatabaze(View rootView) {

        try {
            // Načtení tabulky "rozhodnuti" včetně rowid
            Cursor c = MainActivity.databazeRozhodnuti.rawQuery("SELECT idVlast, * FROM vlastnosti WHERE idRoz =" + DetailActivity.idRozhodnuti, null);

            // Nastavení indexů
            int rowidIndex = c.getColumnIndex("idVlast");
            int nazevIndex = c.getColumnIndex("nazevVlast");
            int idRozIndex = c.getColumnIndex("idRoz");
            int vahaIndex = c.getColumnIndex("vahaVlast");

            // Posunutí ukazatele na první položku
            c.moveToFirst();

            // Pročištění polí před naplněním
            vlastnosti.clear();
            cisloVlastnosti.clear();
            vahaVlastnosti.clear();

            adapter.clear();


            while (c != null) {

                Log.i(TAG, "nazevIndex: " + String.valueOf(c.getCount()));
                Log.i(TAG,"vahaIndex: " +  String.valueOf(c.getPosition()));

                // Ukládá jméno do vlastnosti
                vlastnosti.add(c.getString(nazevIndex));

                // Ukládá váhu do vahaVlastnosti
                vahaVlastnosti.add(c.getInt(vahaIndex));

                // Ukládá pozici do vahaVlastnosti
                cisloVlastnosti.add(c.getInt(rowidIndex));

                Vyhodnoceni noveVyhodnoceni = new Vyhodnoceni(c.getString(nazevIndex),c.getInt(vahaIndex));

                adapter.add(noveVyhodnoceni);

                if (c.isLast() == true) {
                    //Log.i(TAG,"jeposledni");
                    break;
                }
                else c.moveToNext();
            }

            adapter.notifyDataSetChanged();

            //Log.i(TAG,"");

            c.close();

            Tab1Activity.nacteniDatabaze();
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}