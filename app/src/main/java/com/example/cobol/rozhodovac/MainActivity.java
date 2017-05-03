package com.example.cobol.rozhodovac;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Pole pro výpis do ViewList
    static ArrayList<String> notes = new ArrayList<>();

    // Pole pro předání pozice v tabulce
    static ArrayList<Integer> poradi = new ArrayList<>();

    // Adaptér pro řizení ViewListu
    static ArrayAdapter arrayAdapter;

    static SQLiteDatabase databazeRozhodnuti;

    // Funkce pro otevření položky v nové aktivitě určené pro úpravy
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        // Spíš sem dám switch ;)

        if(item.getItemId() == R.id.smazat_vse){

            try {

                MainActivity.databazeRozhodnuti.execSQL("DROP TABLE IF EXISTS srovnani");
                MainActivity.databazeRozhodnuti.execSQL("DROP TABLE IF EXISTS vlastnosti");
                MainActivity.databazeRozhodnuti.execSQL("DROP TABLE IF EXISTS rozhodnuti");
                MainActivity.databazeRozhodnuti.execSQL("DROP TABLE IF EXISTS polozky");

                notes.clear();
                poradi.clear();

                arrayAdapter.notifyDataSetChanged();

                vytvoreniDatabazi();

            } catch (Exception e){
                e.printStackTrace();
            }


            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.cobol.rozhodovac", Context.MODE_PRIVATE);

        // Načtení nebo vytvoření databáze
        databazeRozhodnuti = this.openOrCreateDatabase("Rozhodnuti", MODE_PRIVATE, null);

        vytvoreniDatabazi();

        //tady se to bude volat
        nacteniDatabaze();

        Toast.makeText(getApplicationContext(), "Načteno", Toast.LENGTH_SHORT).show();

        ListView listView = (ListView) findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent (getApplicationContext(), DetailActivity.class);
                intent.putExtra("noteId", poradi.get(position));
                intent.putExtra("nazev", notes.get(position));

                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // Smaže záznam
                databazeRozhodnuti.execSQL("DELETE FROM rozhodnuti WHERE rowid = " + poradi.get(position));

                // Načte databázi a zaktualizuje podle ní ListView
                nacteniDatabaze();
                arrayAdapter.notifyDataSetChanged();

                // Musí vracet true, jinak se aktivuje i ClickListener
                return true;
            }
        });

        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(),NoteEditorActivity.class);
                startActivity(intent);

            }
        });
    }

    // Nachystané menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.hlavni_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public static void nacteniDatabaze(){
        try {
            // Načtení tabulky "rozhodnuti" včetně rowid
            Cursor c = databazeRozhodnuti.rawQuery("SELECT rowid, * FROM rozhodnuti", null);

            // Nastavení indexů
            int rowidIndex = c.getColumnIndex("rowid");
            int nazevIndex = c.getColumnIndex("nazev");
            int poradiIndex = c.getColumnIndex("poradi");

            // Posunutí ukazatele na první položku
            c.moveToFirst();

            // Pročištění polí před naplněním
            notes.clear();
            poradi.clear();

            while (c != null) {
                // Ukládá jméno do notes
                notes.add(c.getString(nazevIndex));

                // Ukládá číslo záznamu do poradi
                poradi.add(c.getInt(rowidIndex));

                // Kontroluje, zda již nejsme na konci
                if (c.moveToNext() == false) break;

                //if(!c.isLast()) c.moveToNext();
                //else c = null;
            }

            c.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void vytvoreniDatabazi(){
        try {
            MainActivity.databazeRozhodnuti.execSQL("CREATE TABLE IF NOT EXISTS rozhodnuti (nazev VARCHAR, poradi INT(3))");
            MainActivity.databazeRozhodnuti.execSQL("CREATE TABLE IF NOT EXISTS srovnani (idSrov integer primary key, idPol INT(3), idVlast INT(3), vaha INT(3))");
            MainActivity.databazeRozhodnuti.execSQL("CREATE TABLE IF NOT EXISTS polozky (nazev VARCHAR, idRoz INT(3))");
            MainActivity.databazeRozhodnuti.execSQL("CREATE TABLE IF NOT EXISTS vlastnosti (idVlast integer primary key, nazevVlast VARCHAR, idRoz INT(3), vahaVlast INT(3))");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}