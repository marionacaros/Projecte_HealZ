package pae.healz.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marc on 27/04/2016.
 */
public class DataSourceDAO {
    /*This class maintains the database connection an suppports adding new information*/

    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = { SQLiteHelper.COLUMN_ID, SQLiteHelper.COLUMN_TYPE };

    public DataSourceDAO(Context context){
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    //Pillamos el Type de ModelClassSQL
    public void addparameters(ModelClassSQL model){
        Atribute atr=model.getAtribute();
        ContentValues values= new ContentValues();
        values.put(SQLiteHelper.COLUMN_TYPE,atr.var);
        values.put(SQLiteHelper.COLUMN_TYPE,atr.date);

        if(model.type==0) atr.id = database.insert(SQLiteHelper.TABLE_HEARTRATE, null, values);
        else if(model.type==1)atr.id = database.insert(SQLiteHelper.TABLE_FATFREEMASS, null, values);
        else if(model.type==2)atr.id = database.insert(SQLiteHelper.TABLE_BODYWATER, null, values);
        else atr.id = database.insert(SQLiteHelper.TABLE_WEIGHT, null, values);
    }

    public List<Atribute> getAtributes(int type) {
        List<Atribute> atributes = new ArrayList<Atribute>();

        Cursor cursor = null;
        if(type==0)cursor=database.query(SQLiteHelper.TABLE_HEARTRATE,allColumns, null, null, null, null, null);
        else if(type==1)cursor=database.query(SQLiteHelper.TABLE_FATFREEMASS,allColumns, null, null, null, null, null);
        else if(type==2)cursor=database.query(SQLiteHelper.TABLE_BODYWATER,allColumns, null, null, null, null, null);
        else cursor=database.query(SQLiteHelper.TABLE_WEIGHT,allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Atribute atribute = cursorToAtribute(cursor);
            atributes.add(atribute);
            cursor.moveToNext();//Pasas a la siguiente fila de la tabla
        }
        // make sure to close the cursor
        cursor.close();
        return atributes;
    }

    private Atribute cursorToAtribute(Cursor cursor){

        Atribute atr = new Atribute(cursor.getLong(0),cursor.getFloat(1),cursor.getLong(2));

        return atr;


    }


}
