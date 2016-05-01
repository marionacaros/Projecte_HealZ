package pae.healz.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Marc on 27/04/2016.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Data.db";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TYPE = "measuresType";
    public static final String COLUMN_FECHA = "fecha";
    public static final String TABLE_HEARTRATE = "heartRate";
    public static final String TABLE_WEIGHT = "weight";
    public static final String TABLE_BODYWATER = "bodywater";
    public static final String TABLE_FATFREEMASS = "fatfreemass";

    //Taula per fer proves
    public static final String TABLE_AUX = "tablaproba";



    //Declaracion de las tablas en DB
    private static final String DATABASE_CREATE_HEARTRATE = "create table if not exists "
            + TABLE_HEARTRATE + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TYPE
            + " float not null, "+ COLUMN_FECHA +" long not null);";
    private static final String DATABASE_CREATE_WEIGHT = "create table if not exists "
            + TABLE_WEIGHT + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TYPE
            + " float not null, "+ COLUMN_FECHA +" long not null);";
    private static final String DATABASE_CREATE_BODYWATER = "create table if not exists "
            + TABLE_BODYWATER + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TYPE
            + " float not null, "+ COLUMN_FECHA +" long not null);";
    private static final String DATABASE_CREATE_FATFREEMASS = "create table if not exists "
            + TABLE_FATFREEMASS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TYPE
            + " float not null, "+ COLUMN_FECHA +" long not null);";

    //Taula per treballar
    private static final String DATABASE_CREATE_AUX = "create table if not exists "
            + TABLE_AUX + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TYPE
            + " float not null, "+ COLUMN_FECHA +" long not null);";

    public SQLiteHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public void onCreate(SQLiteDatabase db){
        //Inicializacion de las tablas
        db.execSQL(DATABASE_CREATE_HEARTRATE);
        db.execSQL(DATABASE_CREATE_WEIGHT);
        db.execSQL(DATABASE_CREATE_BODYWATER);
        db.execSQL(DATABASE_CREATE_FATFREEMASS);
        db.execSQL(DATABASE_CREATE_AUX);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*Log.w(SQLiteOpenHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHT);
        onCreate(db);*/
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
