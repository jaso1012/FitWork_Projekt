package com.example.fitworkmockup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Uebungshistorie extends AppCompatActivity {

    // Array of strings for ListView Title
    /*String[] listviewTitle = new String[]{
            "Spazieren", "Rückendehnung 1", "Rückendehnung 2", "ListView Title 4",
            "ListView Title 5", "ListView Title 6", "ListView Title 7", "ListView Title 8",
    };


    int[] listviewImage = new int[]{
            R.drawable.spazieren_fitwork, R.drawable.fitwork_beispielbild, R.drawable.fitwork_beispielbild, R.drawable.fitwork_beispielbild,
            R.drawable.fitwork_beispielbild, R.drawable.fitwork_beispielbild, R.drawable.fitwork_beispielbild, R.drawable.fitwork_beispielbild,
    };

    String[] listviewDatum = new String[]{
            "15.04.2022", "14.05.2022", "14.05.2022", "Android ListView Short Description",
            "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description",
    };

    String[] listviewDauer = new String[]{
            "5:04 Minuten", "30 Sekunden", "1:00 Minute", "Android ListView Short Description",
            "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description",
    };*/

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uebungen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Übungshistorie");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 8; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", listviewTitle[i]);
            hm.put("listview_datum", listviewDatum[i]);
            hm.put("listview_dauer", listviewDauer[i]);
            hm.put("listview_image", Integer.toString(listviewImage[i]));
            aList.add(hm);
        }

        String[] from = {"listview_image", "listview_title", "listview_datum", "listview_dauer"};
        int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_datum, R.id.listview_dauer};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.listview_for_uebungshistorie, from, to);
        ListView androidListView = (ListView) findViewById(R.id.uebungen_listview);
        androidListView.setAdapter(simpleAdapter);
    }*/
}