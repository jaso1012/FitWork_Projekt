package com.example.fitworkmockup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Uebungen extends AppCompatActivity {

    DBHelper mDBHelper = new DBHelper(this);
    private List<HashMap<String, String>> mUebungenList = new ArrayList<HashMap<String, String>>();

    PopupWindow popup;

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

        String[] from = {"bild", "uebungsname", "vorgabezeit", "schwierigkeit", "partner"};
        int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_dauer, R.id.listview_item_short_description, R.id.listview_item_partner_necessary};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), mUebungenList, R.layout.listview_activity, from, to);
        ListView androidListView = (ListView) findViewById(R.id.uebungen_listview);
        androidListView.setAdapter(simpleAdapter);

        androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int mUebungID;
                final String item = parent.getItemAtPosition(position).toString();
                mUebungID = extractUebungID(item);
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

                /*//Daten aus HashMap holen
                String uebungsname; String uebungsbeschreibung; String gif; String koerperteil;
                String schwierigkeit; String partner; String vorgabezeit; String beispiel; String arbeitsplatz;
                int gifInteger;

                uebungsname = mUebungenList.get(mUebungID).get("uebungsname");
                uebungsbeschreibung = "Beschreibung: " + mUebungenList.get(mUebungID).get("uebungsbeschreibung");
                gif = mUebungenList.get(mUebungID).get("gif");
                gifInteger = Integer.parseInt(gif);
                koerperteil = "Hauptziel der Übung: " + mUebungenList.get(mUebungID).get("koerperteil");
                schwierigkeit = mUebungenList.get(mUebungID).get("schwierigkeit");
                if (mUebungenList.get(mUebungID).get("partner") == "") {
                    partner = "Diese Übung kann alleine durchgeführt werden";
                }
                else {
                    partner = "Für diese Übung ist ein " + mUebungenList.get(mUebungID).get("partner");
                }
                vorgabezeit = "Die erwartete Dauer dieser Übung beträgt " + mUebungenList.get(mUebungID).get("vorgabezeit");
                beispiel = "Beispiel: " + mUebungenList.get(mUebungID).get("beispiel");
                if(mUebungenList.get(mUebungID).get("arbeitszeit") == "true") {
                    arbeitsplatz = "Diese Übung kann am Arbeitsplatz durchgeführt werden";
                }
                else{
                    arbeitsplatz = "Diese Übung kann nicht am Arbeitsplatz durchgeführt werden";
                }

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.uebungen_popup, null);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

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

                Log.d("TAG", gif);
                mUebungsname.setText(uebungsname);
                mKoerperteil.setText(koerperteil);
                mBeschreibung.setText(uebungsbeschreibung);
                mSchwierigkeit.setText(schwierigkeit);
                mPartner.setText(partner);
                mDauer.setText(vorgabezeit);
                mBeispiel.setText(beispiel);
                mArbeitsplatz.setText(arbeitsplatz);


                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });*/
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.uebungen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.alphabetisch:
                //Pop-up "Sind sie sicher, dass sie alle Einträge löschen wollen?" Ja/Nein
                Toast.makeText(Uebungen.this, "Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}