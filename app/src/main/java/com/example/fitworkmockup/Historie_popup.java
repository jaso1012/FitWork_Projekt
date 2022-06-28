package com.example.fitworkmockup;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;

public class Historie_popup extends Activity {
    private ArrayList<String> dataByID = new ArrayList<>();
    DBHelper mDBHelper = new DBHelper(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_historie);

        //values
        String uebungsname; String zieldauer; String gif; String koerperteil;
        String schwierigkeit; String datum; String vorgabezeit; String beispiel; String erzielteWiederholungen;
        String dauer; String zielWiederholungen;

        Intent intent = getIntent();
        int mUebungsID = intent.getIntExtra("UEBUNGS_ID", -1);
        int mHistorieID = intent.getIntExtra("HISTORIEN_ID", -1);
        Log.d("TAG", String.valueOf(mUebungsID));

        //setting activity display size
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .7));

        //set Text etc
        GifImageView mGif = findViewById(R.id.popup_gifImageView_hist);
        TextView mUebungsname = findViewById(R.id.popup_uebungsname_hist);
        TextView mKoerperteil = findViewById(R.id.pupup_koerperteil_hist);
        TextView mDauer = findViewById(R.id.popup_dauer_hist);
        TextView mSchwierigkeit = findViewById(R.id.popup_schwierigkeit_hist);
        TextView mDatum = findViewById(R.id.popup_datum_hist);
        TextView mZieldauer = findViewById(R.id.popup_zieldauer_hist);
        TextView mBeispiel = findViewById(R.id.popup_beispiel_hist);
        TextView mVorgegebeneDauer = findViewById(R.id.popup_vorgegebenedauer_hist);
        TextView mWiederholungen = findViewById(R.id.popup_wiederholungen_hist);
        TextView mZielWiederholungen = findViewById(R.id.popup_zielwiederholungen_hist);

        //getData and make it usable
        getHistoryByID(String.valueOf(mUebungsID));
        getDataById(String.valueOf(mHistorieID));

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

        uebungsname = dataByID.get(1);
        gif = dataByID.get(3);
        koerperteil = "Hauptziel der Übung: " + dataByID.get(5);
        schwierigkeit = mSchwierigkeit_string;
        vorgabezeit = "Die erwartete Dauer dieser Übung betrug " + dataByID.get(9);
        beispiel = "Beispiel: " + dataByID.get(10);

        datum = "Datum: " + dataByID.get(14);
        dauer = "Dauer: " + dataByID.get(15);
        zieldauer = dataByID.get(16);
        erzielteWiederholungen = dataByID.get(17);
        zielWiederholungen = dataByID.get(18);



        Log.d("TAG", gif);
        mUebungsname.setText(uebungsname);
        mKoerperteil.setText(koerperteil);
        mDauer.setText(dauer);
        mSchwierigkeit.setText(schwierigkeit);
        mDatum.setText(datum);
        mVorgegebeneDauer.setText(vorgabezeit);
        mBeispiel.setText(beispiel);
        mWiederholungen.setText("Erzielte Wiederholungen: " + erzielteWiederholungen);
        mZielWiederholungen.setText("Ziel Wiederholungen: " + zielWiederholungen);
        mZieldauer.setText("Ziel Dauer: " + zieldauer);
        mGif.setImageResource(Integer.parseInt(gif));
    }

    private void getDataById(String id) {
        Cursor data1 = mDBHelper.getHistorieByID(id);
        while(data1.moveToNext()) {
            dataByID.add(data1.getString(0));
            dataByID.add(data1.getString(1));
            dataByID.add(data1.getString(2));
            dataByID.add(data1.getString(3));
            dataByID.add(data1.getString(4));
            dataByID.add(data1.getString(5));
            dataByID.add(data1.getString(6));
        }
        data1.close();
    }

    private void getHistoryByID(String id1) {
        if(mDBHelper != null)
        {
            Cursor data = mDBHelper.getUebungenByID(String.valueOf(id1));
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
}
