package com.example.fitworkmockup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;

public class Aktive_Uebung extends AppCompatActivity {

    private ArrayList<String> dataByID = new ArrayList<>();
    DBHelper mDBHelper = new DBHelper(this);
    private boolean timerSelected = false;
    private boolean repsSelected = false;
    private String mMinuten;
    private String mSekunden;
    private  String mErreichteReps;
    private int mMillisekunden;
    private  int mUebungsID;
    private String mZielzeit;
    private String mZielWiederholungen;
    private String time;
    private  int mDauerInSekunden;
    // Number of seconds displayed
    // on the stopwatch.
    private int seconds = 0;
    // Is the stopwatch running?
    private boolean running;
    private boolean done = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktive_uebung);

        //values
        String uebungsname; String gif;
        String beispiel;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Gewählte Übung");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView mUebungsname = findViewById(R.id.akt_uebungsname);
        TextView mBeispiel = findViewById(R.id.akt_beispiel);
        TextView mZielWiederholung = findViewById(R.id.akt_zielwiederholungen);
        TextView mCountdown = findViewById(R.id.akt_countdown);
        TextView mTimer = findViewById(R.id.akt_timer);
        GifImageView mGif = findViewById(R.id.akt_gifImageView);
        Button mUebungStarten = findViewById(R.id.akt_buttonstart);
        Button mUebungBeenden = findViewById(R.id.akt_buttonende);

        Intent intent = getIntent();
        mUebungsID = intent.getIntExtra("UEBUNGS_ID", -1);
        mZielzeit = intent.getStringExtra("GEWÄHLTE_ZEIT");
        mZielWiederholungen = intent.getStringExtra("GEWÄHLTE_REPS");

        if(!timerSelected){
            mCountdown.setVisibility(View.GONE);
        }
        if(!repsSelected){
            mZielWiederholung.setVisibility(View.GONE);
        }

        if(Objects.equals(mZielzeit, "Keine Angabe")){
            mCountdown.setVisibility(View.INVISIBLE);
        }
        else{
            timerSelected= true;
            mMinuten = mZielzeit.substring(0, 2);
            mSekunden = mZielzeit.substring(3,5);
            Log.d("TAG", mMinuten + ":" + mSekunden);
            mCountdown.setText("Verbleibende Zeit: " +mZielzeit);
            mCountdown.setVisibility(View.VISIBLE);
            mMillisekunden = Integer.parseInt(mMinuten)*60000 + Integer.parseInt(mSekunden)*1000;
        }
        if(Objects.equals(mZielWiederholungen, "Keine Angabe")){
            mZielWiederholung.setVisibility(View.INVISIBLE);
        }
        else{
            repsSelected= true;
            mZielWiederholung.setText(getText(R.string.zielwiederholung) +" " + mZielWiederholungen);
            mZielWiederholung.setVisibility(View.VISIBLE);
        }

        //getData and make it usable
        getDataByID(mUebungsID);
        uebungsname = dataByID.get(1);
        gif = dataByID.get(3);
        beispiel = dataByID.get(10);

        mUebungsname.setText(uebungsname);
        mBeispiel.setText(beispiel);
        mGif.setImageResource(Integer.parseInt(gif));

        mUebungStarten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUebungStarten.setVisibility(View.GONE);
                mUebungBeenden.setVisibility(View.VISIBLE);
                running = true;
                runTimer();
                if(timerSelected){
                    // Time is in millisecond
                    // countdown Interveal is 1sec = 1000 I have used
                    new CountDownTimer(mMillisekunden, 1000) {
                        public void onTick(long millisUntilFinished) {
                            // Used for formatting digit to be in 2 digits only
                            NumberFormat f = new DecimalFormat("00");
                            long min = (millisUntilFinished / 60000) % 60;
                            long sec = (millisUntilFinished / 1000) % 60;
                            mCountdown.setText("Verbleibende Zeit: " + f.format(min) + ":" + f.format(sec));
                        }
                        // When the task is over it will print 00:00 there
                        public void onFinish() {
                            mCountdown.setText("Verbleibende Zeit: 00:00");

                        }
                    }.start();
                }
            }
        });

        mUebungBeenden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDauerInSekunden = seconds;
                running = false;
                if(repsSelected){
                    AlertDialog.Builder repDialog = new AlertDialog.Builder(Aktive_Uebung.this);
                    repDialog.setTitle("Bitte geben Sie die erreichten Wiederholungen an");

                    final EditText repInput = new EditText(Aktive_Uebung.this);
                    repInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                    repDialog.setView(repInput);

                    repDialog.setPositiveButton("Weiter", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mErreichteReps = repInput.getText().toString();
                            done = true;
                            mClose();
                        }
                    });

                    repDialog.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            mErreichteReps = "Keine Angabe";
                            done = true;
                            mClose();
                        }
                    });
                    repDialog.show();
                }
                else {
                    mErreichteReps = "Keine Angabe";
                }
                mClose();
            }

        });
    }

    private void mClose() {
        //does nothing just shortens the closing cycle
        int minutes = (mDauerInSekunden % 3600) / 60;
        int secs = mDauerInSekunden % 60;

        // Format the seconds into hours, minutes,
        // and seconds.
        time = String.format(Locale.getDefault(), "%02d:%02d", minutes, secs);
        mAddToDatabase();
        Toast.makeText(Aktive_Uebung.this, "Speichert...", Toast.LENGTH_SHORT).show();
        if (!repsSelected){
            done = true;
        }
        if (done){
            finish();
        }
    }

    //Database
    private void mAddToDatabase() {
        String mDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        mDBHelper.addHistorie(String.valueOf(mUebungsID), mDate, time, mZielzeit, mErreichteReps, mZielWiederholungen);
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

    // Sets the NUmber of seconds on the timer.
    // The runTimer() method uses a Handler
    // to increment the seconds and
    // update the text view.
    private void runTimer()
    {

        // Get the text view.
        final TextView timeView = (TextView)findViewById(R.id.akt_timer);

        // Creates a new Handler
        final Handler handler = new Handler();

        // Call the post() method,
        // passing in a new Runnable.
        // The post() method processes
        // code without a delay,
        // so the code in the Runnable
        // will run almost immediately.
        handler.post(new Runnable() {
            @Override

            public void run()
            {
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                // Format the seconds into hours, minutes,
                // and seconds.
                String time
                        = String
                        .format(Locale.getDefault(),
                                "%02d:%02d",
                                minutes, secs);

                // Set the text view text.
                timeView.setText(time);

                // If running is true, increment the
                // seconds variable.
                if (running) {
                    seconds++;
                }

                // Post the code again
                // with a delay of 1 second.
                handler.postDelayed(this, 1000);
            }
        });
    }

    //Lifecycle

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        running = false;
        seconds = 0;
    }

    @Override
    protected void onStop() {
        super.onStop();
        running = false;
        seconds = 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        running = false;
        seconds = 0;
    }
}