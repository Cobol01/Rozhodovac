package com.example.cobol.rozhodovac;


import android.renderscript.Sampler;

public class Vyhodnoceni {
    public String polozka;
    public String score;

    public Vyhodnoceni(String polozka, Integer score) {
        this.polozka = polozka;
        this.score = String.valueOf(score);
    }
}