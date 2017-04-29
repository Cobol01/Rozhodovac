package com.example.cobol.rozhodovac;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

public class NoteEditorActivity extends AppCompatActivity {
    int noteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        EditText editText = (EditText) findViewById(R.id.editText);

        Intent intent = getIntent();

        noteId = intent.getIntExtra("noteId", -1);

        if (noteId != -1){
            editText.setText(MainActivity.notes.get(noteId));
        } else {
            MainActivity.notes.add("");
            noteId = MainActivity.notes.size() - 1;
            MainActivity.arrayAdapter.notifyDataSetChanged();
        }


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                MainActivity.notes.set(noteId,String.valueOf(s));
                MainActivity.arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onStop() {

        Toast.makeText(getApplicationContext(), "konéc", Toast.LENGTH_SHORT).show();

        super.onStop();
        getDelegate().onStop();
    }
}
