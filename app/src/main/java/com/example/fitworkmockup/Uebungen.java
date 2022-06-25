package com.example.fitworkmockup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Uebungen extends AppCompatActivity {

    DBHelper mDBHelper = new DBHelper(this);
    private List<HashMap<String, String>> mUebungenList = new ArrayList<HashMap<String, String>>();
    private  String mClickedItem;

    // Array of strings for ListView Title
    /*String[] listviewTitle = new String[]{
            "Spazieren", "Rückendehnung 1", "Rückendehnung 2", "ListView Title 4",
            "ListView Title 5", "ListView Title 6", "ListView Title 7", "ListView Title 8",
    };


    int[] listviewImage = new int[]{
            R.drawable.spazieren_fitwork, R.drawable.fitwork_beispielbild, R.drawable.fitwork_beispielbild, R.drawable.fitwork_beispielbild,
            R.drawable.fitwork_beispielbild, R.drawable.fitwork_beispielbild, R.drawable.fitwork_beispielbild, R.drawable.fitwork_beispielbild,
    };

    String[] listviewShortDescription = new String[]{
            "Geh raus an die frische Luft und laufe durch die Gegend", "Wirke Verspannungen entgegen", "Android ListView Short Description", "Android ListView Short Description",
            "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description",
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uebungen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Übungen");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getUebungenAlphabetically();

        String[] from = {"bild", "uebungsname", "schwierigkeit", "partner"};
        int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_item_short_description, R.id.listview_item_partner_necessary};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), mUebungenList, R.layout.listview_activity, from, to);
        ListView androidListView = (ListView) findViewById(R.id.uebungen_listview);
        androidListView.setAdapter(simpleAdapter);

        androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int mUebungID;
                final String item = parent.getItemAtPosition(position).toString();
                mUebungID = extractUebungID(item);
                mClickedItem = item.substring(0,1);
                Log.d("TAG", item);
                Log.d("TAG", String.valueOf(mUebungID));
                Intent intent = new Intent(Uebungen.this, Aktive_Uebung.class);
                startActivity(intent);
            }
        });

        androidListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //popup window
                return false;
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
                hm.put("vorgabezeit", data.getString(9));
                hm.put("beispiel", data.getString(10));
                hm.put("arbeitszeit", data.getString(11));
                mUebungenList.add(hm);
            }
            /* Beispielzwecke
            for (int i = 0; i < 8; i++) {
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("listview_title", listviewTitle[i]);
                hm.put("listview_description", listviewShortDescription[i]);
                hm.put("listview_image", Integer.toString(listviewImage[i]));
                aList.add(hm);
            }*/
            data.close();
        }
    }
}