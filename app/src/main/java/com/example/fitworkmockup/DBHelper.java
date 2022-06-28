package com.example.fitworkmockup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

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

    public boolean addHistorie(/*String ID, */String UebungsID, String Datum, String Zeit,
                                          String Zielzeit, String Erzieltewiederhoholungen, String Zielwiederholungen) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_UEBUNGSID, UebungsID);
        contentValues.put(COLUMN_NAME_DATUM, Datum);
        contentValues.put(COLUMN_NAME_ZEIT, Zeit);
        contentValues.put(COLUMN_NAME_ZIELZEIT, Zielzeit);
        contentValues.put(COLUMN_NAME_ERZIELTEWIEDERHOLUNGEN, Erzieltewiederhoholungen);
        contentValues.put(COLUMN_NAME_ZIELWIEDERHOLUNGEN, Zielwiederholungen);

        long result = db.insert(TABLE_HISTORIE, null, contentValues);

        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public Cursor getHistoryByDate(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_HISTORIE + " ORDER BY " + COLUMN_NAME_DATUM + " DESC";
        Cursor data = db.rawQuery(query,null);
        return data;
    }

    public void deleteDataByID(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Define 'where' part of query.
        String selection = COLUMN_NAME_ID + "=" + id;
        // Issue SQL statement.
        int deletedRows = db.delete(TABLE_HISTORIE, selection, null);
    }

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Define 'where' part of query.
        db.execSQL("delete from " + TABLE_HISTORIE);
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
        values1.put(COLUMN_NAME_SCHWIERIGKEIT, "3"); //Schwierigkeit als Zahl - 1 = Leicht, 2 = Mittel, 3 = Schwer
        values1.put(COLUMN_NAME_WIEDERHOLUNG, mFalse); // Wenn die Übung mit Wiederholungen ausgeübt wird (wie z.B. Kniebeugen) = true; wenn nicht (z.B. Spazieren) = false
        values1.put(COLUMN_NAME_PARTNER, mTrue);
        values1.put(COLUMN_NAME_VORGABEZEIT, "1-5 Minuten"); //einfach als String
        values1.put(COLUMN_NAME_BEISPIEL, "z.B. Rechter Arm nach oben und linkes Bein zur Seite im Wechsel mit linker Arm nach oben und rechtes Bein zur Seite");
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
        values2.put(COLUMN_NAME_VORGABEZEIT, "1-5 Minuten"); //einfach als String
        values2.put(COLUMN_NAME_BEISPIEL, "Armdrücken mit parallel auf dem Ellenbogen stehenden Oberarmen ");
        values2.put(COLUMN_NAME_ARBEITSZEIT, mTrue); //wenn während Arbeitszeit geeignet = true
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
        values3.put(COLUMN_NAME_VORGABEZEIT, "2-5 Minuten"); //einfach als String
        values3.put(COLUMN_NAME_BEISPIEL, "Achtsam Atmen ");
        values3.put(COLUMN_NAME_ARBEITSZEIT, mTrue); //wenn während Arbeitszeit geeignet = true
        db.insert(TABLE_WORKOUTS, null, values3);

        //Übung 4
        int baum_liegestuetz_bild = R.drawable.baum_liegestuetz_bild;
        int baum_liegestuetze_gif = R.drawable.baum_liegestuetze_gif;
        ContentValues values4 = new ContentValues();
        values4.put(COLUMN_NAME_UEBUNGSNAME, "Baum Liegestütze");
        values4.put(COLUMN_NAME_UEBUNGSBESCHREIBUNG, "Vor einem Baum und einer Wand stehend (mit ca. 20-30 cm Abstand) werden die Handflächen auf den Baum/ die Wand gelegt. Anschließend werden Liegestützen gemacht, indem die Ellenbogen gebeugt und dernKopf in die Richtung der Hände gestreckt wird während der Rücken gerade bleibt. Anschließend drückt man sich durch Ausstrecken der Arme zurück in die Ausgangsposition. ");
        values4.put(COLUMN_NAME_GIF, Integer.toString(baum_liegestuetze_gif)); //echt auf Tippfehler aufpassen - am besten Copy&Paste vom Namen und dann ".gif" löschen
        values4.put(COLUMN_NAME_BILD, Integer.toString(baum_liegestuetz_bild)); //siehe GIF
        values4.put(COLUMN_NAME_KOERPERTEIL, mArme);
        values4.put(COLUMN_NAME_SCHWIERIGKEIT, "2"); //Schwierigkeit als Zahl - 1 = Leicht, 2 = Mittel, 3 = Schwer
        values4.put(COLUMN_NAME_WIEDERHOLUNG, mTrue); // Wenn die Übung mit Wiederholungen ausgeübt wird (wie z.B. Kniebeugen) = true; wenn nicht (z.B. Spazieren) = false
        values4.put(COLUMN_NAME_PARTNER, mFalse);
        values4.put(COLUMN_NAME_VORGABEZEIT, "1-5 Minuten"); //einfach als String
        values4.put(COLUMN_NAME_BEISPIEL, "Liegestützen vertikal an einem Baum oder einer Wand.");
        values4.put(COLUMN_NAME_ARBEITSZEIT, mTrue); //wenn während Arbeitszeit geeignet = true
        db.insert(TABLE_WORKOUTS, null, values4);

        //Übung 5
        int beinschwung_bild = R.drawable.beinschwung_bild;
        int beinschwung = R.drawable.beinschwung;
        ContentValues values5 = new ContentValues();
        values5.put(COLUMN_NAME_UEBUNGSNAME, "Beinschwung");
        values5.put(COLUMN_NAME_UEBUNGSBESCHREIBUNG, "Auf dem linken Bein stehen, den rechten Arm und das rechte Bein vor- und zurückschingen. Danach andersherum (linkes Bein, linker Arm schwingen).");
        values5.put(COLUMN_NAME_GIF, Integer.toString(beinschwung)); //echt  auf Tippfehler aufpassen - am besten Copy&Paste vom Namen und dann ".gif" löschen
        values5.put(COLUMN_NAME_BILD, Integer.toString(beinschwung_bild)); //siehe GIF
        values5.put(COLUMN_NAME_KOERPERTEIL, mGanzkoerper);
        values5.put(COLUMN_NAME_SCHWIERIGKEIT, "1"); //Schwierigkeit als Zahl - 1 = Leicht, 2 = Mittel, 3 = Schwer
        values5.put(COLUMN_NAME_WIEDERHOLUNG, mTrue); // Wenn die Übung mit Wiederholungen ausgeübt wird (wie z.B. Kniebeugen) = true; wenn nicht (z.B. Spazieren) = false
        values5.put(COLUMN_NAME_PARTNER, mFalse);
        values5.put(COLUMN_NAME_VORGABEZEIT, "2-3 Minuten"); //einfach als String
        values5.put(COLUMN_NAME_BEISPIEL, "Im Einbeinstand jeweils das freie Bein und den Arm der gleichen Seite vor- und zurückschwingen.");
        values5.put(COLUMN_NAME_ARBEITSZEIT, mTrue); //wenn während Arbeitszeit geeignet = true
        db.insert(TABLE_WORKOUTS, null, values5);

        //Übung 6
        int dehnen_bild = R.drawable.dehnen_bild;
        int dehnen_gif = R.drawable.dehnen_gif;
        ContentValues values6 = new ContentValues();
        values6.put(COLUMN_NAME_UEBUNGSNAME, "Dehnen");
        values6.put(COLUMN_NAME_UEBUNGSBESCHREIBUNG, "1. Füße parallel mit ca 40cm Abstand, Rücken gerade halten, Knie langsam nach außen bewegen. " +
                "2. Füße parallel hintereinander mit ca 20cm Abstand, Knie langsam nach vorne bewegen." +
                "3. Hände hinter dem Rücken von oben und unte");
        values6.put(COLUMN_NAME_GIF, Integer.toString(dehnen_gif)); //echt auf Tippfehler aufpassen - am besten Copy&Paste vom Namen und dann ".gif" löschen
        values6.put(COLUMN_NAME_BILD, Integer.toString(dehnen_bild)); //siehe GIF
        values6.put(COLUMN_NAME_KOERPERTEIL, mGanzkoerper);
        values6.put(COLUMN_NAME_SCHWIERIGKEIT, "1"); //Schwierigkeit als Zahl - 1 = Leicht, 2 = Mittel, 3 = Schwer
        values6.put(COLUMN_NAME_WIEDERHOLUNG, mFalse); // Wenn die Übung mit Wiederholungen ausgeübt wird (wie z.B. Kniebeugen) = true; wenn nicht (z.B. Spazieren) = false
        values6.put(COLUMN_NAME_PARTNER, mFalse);
        values6.put(COLUMN_NAME_VORGABEZEIT, "2-5 Minuten"); //einfach als String
        values6.put(COLUMN_NAME_BEISPIEL, "Verschiedenen Dehnübungen.");
        values6.put(COLUMN_NAME_ARBEITSZEIT, mTrue); //wenn während Arbeitszeit geeignet = true
        db.insert(TABLE_WORKOUTS, null, values6);

        //Übung 7
        int handschalgen_bild = R.drawable.handschalgen_bild;
        int handschlagen_gif = R.drawable.handschlagen_gif;
        ContentValues values7 = new ContentValues();
        values7.put(COLUMN_NAME_UEBUNGSNAME, "Handschlagen");
        values7.put(COLUMN_NAME_UEBUNGSBESCHREIBUNG, "Beide Hände mit den Handinnenflächen aneinander, den Daumen oben und den Fingerspizten vom Körper weg vor den Körper halten. Die andere Person stellt sich direkt davor und macht die gleiche Geste, sodass sich die Fingerspitzen berühren. Eine Person beginnt und versucht die Hände der anderen Person zwischen ihren eigenen Handflächen zu fangen. Wenn die andere Person ihre Hände früh genug wegzieht klatscht die erste Person is leere. Wenn die andere Person nicht schnell genug reagiert und ihre Hände \"gefangen\" werden ist sie an der Reihe die Hände ihres*ihrer Mitspieler*in zu fangen. So geht das hin und her.");
        values7.put(COLUMN_NAME_GIF, Integer.toString(handschlagen_gif)); //echt auf Tippfehler aufpassen - am besten Copy&Paste vom Namen und dann ".gif" löschen
        values7.put(COLUMN_NAME_BILD, Integer.toString(handschalgen_bild)); //siehe GIF
        values7.put(COLUMN_NAME_KOERPERTEIL, mMental);
        values7.put(COLUMN_NAME_SCHWIERIGKEIT, "1"); //Schwierigkeit als Zahl - 1 = Leicht, 2 = Mittel, 3 = Schwer
        values7.put(COLUMN_NAME_WIEDERHOLUNG, mFalse); // Wenn die Übung mit Wiederholungen ausgeübt wird (wie z.B. Kniebeugen) = true; wenn nicht (z.B. Spazieren) = false
        values7.put(COLUMN_NAME_PARTNER, mTrue);
        values7.put(COLUMN_NAME_VORGABEZEIT, "2-5 Minuten"); //einfach als String
        values7.put(COLUMN_NAME_BEISPIEL, "Durch Klatschen müssen die Hände des gegenspielers gefangen werden");
        values7.put(COLUMN_NAME_ARBEITSZEIT, mTrue); //wenn während Arbeitszeit geeignet = true
        db.insert(TABLE_WORKOUTS, null, values7);

        //Übung 8
        int rueckensitzen_bild = R.drawable.rueckensitzen_bild;
        int rueckensitzen_gif = R.drawable.rueckensitzen_gif;
        ContentValues values8 = new ContentValues();
        values8.put(COLUMN_NAME_UEBUNGSNAME, "Rückensitzen");
        values8.put(COLUMN_NAME_UEBUNGSBESCHREIBUNG, "Wie Wandsitzen, nur das Wand durch Person in selber Haltung ersetzt wird");
        values8.put(COLUMN_NAME_GIF, Integer.toString(rueckensitzen_gif)); //echt auf Tippfehler aufpassen - am besten Copy&Paste vom Namen und dann ".gif" löschen
        values8.put(COLUMN_NAME_BILD, Integer.toString(rueckensitzen_bild)); //siehe GIF
        values8.put(COLUMN_NAME_KOERPERTEIL, mGanzkoerper);
        values8.put(COLUMN_NAME_SCHWIERIGKEIT, "2"); //Schwierigkeit als Zahl - 1 = Leicht, 2 = Mittel, 3 = Schwer
        values8.put(COLUMN_NAME_WIEDERHOLUNG, mFalse); // Wenn die Übung mit Wiederholungen ausgeübt wird (wie z.B. Kniebeugen) = true; wenn nicht (z.B. Spazieren) = false
        values8.put(COLUMN_NAME_PARTNER, mTrue);
        values8.put(COLUMN_NAME_VORGABEZEIT, "1-2 Minuten"); //einfach als String
        values8.put(COLUMN_NAME_BEISPIEL, "Durch halten werden Rücken- und Beinmuskulatur gestärkt");
        values8.put(COLUMN_NAME_ARBEITSZEIT, mTrue); //wenn während Arbeitszeit geeignet = true
        db.insert(TABLE_WORKOUTS, null, values8);

        //Übung 9
        int trizeps_bild = R.drawable.trizeps_bild;
        int trizeps_gif = R.drawable.trizeps_gif;
        ContentValues values9 = new ContentValues();
        values9.put(COLUMN_NAME_UEBUNGSNAME, "Tischplatten Trizeps");
        values9.put(COLUMN_NAME_UEBUNGSBESCHREIBUNG, "Durch Druck auf die Tischplatte wird der Trizeps trainiert");
        values9.put(COLUMN_NAME_GIF, Integer.toString(trizeps_gif)); //echt auf Tippfehler aufpassen - am besten Copy&Paste vom Namen und dann ".gif" löschen
        values9.put(COLUMN_NAME_BILD, Integer.toString(trizeps_bild)); //siehe GIF
        values9.put(COLUMN_NAME_KOERPERTEIL, mArme);
        values9.put(COLUMN_NAME_SCHWIERIGKEIT, "1"); //Schwierigkeit als Zahl - 1 = Leicht, 2 = Mittel, 3 = Schwer
        values9.put(COLUMN_NAME_WIEDERHOLUNG, mTrue); // Wenn die Übung mit Wiederholungen ausgeübt wird (wie z.B. Kniebeugen) = true; wenn nicht (z.B. Spazieren) = false
        values9.put(COLUMN_NAME_PARTNER, mFalse);
        values9.put(COLUMN_NAME_VORGABEZEIT, "1-2 Minuten"); //einfach als String
        values9.put(COLUMN_NAME_BEISPIEL, "Im Bürostuhl aufrecht hinsetzen. Die Unterarme liegen parallel auf der Tischplatte und die Finger zeigen nach vorne. Drücken Sie nun die Unterarme kräftig gegen den Tisch und halten die Spannung");
        values9.put(COLUMN_NAME_ARBEITSZEIT, mTrue); //wenn während Arbeitszeit geeignet = true
        db.insert(TABLE_WORKOUTS, null, values9);

        //Übung 10
        int werfen_bild = R.drawable.werfen_bild;
        int werfen_variation_gif = R.drawable.werfen_variation_gif;
        ContentValues values10 = new ContentValues();
        values10.put(COLUMN_NAME_UEBUNGSNAME, "Zuwerfen");
        values10.put(COLUMN_NAME_UEBUNGSBESCHREIBUNG, "Beliebige Gegenstände werden zwischen Personen hin- und her geworfen");
        values10.put(COLUMN_NAME_GIF, Integer.toString(werfen_variation_gif)); //echt auf Tippfehler aufpassen - am besten Copy&Paste vom Namen und dann ".gif" löschen
        values10.put(COLUMN_NAME_BILD, Integer.toString(werfen_bild)); //siehe GIF
        values10.put(COLUMN_NAME_KOERPERTEIL, mMental);
        values10.put(COLUMN_NAME_SCHWIERIGKEIT, "1"); //Schwierigkeit als Zahl - 1 = Leicht, 2 = Mittel, 3 = Schwer
        values10.put(COLUMN_NAME_WIEDERHOLUNG, mTrue); // Wenn die Übung mit Wiederholungen ausgeübt wird (wie z.B. Kniebeugen) = true; wenn nicht (z.B. Spazieren) = false
        values10.put(COLUMN_NAME_PARTNER, mTrue);
        values10.put(COLUMN_NAME_VORGABEZEIT, "2-3 Minuten"); //einfach als String
        values10.put(COLUMN_NAME_BEISPIEL, "Person A wirft Person B einen Bleistift zu und gibt Befehl mit welcher Hand dieser gefangen werden soll. Variation: Gegenstand und Anzahl dieser kann variiert werden, sowie Name des Befehls, Art des Fangens und des Werfens");
        values10.put(COLUMN_NAME_ARBEITSZEIT, mFalse); //wenn während Arbeitszeit geeignet = true
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