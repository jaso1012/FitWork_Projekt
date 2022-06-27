package com.example.fitworkmockup;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;

public class Uebungen_popup extends Activity {

    private ArrayList<String> dataByID = new ArrayList<>();
    DBHelper mDBHelper = new DBHelper(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uebungen_popup);

        //values
        String uebungsname; String uebungsbeschreibung; String gif; String koerperteil;
        String schwierigkeit; String partner; String vorgabezeit; String beispiel; String arbeitsplatz;
        int gifInteger;

        Intent intent = getIntent();
        int mUebungsID = intent.getIntExtra("UEBUNGS_ID", -1);
        Log.d("TAG", String.valueOf(mUebungsID));

        //setting activity display size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .7));

        //set Text etc
        GifImageView mGif = findViewById(R.id.popup_gifImageView);
        TextView mUebungsname = findViewById(R.id.popup_uebungsname);
        TextView mKoerperteil = findViewById(R.id.pupup_koerperteil);
        TextView mBeschreibung = findViewById(R.id.popup_beschreibung);
        TextView mSchwierigkeit = findViewById(R.id.popup_schwierigkeit);
        TextView mPartner = findViewById(R.id.popup_partner);
        TextView mDauer = findViewById(R.id.popup_dauer);
        TextView mBeispiel = findViewById(R.id.popup_beispiel);
        TextView mArbeitsplatz = findViewById(R.id.popup_arbeitsplatz);

        //getData and make it usable
        getDataByID(mUebungsID);

        String mSchwierigkeit_string = "";
        String mPartnerErforderlich = "";
        String mErwarteteDauer = "";
        if(Objects.equals(dataByID.get(6), "1")){
            mSchwierigkeit_string = "Schwierigkeit: Leicht";
        }
        else if(Objects.equals(dataByID.get(6), "2")){
            mSchwierigkeit_string = "Schwierigkeit: Mittel";
        }
        else{
            mSchwierigkeit_string = "Schwierigkeit: Schwer";
        }
        if(Objects.equals(dataByID.get(8), "true")){
            mPartnerErforderlich = "Partner Erforderlich!";
        }

        uebungsname = dataByID.get(1);
        uebungsbeschreibung = "Beschreibung: " + dataByID.get(2);
        gif = dataByID.get(3);
        gifInteger = Integer.parseInt(gif);
        koerperteil = "Hauptziel der Übung: " + dataByID.get(5);
        schwierigkeit = mSchwierigkeit_string;
        if (mPartnerErforderlich == "") {
            partner = "Diese Übung kann alleine durchgeführt werden";
        }
        else {
            partner = "Für diese Übung ist ein " + mPartnerErforderlich;
        }
        vorgabezeit = "Die erwartete Dauer dieser Übung beträgt " + dataByID.get(9);
        beispiel = "Beispiel: " + dataByID.get(10);
        if(dataByID.get(11) == "true") {
            arbeitsplatz = "Diese Übung kann am Arbeitsplatz durchgeführt werden";
        }
        else{
            arbeitsplatz = "Diese Übung kann nicht am Arbeitsplatz durchgeführt werden";
        }

        Log.d("TAG", gif);
        mUebungsname.setText(uebungsname);
        mKoerperteil.setText(koerperteil);
        mBeschreibung.setText(uebungsbeschreibung);
        mSchwierigkeit.setText(schwierigkeit);
        mPartner.setText(partner);
        mDauer.setText(vorgabezeit);
        mBeispiel.setText(beispiel);
        mArbeitsplatz.setText(arbeitsplatz);
        mGif.setImageResource(Integer.parseInt(gif));
    }

    private void getDataByID(int mUebungsID) {
        Cursor data = mDBHelper.getUebungenByID(String.valueOf(mUebungsID));
        while(data.moveToNext())
        {
            dataByID.add(data.getString(0));
            dataByID.add(data.getString(1));
            dataByID.add(data.getString(2));
            dataByID.add(data.getString(3));
            dataByID.add(data.getString(4));
            dataByID.add(data.getString(5));
            dataByID.add(data.getString(6));
            dataByID.add(data.getString(7));
            dataByID.add(data.getString(8));
            dataByID.add(data.getString(9));
            dataByID.add(data.getString(10));
            dataByID.add(data.getString(11));

        }
        data.close();
    }
}