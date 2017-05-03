package com.example.cobol.rozhodovac;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class VlastnostDetailActivity extends AppCompatActivity {

    String nazevVlastnosti;

    int vahaVlastnosti;
    int idVlastnost;
    int kdo;

    EditText editText;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vlastnost_detail);

        Intent intent = getIntent();
        nazevVlastnosti = intent.getStringExtra("nazevVlastnosti");
        vahaVlastnosti = intent.getIntExtra("vahaVlastnosti", -1);
        idVlastnost = intent.getIntExtra("vlastnostId", -1);
        kdo = intent.getIntExtra("kdo", -1);

        editText = (EditText) findViewById(R.id.nazevVlastnostiEditText);

        if (kdo == 2) editText.setFocusable(false);

        seekBar = (SeekBar) findViewById(R.id.vahaSeekBar);

        if (nazevVlastnosti != null) editText.setText(nazevVlastnosti);

        if (vahaVlastnosti != -1) seekBar.setProgress(vahaVlastnosti);



        Button ulozitButton = (Button) findViewById(R.id.ulozitVlastnostButton);

        ulozitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kdo == 1) {
                    try {
                        MainActivity.databazeRozhodnuti.execSQL("UPDATE vlastnosti SET nazevVlast = \"" + editText.getText() + "\" , vahaVlast = " + seekBar.getProgress() + " WHERE idVlast = " + idVlastnost);
                        Tab3Activity.nacteniDatabaze(Tab3Activity.mRootView);
                        Tab3Activity.arrayAdapter.notifyDataSetChanged();
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if(kdo == 2){
                    MainActivity.databazeRozhodnuti.execSQL("UPDATE srovnani SET vaha = " + seekBar.getProgress() + " WHERE idSrov = " + idVlastnost);
                    PolozkyDetailActivity.nacteniDatabaze();
                    finish();
                }
                }
        });

    }
}
