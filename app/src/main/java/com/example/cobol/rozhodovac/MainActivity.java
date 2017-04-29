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

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;

    static SQLiteDatabase databazeRozhodnuti;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        //super.onOptionsItemSelected(item);


        if(item.getItemId() == R.id.add_note){

            Intent intent = new Intent(getApplicationContext(),NoteEditorActivity.class);

            startActivity(intent);

            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databazeRozhodnuti = this.openOrCreateDatabase("Rozhodnuti", MODE_PRIVATE, null);

        //databazeRozhodnuti.execSQL("DROP TABLE rozhodnuti ");

        /*databazeRozhodnuti.execSQL("CREATE TABLE IF NOT EXISTS rozhodnuti (nazev VARCHAR, poradi INT(3))");
        databazeRozhodnuti.execSQL("INSERT INTO rozhodnuti (nazev, poradi) VALUES ('Prvni', 1)");
        databazeRozhodnuti.execSQL("INSERT INTO rozhodnuti (nazev, poradi) VALUES ('Druhy', 2)");*/

        Cursor c = databazeRozhodnuti.rawQuery("SELECT * FROM rozhodnuti", null);

        int nazevIndex = c.getColumnIndex("nazev");
        int poradiIndex = c.getColumnIndex("poradi");

        c.moveToFirst();


        while (c != null){
            notes.add(c.getString(nazevIndex));
            if(!c.isLast()) c.moveToNext();
            else c = null;
        }


        ListView listView = (ListView) findViewById(R.id.listView);

        //notes.add("A další");

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent (getApplicationContext(), NoteEditorActivity.class);

                intent.putExtra("noteId", position);

                startActivity(intent);


            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                notes.remove(position);
                arrayAdapter.notifyDataSetChanged();

                return false;
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
}