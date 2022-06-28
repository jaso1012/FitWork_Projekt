package com.example.fitworkmockup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Uebungen extends AppCompatActivity {

    DBHelper mDBHelper = new DBHelper(Uebungen.this);
    private List<HashMap<String, String>> mUebungenList = new ArrayList<HashMap<String, String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uebungen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Übungen");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String prevStarted = "prevStarted2";
        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);

        getUebungenAlphabetically();

        String[] from = {"bild", "uebungsname", "vorgabezeit", "schwierigkeit", "partner"};
        int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_dauer, R.id.listview_item_short_description, R.id.listview_item_partner_necessary};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), mUebungenList, R.layout.listview_activity, from, to);
        ListView androidListView = (ListView) findViewById(R.id.uebungen_listview);
        androidListView.setAdapter(simpleAdapter);

        if (!sharedpreferences.getBoolean(prevStarted, false))
        {
            new TapTargetSequence(this)
                    .targets(
                            TapTarget.forView(androidListView,"Jede Übung beinhaltet eine einfache Bescheibung der Übung mitsamt Anleitbildern, eine Uhr und einen geschätzen Schwierigkeitsgrad, "," ")
                                    .outerCircleColor(R.color.light_blue)
                                    .outerCircleAlpha(0.96f)
                                    .targetCircleColor(R.color.white)
                                    .titleTextSize(20)
                                    .titleTextColor(R.color.white)
                                    .descriptionTextSize(10)
                                    .descriptionTextColor(R.color.black)
                                    .textColor(R.color.black)
                                    .textTypeface(Typeface.SANS_SERIF)
                                    .dimColor(R.color.black)
                                    .drawShadow(true)
                                    .cancelable(false)
                                    .tintTarget(true)
                                    .transparentTarget(false)
                                    .targetRadius(60)).listener(new TapTargetSequence.Listener()
                    {
                        @Override
                        public void onSequenceFinish() {

                        }

                        @Override
                        public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                        }

                        @Override
                        public void onSequenceCanceled(TapTarget lastTarget) {

                        }
                    }).start();

            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(prevStarted, Boolean.TRUE);
            editor.apply();
        }

        androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int mUebungID;
                final String item = parent.getItemAtPosition(position).toString();
                mUebungID = extractUebungID(item);
                Log.d("TAG", String.valueOf(mUebungID));
                Intent intent = new Intent(Uebungen.this, Uebungsoptionen.class);
                intent.putExtra("UEBUNGS_ID", mUebungID);
                startActivity(intent);
            }
        });

        androidListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //popup window
                int mUebungID;
                final String item = parent.getItemAtPosition(position).toString();
                mUebungID = extractUebungID(item);
                Intent intent = new Intent(Uebungen.this, Uebungen_popup.class);
                intent.putExtra("UEBUNGS_ID", mUebungID);
                startActivity(intent);
                return true;
            }
        });
    }

    private int extractUebungID(String item) {
        int mFirstIndex; int mLastIndex;
        String mFirstCutOff; String mSecondCutOff;
        mFirstIndex = item.lastIndexOf("uebungsID=");
        mFirstCutOff = item.substring(mFirstIndex);
        mSecondCutOff = mFirstCutOff.replace("uebungsID=", "");
        mLastIndex = mSecondCutOff.indexOf(",");
        return Integer.parseInt(mSecondCutOff.substring(0, mLastIndex));
    }


    protected void getUebungenAlphabetically()
    {
        if(mDBHelper != null)
        {
            Cursor data = mDBHelper.getUebungenAlphabetically();

            while(data.moveToNext())
            {
                String mSchwierigkeit_string = "";
                String mPartnerErforderlich = "";
                String mErwarteteDauer = "";
                if(Objects.equals(data.getString(6), "1")){
                    mSchwierigkeit_string = "Schwierigkeit: Leicht";
                }
                else if(Objects.equals(data.getString(6), "2")){
                    mSchwierigkeit_string = "Schwierigkeit: Mittel";
                }
                else{
                    mSchwierigkeit_string = "Schwierigkeit: Schwer";
                }
                if(Objects.equals(data.getString(8), "true")){
                    mPartnerErforderlich = "Partner Erforderlich!";
                }
                mErwarteteDauer = "Dauer: " + data.getString(9);


                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("uebungsID", data.getString(0));
                hm.put("uebungsname", data.getString(1));
                hm.put("uebungsbeschreibung", data.getString(2));
                hm.put("gif", data.getString(3));
                hm.put("bild", data.getString(4));
                hm.put("koerperteil", data.getString(5));
                hm.put("schwierigkeit", mSchwierigkeit_string);
                hm.put("wiederholung", data.getString(7));
                hm.put("partner", mPartnerErforderlich);
                hm.put("vorgabezeit", mErwarteteDauer);
                hm.put("beispiel", data.getString(10));
                hm.put("arbeitszeit", data.getString(11));
                mUebungenList.add(hm);
            }

            data.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.uebungen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String[] from = {"bild", "uebungsname", "vorgabezeit", "schwierigkeit", "partner"};
        int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_dauer, R.id.listview_item_short_description, R.id.listview_item_partner_necessary};
        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), mUebungenList, R.layout.listview_activity, from, to);
        ListView androidListView = (ListView) findViewById(R.id.uebungen_listview);
        switch (item.getItemId()) {
            case R.id.alphabetisch:
                mUebungenList.clear();
                getUebungenAlphabetically();
                androidListView.setAdapter(simpleAdapter);
                return true;
            case R.id.Difficulty:
                mUebungenList.clear();
                getUebungenDifficulty();
                androidListView.setAdapter(simpleAdapter);
                return true;
            case R.id.Leicht:
                mUebungenList.clear();
                getEasy();
                androidListView.setAdapter(simpleAdapter);
                return true;
            case R.id.Mittel:
                mUebungenList.clear();
                getMiddle();
                androidListView.setAdapter(simpleAdapter);
                return true;
            case R.id.Schwer:
                mUebungenList.clear();
                getDifficult();
                androidListView.setAdapter(simpleAdapter);
                return true;
            case R.id.NAAM:
                mUebungenList.clear();
                getAtWorkplace();
                androidListView.setAdapter(simpleAdapter);
                return true;
            case R.id.AAM:
                mUebungenList.clear();
                getNotAtWorkplace();
                androidListView.setAdapter(simpleAdapter);
                return true;
            case R.id.MP:
                mUebungenList.clear();
                getWithPartner();
                androidListView.setAdapter(simpleAdapter);
                return true;
            case R.id.OP:
                mUebungenList.clear();
                getWithoutPartner();
                androidListView.setAdapter(simpleAdapter);
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getWithoutPartner() {
        if(mDBHelper != null) {
            Cursor data = mDBHelper.getOP();

            while (data.moveToNext()) {
                String mSchwierigkeit_string = "";
                String mPartnerErforderlich = "";
                String mErwarteteDauer = "";
                if (Objects.equals(data.getString(6), "1")) {
                    mSchwierigkeit_string = "Schwierigkeit: Leicht";
                } else if (Objects.equals(data.getString(6), "2")) {
                    mSchwierigkeit_string = "Schwierigkeit: Mittel";
                } else {
                    mSchwierigkeit_string = "Schwierigkeit: Schwer";
                }
                if (Objects.equals(data.getString(8), "true")) {
                    mPartnerErforderlich = "Partner Erforderlich!";
                }
                mErwarteteDauer = "Dauer: " + data.getString(9);
                //kann nicht


                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("uebungsID", data.getString(0));
                hm.put("uebungsname", data.getString(1));
                hm.put("uebungsbeschreibung", data.getString(2));
                hm.put("gif", data.getString(3));
                hm.put("bild", data.getString(4));
                hm.put("koerperteil", data.getString(5));
                hm.put("schwierigkeit", mSchwierigkeit_string);
                hm.put("wiederholung", data.getString(7));
                hm.put("partner", mPartnerErforderlich);
                hm.put("vorgabezeit", mErwarteteDauer);
                hm.put("beispiel", data.getString(10));
                hm.put("arbeitszeit", data.getString(11));
                mUebungenList.add(hm);
            }
            data.close();
        }
    }

    private void getWithPartner() {
        if(mDBHelper != null) {
            Cursor data = mDBHelper.getMP();

            while (data.moveToNext()) {
                String mSchwierigkeit_string = "";
                String mPartnerErforderlich = "";
                String mErwarteteDauer = "";
                if (Objects.equals(data.getString(6), "1")) {
                    mSchwierigkeit_string = "Schwierigkeit: Leicht";
                } else if (Objects.equals(data.getString(6), "2")) {
                    mSchwierigkeit_string = "Schwierigkeit: Mittel";
                } else {
                    mSchwierigkeit_string = "Schwierigkeit: Schwer";
                }
                if (Objects.equals(data.getString(8), "true")) {
                    mPartnerErforderlich = "Partner Erforderlich!";
                }
                mErwarteteDauer = "Dauer: " + data.getString(9);


                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("uebungsID", data.getString(0));
                hm.put("uebungsname", data.getString(1));
                hm.put("uebungsbeschreibung", data.getString(2));
                hm.put("gif", data.getString(3));
                hm.put("bild", data.getString(4));
                hm.put("koerperteil", data.getString(5));
                hm.put("schwierigkeit", mSchwierigkeit_string);
                hm.put("wiederholung", data.getString(7));
                hm.put("partner", mPartnerErforderlich);
                hm.put("vorgabezeit", mErwarteteDauer);
                hm.put("beispiel", data.getString(10));
                hm.put("arbeitszeit", data.getString(11));
                mUebungenList.add(hm);
            }
            data.close();
        }
    }

    private void getNotAtWorkplace() {
        if(mDBHelper != null) {
            Cursor data = mDBHelper.getNAAM();

            while (data.moveToNext()) {
                String mSchwierigkeit_string = "";
                String mPartnerErforderlich = "";
                String mErwarteteDauer = "";
                if (Objects.equals(data.getString(6), "1")) {
                    mSchwierigkeit_string = "Schwierigkeit: Leicht";
                } else if (Objects.equals(data.getString(6), "2")) {
                    mSchwierigkeit_string = "Schwierigkeit: Mittel";
                } else {
                    mSchwierigkeit_string = "Schwierigkeit: Schwer";
                }
                if (Objects.equals(data.getString(8), "true")) {
                    mPartnerErforderlich = "Partner Erforderlich!";
                }
                mErwarteteDauer = "Dauer: " + data.getString(9);


                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("uebungsID", data.getString(0));
                hm.put("uebungsname", data.getString(1));
                hm.put("uebungsbeschreibung", data.getString(2));
                hm.put("gif", data.getString(3));
                hm.put("bild", data.getString(4));
                hm.put("koerperteil", data.getString(5));
                hm.put("schwierigkeit", mSchwierigkeit_string);
                hm.put("wiederholung", data.getString(7));
                hm.put("partner", mPartnerErforderlich);
                hm.put("vorgabezeit", mErwarteteDauer);
                hm.put("beispiel", data.getString(10));
                hm.put("arbeitszeit", data.getString(11));
                mUebungenList.add(hm);
            }
            data.close();
        }
    }

    private void getAtWorkplace() {
        if(mDBHelper != null) {
            Cursor data = mDBHelper.getAAM();

            while (data.moveToNext()) {
                String mSchwierigkeit_string = "";
                String mPartnerErforderlich = "";
                String mErwarteteDauer = "";
                if (Objects.equals(data.getString(6), "1")) {
                    mSchwierigkeit_string = "Schwierigkeit: Leicht";
                } else if (Objects.equals(data.getString(6), "2")) {
                    mSchwierigkeit_string = "Schwierigkeit: Mittel";
                } else {
                    mSchwierigkeit_string = "Schwierigkeit: Schwer";
                }
                if (Objects.equals(data.getString(8), "true")) {
                    mPartnerErforderlich = "Partner Erforderlich!";
                }
                mErwarteteDauer = "Dauer: " + data.getString(9);


                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("uebungsID", data.getString(0));
                hm.put("uebungsname", data.getString(1));
                hm.put("uebungsbeschreibung", data.getString(2));
                hm.put("gif", data.getString(3));
                hm.put("bild", data.getString(4));
                hm.put("koerperteil", data.getString(5));
                hm.put("schwierigkeit", mSchwierigkeit_string);
                hm.put("wiederholung", data.getString(7));
                hm.put("partner", mPartnerErforderlich);
                hm.put("vorgabezeit", mErwarteteDauer);
                hm.put("beispiel", data.getString(10));
                hm.put("arbeitszeit", data.getString(11));
                mUebungenList.add(hm);
            }
            data.close();
        }
    }

    private void getDifficult() {
        if(mDBHelper != null) {
            Cursor data = mDBHelper.getUebungenDifficultDifficulty();

            while (data.moveToNext()) {
                String mSchwierigkeit_string = "";
                String mPartnerErforderlich = "";
                String mErwarteteDauer = "";
                if (Objects.equals(data.getString(6), "1")) {
                    mSchwierigkeit_string = "Schwierigkeit: Leicht";
                } else if (Objects.equals(data.getString(6), "2")) {
                    mSchwierigkeit_string = "Schwierigkeit: Mittel";
                } else {
                    mSchwierigkeit_string = "Schwierigkeit: Schwer";
                }
                if (Objects.equals(data.getString(8), "true")) {
                    mPartnerErforderlich = "Partner Erforderlich!";
                }
                mErwarteteDauer = "Dauer: " + data.getString(9);


                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("uebungsID", data.getString(0));
                hm.put("uebungsname", data.getString(1));
                hm.put("uebungsbeschreibung", data.getString(2));
                hm.put("gif", data.getString(3));
                hm.put("bild", data.getString(4));
                hm.put("koerperteil", data.getString(5));
                hm.put("schwierigkeit", mSchwierigkeit_string);
                hm.put("wiederholung", data.getString(7));
                hm.put("partner", mPartnerErforderlich);
                hm.put("vorgabezeit", mErwarteteDauer);
                hm.put("beispiel", data.getString(10));
                hm.put("arbeitszeit", data.getString(11));
                mUebungenList.add(hm);
            }
            data.close();
        }
    }

    private void getMiddle() {
        if(mDBHelper != null) {
            Cursor data = mDBHelper.getUebungenMiddleDifficulty();

            while (data.moveToNext()) {
                String mSchwierigkeit_string = "";
                String mPartnerErforderlich = "";
                String mErwarteteDauer = "";
                if (Objects.equals(data.getString(6), "1")) {
                    mSchwierigkeit_string = "Schwierigkeit: Leicht";
                } else if (Objects.equals(data.getString(6), "2")) {
                    mSchwierigkeit_string = "Schwierigkeit: Mittel";
                } else {
                    mSchwierigkeit_string = "Schwierigkeit: Schwer";
                }
                if (Objects.equals(data.getString(8), "true")) {
                    mPartnerErforderlich = "Partner Erforderlich!";
                }
                mErwarteteDauer = "Dauer: " + data.getString(9);


                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("uebungsID", data.getString(0));
                hm.put("uebungsname", data.getString(1));
                hm.put("uebungsbeschreibung", data.getString(2));
                hm.put("gif", data.getString(3));
                hm.put("bild", data.getString(4));
                hm.put("koerperteil", data.getString(5));
                hm.put("schwierigkeit", mSchwierigkeit_string);
                hm.put("wiederholung", data.getString(7));
                hm.put("partner", mPartnerErforderlich);
                hm.put("vorgabezeit", mErwarteteDauer);
                hm.put("beispiel", data.getString(10));
                hm.put("arbeitszeit", data.getString(11));
                mUebungenList.add(hm);
            }
            data.close();
        }
    }

    private void getEasy() {
        if(mDBHelper != null)
        {
            Cursor data = mDBHelper.getUebungenEasyDifficulty();

            while(data.moveToNext())
            {
                String mSchwierigkeit_string = "";
                String mPartnerErforderlich = "";
                String mErwarteteDauer = "";
                if(Objects.equals(data.getString(6), "1")){
                    mSchwierigkeit_string = "Schwierigkeit: Leicht";
                }
                else if(Objects.equals(data.getString(6), "2")){
                    mSchwierigkeit_string = "Schwierigkeit: Mittel";
                }
                else{
                    mSchwierigkeit_string = "Schwierigkeit: Schwer";
                }
                if(Objects.equals(data.getString(8), "true")){
                    mPartnerErforderlich = "Partner Erforderlich!";
                }
                mErwarteteDauer = "Dauer: " + data.getString(9);


                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("uebungsID", data.getString(0));
                hm.put("uebungsname", data.getString(1));
                hm.put("uebungsbeschreibung", data.getString(2));
                hm.put("gif", data.getString(3));
                hm.put("bild", data.getString(4));
                hm.put("koerperteil", data.getString(5));
                hm.put("schwierigkeit", mSchwierigkeit_string);
                hm.put("wiederholung", data.getString(7));
                hm.put("partner", mPartnerErforderlich);
                hm.put("vorgabezeit", mErwarteteDauer);
                hm.put("beispiel", data.getString(10));
                hm.put("arbeitszeit", data.getString(11));
                mUebungenList.add(hm);
            }
            data.close();
        }
    }

    private void getUebungenDifficulty() {
        if(mDBHelper != null)
        {
            Cursor data = mDBHelper.getUebungenDifficulty();

            while(data.moveToNext())
            {
                String mSchwierigkeit_string = "";
                String mPartnerErforderlich = "";
                String mErwarteteDauer = "";
                if(Objects.equals(data.getString(6), "1")){
                    mSchwierigkeit_string = "Schwierigkeit: Leicht";
                }
                else if(Objects.equals(data.getString(6), "2")){
                    mSchwierigkeit_string = "Schwierigkeit: Mittel";
                }
                else{
                    mSchwierigkeit_string = "Schwierigkeit: Schwer";
                }
                if(Objects.equals(data.getString(8), "true")){
                    mPartnerErforderlich = "Partner Erforderlich!";
                }
                mErwarteteDauer = "Dauer: " + data.getString(9);


                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("uebungsID", data.getString(0));
                hm.put("uebungsname", data.getString(1));
                hm.put("uebungsbeschreibung", data.getString(2));
                hm.put("gif", data.getString(3));
                hm.put("bild", data.getString(4));
                hm.put("koerperteil", data.getString(5));
                hm.put("schwierigkeit", mSchwierigkeit_string);
                hm.put("wiederholung", data.getString(7));
                hm.put("partner", mPartnerErforderlich);
                hm.put("vorgabezeit", mErwarteteDauer);
                hm.put("beispiel", data.getString(10));
                hm.put("arbeitszeit", data.getString(11));
                mUebungenList.add(hm);
            }
            data.close();
        }
    }
}