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
    public static final String COLUMN_DATE = "date";
    public static final String TABLE_HEARTRATE = "heartRate";
    public static final String TABLE_WEIGHT = "weight";
    public static final String TABLE_BODYWATER = "bodywater";
    public static final String TABLE_FATFREEMASS = "fatfreemass";



    //Tabla heartrate creada, hay que hacerlo para las demas
    private static final String DATABASE_CREATE_HEARTRATE = "create table "
            + TABLE_HEARTRATE + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_TYPE
            + " float not null, "+ COLUMN_DATE +" long not null);";

    public SQLiteHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        //faltan todas las demas
        db.execSQL(DATABASE_CREATE_HEARTRATE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*Log.w(SQLiteOpenHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEASURES);
        onCreate(db);*/
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
