package com.example.fitworkmockup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;

public class Uebungsoptionen extends AppCompatActivity {
    private ArrayList<String> dataByID = new ArrayList<>();
    DBHelper mDBHelper = new DBHelper(this);
    private boolean timerSelected = false;
    private boolean repsSelected = false;
    private String dialogInput;

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
                    intent.putExtra("GEWÄHLTE_ZEIT", "Keine Angabe");
                    intent.putExtra("GEWÄHLTE_REPS", "Keine Angabe");
                    startActivity(intent);
                }
                else if(timerSelected && !repsSelected){
                    RelativeLayout relative = new RelativeLayout(Uebungsoptionen.this);
                    AlertDialog.Builder timerDialog = new AlertDialog.Builder(Uebungsoptionen.this);
                    timerDialog.setTitle("Geben Sie die gewünschte Übungsdauer an");

                    final NumberPicker numberPickerMinuten = new NumberPicker(Uebungsoptionen.this);
                    numberPickerMinuten.setMaxValue(59);
                    numberPickerMinuten.setMinValue(0);
                    final NumberPicker numberPickerSekunden = new NumberPicker(Uebungsoptionen.this);
                    numberPickerSekunden.setMaxValue(59);
                    numberPickerSekunden.setMinValue(0);

                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);

                    RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    RelativeLayout.LayoutParams qPicerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    numPicerParams.leftMargin = 20;
                    qPicerParams.rightMargin = 20;


                    numPicerParams.addRule(RelativeLayout.ALIGN_PARENT_START);
                    qPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    relative.setLayoutParams(params);
                    relative.addView(numberPickerMinuten,numPicerParams);
                    relative.addView(numberPickerSekunden,qPicerParams);
                    relative.setGravity(Gravity.CENTER);

                    timerDialog.setView(relative);

                    timerDialog.setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String mMinuten; String mSekunden;
                            if(numberPickerMinuten.getValue() <10){
                                mMinuten = "0" + numberPickerMinuten.getValue();
                            }
                            else{
                                mMinuten = String.valueOf(numberPickerMinuten.getValue());
                            }
                            if(numberPickerSekunden.getValue() <10){
                                mSekunden = "0" + numberPickerSekunden.getValue();
                            }
                            else{
                                mSekunden= String.valueOf(numberPickerSekunden.getValue());
                            }

                            dialogInput = mMinuten + ":" + mSekunden;
                            Toast.makeText(Uebungsoptionen.this, dialogInput, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Uebungsoptionen.this, Aktive_Uebung.class);
                            intent.putExtra("UEBUNGS_ID", mUebungsID);
                            intent.putExtra("GEWÄHLTE_ZEIT", dialogInput);
                            intent.putExtra("GEWÄHLTE_REPS", "Keine Angabe");
                            startActivity(intent);
                        }
                    });

                    timerDialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    timerDialog.show();
                }
                else if(!timerSelected && repsSelected){
                    AlertDialog.Builder repDialog = new AlertDialog.Builder(Uebungsoptionen.this);
                    repDialog.setTitle("Wählen Sie die gewünschten Übungswiederholungen");

                    final EditText repInput = new EditText(Uebungsoptionen.this);
                    repInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                    repDialog.setView(repInput);

                    repDialog.setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialogInput = repInput.getText().toString();
                            Toast.makeText(Uebungsoptionen.this, dialogInput, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Uebungsoptionen.this, Aktive_Uebung.class);
                            intent.putExtra("UEBUNGS_ID", mUebungsID);
                            intent.putExtra("GEWÄHLTE_ZEIT", "Keine Angabe");
                            intent.putExtra("GEWÄHLTE_REPS", dialogInput);
                            startActivity(intent);
                        }
                    });

                    repDialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    repDialog.show();
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