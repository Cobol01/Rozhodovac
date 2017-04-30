package com.example.cobol.rozhodovac;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NoteEditorActivity extends AppCompatActivity {

    int noteId;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        Button ulozButton = (Button) findViewById(R.id.ulozitButton);
        Button zrusButton = (Button) findViewById(R.id.zrusitButton);

        editText = (EditText) findViewById(R.id.editText);

        Intent intent = getIntent();

        noteId = intent.getIntExtra("noteId", -1);

        Log.i("muj_noteId: ",String.valueOf(noteId));

        if (noteId != -1){
            //editText.setText(MainActivity.notes.get(noteId));
            //noteId ++;
            Cursor c = MainActivity.databazeRozhodnuti.rawQuery("SELECT rowid, nazev FROM rozhodnuti WHERE rowid = " + noteId, null);
            c.moveToFirst();
            editText.setText(c.getString(c.getColumnIndex("nazev")));
        } else {
            // PŘEKOPAT DO DATABÁZE
            /*MainActivity.notes.add("");
            noteId = MainActivity.notes.size() - 1;
            MainActivity.arrayAdapter.notifyDataSetChanged();*/


        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*
                MainActivity.notes.set(noteId,String.valueOf(s));
                MainActivity.arrayAdapter.notifyDataSetChanged();
                */

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ulozButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().length() != 0) {
                    if (noteId != -1){
                        MainActivity.databazeRozhodnuti.execSQL("UPDATE rozhodnuti SET nazev = \"" + editText.getText() + "\" WHERE rowid = " + noteId);
                    } else {
                        MainActivity.databazeRozhodnuti.execSQL("INSERT INTO rozhodnuti (nazev, poradi) VALUES ('" + editText.getText() + "', 1)");
                    }
                    //MainActivity.arrayAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "Uloženo", Toast.LENGTH_SHORT).show();
                }
                MainActivity.nacteniDatabaze();
                MainActivity.arrayAdapter.notifyDataSetChanged();
                finish();
            }
        });

    }


    @Override
    protected void onStop() {

        super.onStop();
        getDelegate().onStop();
    }
}
