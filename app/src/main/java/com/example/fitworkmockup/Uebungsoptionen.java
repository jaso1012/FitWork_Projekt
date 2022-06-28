package com.example.fitworkmockup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;

public class Uebungsoptionen extends AppCompatActivity {
    private ArrayList<String> dataByID = new ArrayList<>();
    DBHelper mDBHelper = new DBHelper(this);
    private boolean timerSelected = false;
    private boolean repsSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uebungsoptionen);

        //values
        String uebungsname; String gif; String wiederholbar;
        String partner = "";  String beispiel;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Übungsoptionen wählen");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView mUebungsname = findViewById(R.id.opt_uebungsname);
        TextView mBeispiel = findViewById(R.id.opt_beispiel);
        TextView mPartner = findViewById(R.id.opt_partner_necessarry);
        GifImageView mGif = findViewById(R.id.opt_gifImageView);
        RadioButton mKeineAuswahl = findViewById(R.id.opt_chosenothing);
        RadioButton mStoppuhr = findViewById(R.id.opt_chosetime);
        RadioButton mReps = findViewById(R.id.opt_chosereps);
        Button mUebungStarten = findViewById(R.id.opt_uebungstarten);

        Intent intent = getIntent();
        int mUebungsID = intent.getIntExtra("UEBUNGS_ID", -1);
        Log.d("TAG", String.valueOf(mUebungsID));

        //getData and make it usable
        getDataByID(mUebungsID);
        String mPartnerErforderlich = "";
        if(Objects.equals(dataByID.get(8), "true")){
            mPartnerErforderlich = "Partner Erforderlich!";
        }
        uebungsname = dataByID.get(1);
        gif = dataByID.get(3);
        if (mPartnerErforderlich == "") {
            //do nothing
        }
        else {
            partner = "Für diese Übung ist ein " + mPartnerErforderlich;
        }
        wiederholbar = dataByID.get(7);
        beispiel = dataByID.get(10);

        mUebungsname.setText(uebungsname);
        mBeispiel.setText(beispiel);
        mPartner.setText(partner);
        mGif.setImageResource(Integer.parseInt(gif));
        if(Objects.equals(wiederholbar, "true")){
            mReps.setVisibility(View.VISIBLE);
        }

        mKeineAuswahl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKeineAuswahl.setChecked(true);
                mStoppuhr.setChecked(false);
                mReps.setChecked(false);
                timerSelected = false;
                repsSelected = false;
            }
        });

        mStoppuhr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKeineAuswahl.setChecked(false);
                mStoppuhr.setChecked(true);
                mReps.setChecked(false);
                timerSelected = true;
                repsSelected = false;
            }
        });

        mReps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mKeineAuswahl.setChecked(false);
                mStoppuhr.setChecked(false);
                mReps.setChecked(true);
                timerSelected = false;
                repsSelected = true;
            }
        });

        mUebungStarten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timerSelected && !repsSelected){
                    Intent intent = new Intent(Uebungsoptionen.this, Aktive_Uebung.class);
                    intent.putExtra("UEBUNGS_ID", mUebungsID);
                    startActivity(intent);
                }
                else if(timerSelected && !repsSelected){
                    //dialog
                }
                else if(!timerSelected && repsSelected){
                    //dialog
                }
            }
        });
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