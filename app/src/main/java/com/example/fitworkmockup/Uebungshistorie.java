package com.example.fitworkmockup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Uebungshistorie extends AppCompatActivity {

    DBHelper mDBHelper = new DBHelper(Uebungshistorie.this);
    private List<HashMap<String, String>> mUebungenList = new ArrayList<HashMap<String, String>>();
    private String m_1;
    private boolean mTryingToDelete = false;
    private FloatingActionButton mDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uebungshistorie);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Übungshistorie");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDelete = (FloatingActionButton) findViewById(R.id.fab_delete);

        getHistoryByDate();

        String[] from = {"bild", "uebungsname", "datum", "zeit", "zieldauer", "erzieltewiederholungen"};
        int[] to = {R.id.hist_listview_image, R.id.hist_listview_item_title, R.id.hist_listview_datum, R.id.hist_listview_dauer, R.id.hist_listview_zieldauer, R.id.hist_listview_wiederholungen};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), mUebungenList, R.layout.listview_for_uebungshistorie, from, to);
        ListView androidListView = (ListView) findViewById(R.id.uebunshistorie_listview);
        androidListView.setAdapter(simpleAdapter);

        androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int mUebungID; int mHistorieID;
                final String item = parent.getItemAtPosition(position).toString();
                mUebungID = extractUebungID(item);
                mHistorieID =extractHistorieID(item);
                Log.d("TAG", String.valueOf(mUebungID));
                Intent intent = new Intent(Uebungshistorie.this, Historie_popup.class);
                intent.putExtra("UEBUNGS_ID", mUebungID);
                intent.putExtra("HISTORIEN_ID", mHistorieID);
                startActivity(intent);
            }
        });

        androidListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int mHistorienID;
                final String item = parent.getItemAtPosition(position).toString();
                mHistorienID = extractHistorieID(item);
                m_1 = String.valueOf(mHistorienID);
                if (mTryingToDelete) {
                    for (int i = 0; i < parent.getChildCount(); i++) {
                        parent.getChildAt(i).setBackgroundColor(Color.WHITE);
                    }
                    mTryingToDelete = false;
                    mDelete.setVisibility(View.INVISIBLE);
                    mDelete.setClickable(false);

                }
                else {
                    //Set background of all items to white
                    for (int i = 0; i < parent.getChildCount(); i++) {
                        parent.getChildAt(i).setBackgroundColor(Color.WHITE);
                    }
                    view.setBackgroundColor(Color.RED);
                    Log.d("TAG", "ID von zum löschen ausgewehlten Elemt ist: " +mHistorienID);
                    mTryingToDelete = true;
                    mDelete.setVisibility(View.VISIBLE);
                    mDelete.setClickable(true);
                }
                return true;
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(m_1 != null){
                    mDBHelper.deleteDataByID(m_1);
                    Log.d("TAG", "ID: " +m_1 +" wurde eigtl. gelöscht");
                    mDelete.setVisibility(View.INVISIBLE);
                    mDelete.setClickable(false);
                    mUebungenList.clear();
                    getHistoryByDate();
                    androidListView.setAdapter(simpleAdapter);
                    mTryingToDelete = false;
                }
                else{
                    Toast.makeText(Uebungshistorie.this, "Da ist etwas schiefgelaufen! Nicht gelöscht.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getHistoryByDate() {
        if(mDBHelper != null)
        {
            Cursor data = mDBHelper.getHistoryByDate();
            while (data.moveToNext()) {
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("historieID", data.getString(0));
                hm.put("uebungsID", data.getString(1));
                hm.put("datum", "Datum: " + data.getString(2));
                hm.put("zeit", "Dauer: " + data.getString(3));
                hm.put("zielzeit", "Zieldauer war: " + data.getString(4));
                hm.put("erzieltewiederholungen", "Erzielte Wiederholungen: " + data.getString(5));
                hm.put("zielwiederholung", "Zielwiederholungen waren: " + data.getString(6));
                Cursor data1 = mDBHelper.getUebungenByID(data.getString(1));
                while (data1.moveToNext()) {
                    String mSchwierigkeit_string = "";
                    String mPartnerErforderlich = "";
                    String mErwarteteDauer = "";
                    if (Objects.equals(data1.getString(6), "1")) {
                        mSchwierigkeit_string = "Schwierigkeit: Leicht";
                    } else if (Objects.equals(data1.getString(6), "2")) {
                        mSchwierigkeit_string = "Schwierigkeit: Mittel";
                    } else {
                        mSchwierigkeit_string = "Schwierigkeit: Schwer";
                    }
                    if (Objects.equals(data1.getString(8), "true")) {
                        mPartnerErforderlich = "Partner Erforderlich!";
                    }
                    mErwarteteDauer = "Dauer: " + data1.getString(9);

                    hm.put("uebungsname", data1.getString(1));
                    hm.put("uebungsbeschreibung", data1.getString(2));
                    hm.put("gif", data1.getString(3));
                    hm.put("bild", data1.getString(4));
                    hm.put("koerperteil", data1.getString(5));
                    hm.put("schwierigkeit", mSchwierigkeit_string);
                    hm.put("partner", mPartnerErforderlich);
                    hm.put("vorgabezeit", mErwarteteDauer);
                    hm.put("beispiel", data1.getString(10));
                }
                mUebungenList.add(hm);
            }
            data.close();
        }
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

    private int extractHistorieID(String item) {
        int mFirstIndex; int mLastIndex;
        String mFirstCutOff; String mSecondCutOff;
        mFirstIndex = item.lastIndexOf("historieID=");
        mFirstCutOff = item.substring(mFirstIndex);
        mSecondCutOff = mFirstCutOff.replace("historieID=", "");
        mLastIndex = mSecondCutOff.indexOf(",");
        return Integer.parseInt(mSecondCutOff.substring(0, mLastIndex +2));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_all_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sub_delete_all:
                AlertDialog.Builder repDialog = new AlertDialog.Builder(Uebungshistorie.this);
                repDialog.setTitle("Wollen Sie wirklich alle Dateien löschen?");

                final EditText repInput = new EditText(Uebungshistorie.this);
                repInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                repDialog.setView(repInput);

                repDialog.setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDBHelper.deleteAllData();
                        mUebungenList.clear();
                        getHistoryByDate();
                        //an sich unnötig aber gibt die visuelle Bestätigung
                        String[] from = {"bild", "uebungsname", "datum", "zeit", "zieldauer", "erzieltewiederholungen"};
                        int[] to = {R.id.hist_listview_image, R.id.hist_listview_item_title, R.id.hist_listview_datum, R.id.hist_listview_dauer, R.id.hist_listview_zieldauer, R.id.hist_listview_wiederholungen};
                        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), mUebungenList, R.layout.listview_for_uebungshistorie, from, to);
                        ListView androidListView = (ListView) findViewById(R.id.uebunshistorie_listview);
                        androidListView.setAdapter(simpleAdapter);
                    }
                });

                repDialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                repDialog.show();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}