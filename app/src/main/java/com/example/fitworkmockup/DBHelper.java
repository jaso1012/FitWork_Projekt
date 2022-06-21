package com.example.fitworkmockup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG ="DatabaseHelper";

    //Deklarationen Übungstabelle
    public static final String TABLE_WORKOUTS = "Übungsliste";
    public static final String COLUMN_NAME_ID = "ID";
    public static final String COLUMN_NAME_UEBUNGSNAME = "Übungsname";
    public static final String COLUMN_NAME_UEBUNGSBESCHREIBUNG = "Beschreibung";
    public static final String COLUMN_NAME_GIF = "GIF"; // den Namen vom GIF wie er unter drawable steht - z.B. R.drawable.bsp_gif
    public static final String COLUMN_NAME_BILD = "Stillbild"; // Aussagekräftiges Bild wählen (heißt bei Kniebeugen  das Bild wenn unten und natürlich nicht im stehen) den Namen vom Bild wie er unter drawable steht - z.B. R.drawable.bsp_gif
    public static final String COLUMN_NAME_KOERPERTEIL = "AnvisierterKörperbereich"; //nur eine Angabe - Ganzkörper auch möglich
    public static final String COLUMN_NAME_SCHWIERIGKEIT = "Schwierigkeit"; //Schwierigkeit als Zahl - 1 = Leicht, 2 = Mittel, 3 = Schwer
    public static final String COLUMN_NAME_WIEDERHOLUNG = "Wiederholung"; // Wenn die Übung mit Wiederholungen ausgeübt wird (wie z.B. Kniebeugen) = true; wenn nicht (z.B. Spazieren) = false
    public static final String COLUMN_NAME_PARTNER = "Partner"; //wieder wenn mit Partner = true, sonst false
    public static final String COLUMN_NAME_VORGABEZEIT = "VorgegebeneZeit";
    public static final String COLUMN_NAME_BEISPIEL = "Beispiel";
    public static final String COLUMN_NAME_ARBEITSZEIT = "Arbeitszeit"; //wenn während Arbeitszeit geeignet = true




    //Deklarationen Tabelle für gespeicherte Übungen
    public static final String TABLE_HISTORIE = "Übungshistorie";
    public static final String COLUMN_NAME_UEBUNGSID = "ÜbungsID";
    public static final String COLUMN_NAME_DATUM = "Datum";
    public static final String COLUMN_NAME_ZEIT = "Zeit";
    public static final String COLUMN_NAME_ZIELZEIT = "ZielZeit";
    public static final String COLUMN_NAME_ERZIELTEWIEDERHOLUNGEN = "ErzielteWiederholungen";
    public static final String COLUMN_NAME_ZIELWIEDERHOLUNGEN = "ZielWiederholungen";

    //Initialisierungsstring Übungstabelle
    public static final String CREATE_TABLE_WORKOUTS = "CREATE TABLE "+ TABLE_WORKOUTS+ "("+  COLUMN_NAME_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            COLUMN_NAME_UEBUNGSNAME+ " TEXT NOT NULL, "+ COLUMN_NAME_UEBUNGSBESCHREIBUNG+ " TEXT NOT NULL, "+ COLUMN_NAME_GIF+ " TEXT NOT NULL, "+
            COLUMN_NAME_BILD+ " TEXT NOT NULL, "+COLUMN_NAME_KOERPERTEIL+ " TEXT NOT NULL, "+ COLUMN_NAME_SCHWIERIGKEIT+ " TEXT NOT NULL, "+
            COLUMN_NAME_WIEDERHOLUNG+ " TEXT NOT NULL, "+ COLUMN_NAME_PARTNER+ " TEXT NOT NULL, "+  COLUMN_NAME_VORGABEZEIT+ " TEXT NOT NULL, "+
            COLUMN_NAME_BEISPIEL+ " TEXT NOT NULL, "+COLUMN_NAME_ARBEITSZEIT+ " TEXT NOT NULL);";

    //Init-String Tabelle für gespeicherte Übungen
    public static final String CREATE_TABLE_HISTORIE= "CREATE TABLE "+ TABLE_HISTORIE+ "("+  COLUMN_NAME_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            COLUMN_NAME_UEBUNGSID+ " TEXT NOT NULL, "+ COLUMN_NAME_DATUM+ " TEXT NOT NULL, "+ COLUMN_NAME_ZEIT+ " TEXT NOT NULL, "+
            COLUMN_NAME_ZIELZEIT+ " TEXT NOT NULL, "+ COLUMN_NAME_ERZIELTEWIEDERHOLUNGEN+ " TEXT NOT NULL, "+ COLUMN_NAME_ZIELWIEDERHOLUNGEN+ " TEXT NOT NULL);";


    private static final String _DB_FILE_NAME = "locations.db";
    public DBHelper(@Nullable Context context){super(context, _DB_FILE_NAME, null, 1);} //nach Austausch von Beispielen gegen echte Übungen version zu 2 ändern


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_WORKOUTS);
        db.execSQL(CREATE_TABLE_HISTORIE);
        addUebungen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORIE);
        onCreate(db);
    }

    public Cursor getUebungenAlphabetically() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_WORKOUTS + " ORDER BY " + COLUMN_NAME_UEBUNGSNAME + " ASC";
        Cursor data = db.rawQuery(query,null);
        return data;
    }

    public Cursor getUebungenByID(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_WORKOUTS + " WHERE " + COLUMN_NAME_ID + "= " + id;
        return db.rawQuery(query, null);
    }

    public void addUebungen(SQLiteDatabase db){
        //erstellen Strings für betätigtes Körperteil - vermeidet Tippfehler beim Einfügen
        String mGanzkoerper = "Ganzkoerper"; String mBeine = "Beine"; String mArme = "Arme"; String mMental = "Mental";
        String mRuecken = "Ruecken";

        //selbes für true/false
        String mTrue = "true"; String mFalse = "false";

        //Übung 1
        int ansagen_bild = R.drawable.ansagen_bild;
        int ansagen_gif = R.drawable.ansagen_gif;
        ContentValues values1 = new ContentValues();
        values1.put(COLUMN_NAME_UEBUNGSNAME, "Koordination");
        values1.put(COLUMN_NAME_UEBUNGSBESCHREIBUNG, "Im Stand werden Arme und Beine in bestimmten Variationen entweder auf derselben oder der gegenüberliegenden Körperseite nach Ansage bewegt");
        values1.put(COLUMN_NAME_GIF, Integer.toString(ansagen_gif)); //echt auf Tippfehler aufpassen - am besten Copy&Paste vom Namen und dann ".gif" löschen
        values1.put(COLUMN_NAME_BILD, Integer.toString(ansagen_bild)); //siehe GIF
        values1.put(COLUMN_NAME_KOERPERTEIL, mGanzkoerper);
        values1.put(COLUMN_NAME_SCHWIERIGKEIT, "1-2"); //Schwierigkeit als Zahl - 1 = Leicht, 2 = Mittel, 3 = Schwer
        values1.put(COLUMN_NAME_WIEDERHOLUNG, mFalse); // Wenn die Übung mit Wiederholungen ausgeübt wird (wie z.B. Kniebeugen) = true; wenn nicht (z.B. Spazieren) = false
        values1.put(COLUMN_NAME_PARTNER, mTrue);
        values1.put(COLUMN_NAME_VORGABEZEIT, "1-5 Minuten"); //einfach als String
        values1.put(COLUMN_NAME_BEISPIEL, "Blahhhhhhhh");
        values1.put(COLUMN_NAME_ARBEITSZEIT, mTrue); //wenn während Arbeitszeit geeignet = true
        db.insert(TABLE_WORKOUTS, null, values1);

        //Übung 2
        int armdruecken_bild = R.drawable.armdruecken_bild;
        int armdruecken_gif = R.drawable.armdruecken_gif;
        ContentValues values2 = new ContentValues();
        values2.put(COLUMN_NAME_UEBUNGSNAME, "Armdrücken");
        values2.put(COLUMN_NAME_UEBUNGSBESCHREIBUNG, "Beide Personen sitzen sich an einem Tisch direkt gegenüber. Ihre Ellbogen liegen auf der Tischplatte auf und dürfen diese während des gesamten Duells nicht verlassen.");
        values2.put(COLUMN_NAME_GIF, Integer.toString(armdruecken_gif)); //echt auf Tippfehler aufpassen - am besten Copy&Paste vom Namen und dann ".gif" löschen
        values2.put(COLUMN_NAME_BILD, Integer.toString(armdruecken_bild)); //siehe GIF
        values2.put(COLUMN_NAME_KOERPERTEIL, mArme);
        values2.put(COLUMN_NAME_SCHWIERIGKEIT, "2"); //Schwierigkeit als Zahl - 1 = Leicht, 2 = Mittel, 3 = Schwer
        values2.put(COLUMN_NAME_WIEDERHOLUNG, mFalse); // Wenn die Übung mit Wiederholungen ausgeübt wird (wie z.B. Kniebeugen) = true; wenn nicht (z.B. Spazieren) = false
        values2.put(COLUMN_NAME_PARTNER, mTrue);
        db.insert(TABLE_WORKOUTS, null, values2);

        //Übung 3
        int atmen_bild = R.drawable.atmen_bild;
        int atmen_gif = R.drawable.atmen_gif;
        ContentValues values3 = new ContentValues();
        values3.put(COLUMN_NAME_UEBUNGSNAME, "Achtsames Atmen");
        values3.put(COLUMN_NAME_UEBUNGSBESCHREIBUNG, "Tief und ruhig in die Bauchgegend atmen");
        values3.put(COLUMN_NAME_GIF, Integer.toString(atmen_gif)); //echt auf Tippfehler aufpassen - am besten Copy&Paste vom Namen und dann ".gif" löschen
        values3.put(COLUMN_NAME_BILD, Integer.toString(atmen_bild)); //siehe GIF
        values3.put(COLUMN_NAME_KOERPERTEIL, mRuecken);
        values3.put(COLUMN_NAME_SCHWIERIGKEIT, "2"); //Schwierigkeit als Zahl - 1 = Leicht, 2 = Mittel, 3 = Schwer
        values3.put(COLUMN_NAME_WIEDERHOLUNG, mTrue); // Wenn die Übung mit Wiederholungen ausgeübt wird (wie z.B. Kniebeugen) = true; wenn nicht (z.B. Spazieren) = false
        values3.put(COLUMN_NAME_PARTNER, mFalse);
        db.insert(TABLE_WORKOUTS, null, values3);

        //Übung 4
        int baum_liegestuetz_bild = R.drawable.baum_liegestuetz_bild;
        int baum_liegestuetze_gif = R.drawable.baum_liegestuetze_gif;
        ContentValues values4 = new ContentValues();
        values4.put(COLUMN_NAME_UEBUNGSNAME, "Baum Liegestütze");
        values4.put(COLUMN_NAME_UEBUNGSBESCHREIBUNG, "Blah");
        values4.put(COLUMN_NAME_GIF, Integer.toString(baum_liegestuetze_gif)); //echt auf Tippfehler aufpassen - am besten Copy&Paste vom Namen und dann ".gif" löschen
        values4.put(COLUMN_NAME_BILD, Integer.toString(baum_liegestuetz_bild)); //siehe GIF
        values4.put(COLUMN_NAME_KOERPERTEIL, mArme);
        values4.put(COLUMN_NAME_SCHWIERIGKEIT, "2"); //Schwierigkeit als Zahl - 1 = Leicht, 2 = Mittel, 3 = Schwer
        values4.put(COLUMN_NAME_WIEDERHOLUNG, mTrue); // Wenn die Übung mit Wiederholungen ausgeübt wird (wie z.B. Kniebeugen) = true; wenn nicht (z.B. Spazieren) = false
        values4.put(COLUMN_NAME_PARTNER, mTrue);
        db.insert(TABLE_WORKOUTS, null, values4);

        //Übung 5
        int bild5 = R.drawable.beinschwung_bild;
        int gif5 = R.drawable.beinschwung;
        ContentValues values5 = new ContentValues();
        values5.put(COLUMN_NAME_UEBUNGSNAME, "Übung 5");
        values5.put(COLUMN_NAME_UEBUNGSBESCHREIBUNG, "Blah");
        values5.put(COLUMN_NAME_GIF, Integer.toString(gif5)); //echt auf Tippfehler aufpassen - am besten Copy&Paste vom Namen und dann ".gif" löschen
        values5.put(COLUMN_NAME_BILD, Integer.toString(bild5)); //siehe GIF
        values5.put(COLUMN_NAME_KOERPERTEIL, mMental);
        values5.put(COLUMN_NAME_SCHWIERIGKEIT, "2"); //Schwierigkeit als Zahl - 1 = Leicht, 2 = Mittel, 3 = Schwer
        values5.put(COLUMN_NAME_WIEDERHOLUNG, mFalse); // Wenn die Übung mit Wiederholungen ausgeübt wird (wie z.B. Kniebeugen) = true; wenn nicht (z.B. Spazieren) = false
        values5.put(COLUMN_NAME_PARTNER, mTrue);
        db.insert(TABLE_WORKOUTS, null, values5);

        //Übung 6
        int bild6 = R.drawable.dehnen_bild;
        int gif6 = R.drawable.dehnen_gif;
        ContentValues values6 = new ContentValues();
        values6.put(COLUMN_NAME_UEBUNGSNAME, "Übung 6");
        values6.put(COLUMN_NAME_UEBUNGSBESCHREIBUNG, "Blah");
        values6.put(COLUMN_NAME_GIF, Integer.toString(gif6)); //echt auf Tippfehler aufpassen - am besten Copy&Paste vom Namen und dann ".gif" löschen
        values6.put(COLUMN_NAME_BILD, Integer.toString(bild6)); //siehe GIF
        values6.put(COLUMN_NAME_KOERPERTEIL, mBeine);
        values6.put(COLUMN_NAME_SCHWIERIGKEIT, "1"); //Schwierigkeit als Zahl - 1 = Leicht, 2 = Mittel, 3 = Schwer
        values6.put(COLUMN_NAME_WIEDERHOLUNG, mFalse); // Wenn die Übung mit Wiederholungen ausgeübt wird (wie z.B. Kniebeugen) = true; wenn nicht (z.B. Spazieren) = false
        values6.put(COLUMN_NAME_PARTNER, mFalse);
        db.insert(TABLE_WORKOUTS, null, values6);

        //Übung 7
        int bild7 = R.drawable.handschalgen_bild;
        int gif7 = R.drawable.handschlagen_gif;
        ContentValues values7 = new ContentValues();
        values7.put(COLUMN_NAME_UEBUNGSNAME, "Übung 7");
        values7.put(COLUMN_NAME_UEBUNGSBESCHREIBUNG, "Blah");
        values7.put(COLUMN_NAME_GIF, Integer.toString(gif7)); //echt auf Tippfehler aufpassen - am besten Copy&Paste vom Namen und dann ".gif" löschen
        values7.put(COLUMN_NAME_BILD, Integer.toString(bild7)); //siehe GIF
        values7.put(COLUMN_NAME_KOERPERTEIL, mBeine);
        values7.put(COLUMN_NAME_SCHWIERIGKEIT, "3"); //Schwierigkeit als Zahl - 1 = Leicht, 2 = Mittel, 3 = Schwer
        values7.put(COLUMN_NAME_WIEDERHOLUNG, mFalse); // Wenn die Übung mit Wiederholungen ausgeübt wird (wie z.B. Kniebeugen) = true; wenn nicht (z.B. Spazieren) = false
        values7.put(COLUMN_NAME_PARTNER, mFalse);
        db.insert(TABLE_WORKOUTS, null, values7);

        //Übung 8
        int bild8 = R.drawable.rueckensitzen_bild;
        int gif8 = R.drawable.rueckensitzen_gif;
        ContentValues values8 = new ContentValues();
        values8.put(COLUMN_NAME_UEBUNGSNAME, "Übung 8");
        values8.put(COLUMN_NAME_UEBUNGSBESCHREIBUNG, "Blah");
        values8.put(COLUMN_NAME_GIF, Integer.toString(gif8)); //echt auf Tippfehler aufpassen - am besten Copy&Paste vom Namen und dann ".gif" löschen
        values8.put(COLUMN_NAME_BILD, Integer.toString(bild8)); //siehe GIF
        values8.put(COLUMN_NAME_KOERPERTEIL, mRuecken);
        values8.put(COLUMN_NAME_SCHWIERIGKEIT, "1"); //Schwierigkeit als Zahl - 1 = Leicht, 2 = Mittel, 3 = Schwer
        values8.put(COLUMN_NAME_WIEDERHOLUNG, mTrue); // Wenn die Übung mit Wiederholungen ausgeübt wird (wie z.B. Kniebeugen) = true; wenn nicht (z.B. Spazieren) = false
        values8.put(COLUMN_NAME_PARTNER, mFalse);
        db.insert(TABLE_WORKOUTS, null, values8);

        //Übung 9
        int bild9 = R.drawable.trizeps_bild;
        int gif9 = R.drawable.trizeps_gif;
        ContentValues values9 = new ContentValues();
        values9.put(COLUMN_NAME_UEBUNGSNAME, "Übung 9");
        values9.put(COLUMN_NAME_UEBUNGSBESCHREIBUNG, "Blah");
        values9.put(COLUMN_NAME_GIF, Integer.toString(gif9)); //echt auf Tippfehler aufpassen - am besten Copy&Paste vom Namen und dann ".gif" löschen
        values9.put(COLUMN_NAME_BILD, Integer.toString(bild9)); //siehe GIF
        values9.put(COLUMN_NAME_KOERPERTEIL, mBeine);
        values9.put(COLUMN_NAME_SCHWIERIGKEIT, "2"); //Schwierigkeit als Zahl - 1 = Leicht, 2 = Mittel, 3 = Schwer
        values9.put(COLUMN_NAME_WIEDERHOLUNG, mFalse); // Wenn die Übung mit Wiederholungen ausgeübt wird (wie z.B. Kniebeugen) = true; wenn nicht (z.B. Spazieren) = false
        values9.put(COLUMN_NAME_PARTNER, mTrue);
        db.insert(TABLE_WORKOUTS, null, values9);

        //Übung 10
        int bild10 = R.drawable.werfen_bild;
        int gif10 = R.drawable.werfen_gif;
        int gif11 = R.drawable.werfen_variation_gif;
        ContentValues values10 = new ContentValues();
        values10.put(COLUMN_NAME_UEBUNGSNAME, "Übung 10");
        values10.put(COLUMN_NAME_UEBUNGSBESCHREIBUNG, "Blah");
        values10.put(COLUMN_NAME_GIF, Integer.toString(gif10)); //echt auf Tippfehler aufpassen - am besten Copy&Paste vom Namen und dann ".gif" löschen
        values10.put(COLUMN_NAME_BILD, Integer.toString(bild10)); //siehe GIF
        values10.put(COLUMN_NAME_KOERPERTEIL, mGanzkoerper);
        values10.put(COLUMN_NAME_SCHWIERIGKEIT, "1"); //Schwierigkeit als Zahl - 1 = Leicht, 2 = Mittel, 3 = Schwer
        values10.put(COLUMN_NAME_WIEDERHOLUNG, mTrue); // Wenn die Übung mit Wiederholungen ausgeübt wird (wie z.B. Kniebeugen) = true; wenn nicht (z.B. Spazieren) = false
        values10.put(COLUMN_NAME_PARTNER, mFalse);
        db.insert(TABLE_WORKOUTS, null, values10);
    }

    /*public void insertImg(int id , Bitmap img ) {


        byte[] data = getBitmapAsByteArray(img); // this is a function

        insertStatement_logo.bindLong(1, id);
        insertStatement_logo.bindBlob(2, data);

        insertStatement_logo.executeInsert();
        insertStatement_logo.clearBindings() ;

    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }*/

}