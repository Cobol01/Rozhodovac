package com.example.cobol.rozhodovac;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PolozkyDetailActivity extends AppCompatActivity {

    String nazevPolozky;
    static int cisloPolozky;

    static ArrayAdapter arrayAdapter;

    // Pole pro výpis do ViewList
    static ArrayList<String> nazevVlastnosti = new ArrayList<>();

    // Pole pro předání pozice v tabulce
    static ArrayList<Integer> vahaVlastnosti = new ArrayList<>();

    // Pole pro předání pozice v tabulce
    static ArrayList<Integer> idSrov = new ArrayList<>();

    // VYtvoření zdroje dat pro adapter
    static ArrayList<Vyhodnoceni> poleVyhodnoceni = new ArrayList<Vyhodnoceni>();

    // Vytvoření adaptéru
    static MujAdapter adapter = new MujAdapter(DetailActivity.context, poleVyhodnoceni);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polozky_detail);

        Intent intent = getIntent();
        nazevPolozky = intent.getStringExtra("nazev");
        cisloPolozky = intent.getIntExtra("polozkaId", -1);

        TextView textView = (TextView) findViewById(R.id.nazePolozkyTextView);
        textView.setText(nazevPolozky);

        /*ListView listView = (ListView) findViewById(R.id.polozkyListView);

        arrayAdapter = new ArrayAdapter(DetailActivity.context, android.R.layout.simple_list_item_1, nazevVlastnosti);

        listView.setAdapter(arrayAdapter);*/

        ListView listView = (ListView) findViewById(R.id.polozkyListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent (DetailActivity.context, VlastnostDetailActivity.class);
                intent.putExtra("vlastnostId", idSrov.get(position));
                intent.putExtra("nazevVlastnosti", nazevVlastnosti.get(position));
                intent.putExtra("vahaVlastnosti", vahaVlastnosti.get(position));
                intent.putExtra("kdo", 2);


                startActivity(intent);
            }
        });

        nacteniDatabaze();
    }

    public static void nacteniDatabaze (){
        try {
            Cursor c = MainActivity.databazeRozhodnuti.rawQuery(
                    "SELECT nazevVlast, idSrov, hodnoc, idPol " +
                            "FROM srovnani INNER JOIN vlastnosti " +
                            "ON srovnani.idVlast = vlastnosti.idVlast " +
                            "WHERE idPol = " + cisloPolozky, null);

            // Nastavení indexů
            int idSrovIndex = c.getColumnIndex("idSrov");
            int hodnocIndex = c.getColumnIndex("hodnoc");
            int nazevVlastIndex = c.getColumnIndex("nazevVlast");

            // Posunutí ukazatele na první položku
            c.moveToFirst();

            //Log.i("nazevVlastIndex: ", String.valueOf(nazevVlastIndex));

            // Pročištění polí před naplněním
            idSrov.clear();
            vahaVlastnosti.clear();
            nazevVlastnosti.clear();
            adapter.clear();

            while (c != null){

                idSrov.add(c.getInt(idSrovIndex));

                vahaVlastnosti.add(c.getInt(hodnocIndex));

                nazevVlastnosti.add(c.getString(nazevVlastIndex));

                Vyhodnoceni noveVyhodnoceni = new Vyhodnoceni(c.getString(nazevVlastIndex),c.getInt(hodnocIndex));

                adapter.add(noveVyhodnoceni);

                if (c.moveToNext() == false) break;
            }

            adapter.notifyDataSetChanged();

            c.close();
            Tab1Activity.nacteniDatabaze();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
