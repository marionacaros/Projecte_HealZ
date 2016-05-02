package pae.healz.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import pae.healz.R;
import pae.healz.SQLite.Atribute;
import pae.healz.SQLite.DataSourceDAO;
import pae.healz.SQLite.ModelClassSQL;
import pae.healz.UserData.User;

/**
 * Created by Marc on 30/04/2016.
 */
public class Proba_Database extends AppCompatActivity {

    private EditText dataLayout, viewFromDB, viewFromSP;
    private Button buttonDBSave, buttonDBview, buttonSPview, buttonSPSave;
    private String infolayout = null;
    private SharedPreferences probaSP;
    //Informacio per la BD
    private ModelClassSQL model;
    private DataSourceDAO BD;
    private List<Atribute> atr = null;
    private float data = 0;
    private long date = System.currentTimeMillis();
    private Atribute atribute=null;





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proba_to_database);

        //Inicialitzacio base de dades:
        BD = new DataSourceDAO(this.getApplicationContext());
        model = new ModelClassSQL(4, 0, data, date);

        probaSP = getApplicationContext().getSharedPreferences("Proba_DB", getApplicationContext().MODE_PRIVATE);

        dataLayout = (EditText) findViewById(R.id.data_probacalculs_introduce);
        viewFromDB = (EditText) findViewById(R.id.show_data_fromDB);
        viewFromSP = (EditText) findViewById(R.id.show_data_fromSP);



        //Escriptura en memoria per Shared Preference
        buttonSaveSP();
        buttonGetSP();

        //Escriptura en memoria a Base de Dades


        buttonSaveBD();
        getInfoBD();

    }



    private void buttonSaveSP() {
        buttonSPSave = (Button) findViewById(R.id.guarda_sharedpreference);
        buttonSPSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dataLayout = (EditText) findViewById(R.id.data_probacalculs_introduce);
                infolayout = dataLayout.getText().toString();

                saveInfo(infolayout);


                CharSequence text = "Data has been saved OKAY";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
            }
        });
    }


    private void buttonGetSP(){
        buttonSPview = (Button) findViewById(R.id.view_data_fromSP);
        buttonSPview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                String datafromsp = getInfo();
                viewFromSP.setText(datafromsp);

                CharSequence text = "The is loaded";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
            }
        });}


    public  void saveInfo(String layout){
        SharedPreferences.Editor editor = probaSP.edit();
        editor.putString("InfoLayout", layout);
        editor.commit();
    }

    public String getInfo(){
        String info = probaSP.getString("InfoLayout", null);
        return info;
    }

    public void buttonSaveBD(){
        buttonDBSave = (Button) findViewById(R.id.guarda_database);
        buttonDBSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                EditText a = (EditText) findViewById(R.id.data_probacalculs_introduce);
                Float data = Float.valueOf(a.getText().toString());
                long date = System.currentTimeMillis();
                model = new ModelClassSQL(3, 0, data, date);

                try {
                    BD.addparameters(model);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                CharSequence text = "The data is saved to the DB";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
            }
        });


}
    public void getInfoBD(){
        buttonDBview = (Button) findViewById(R.id.view_data_fromDB);
        buttonDBview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                //Mostramos el ultimo Atributo
                try {
                    atr = BD.getAtributes(3);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Iterator it=atr.iterator();
                viewFromDB.setText(""+((Atribute)it.next()).getVar());

                CharSequence text = "Data has been loaded from DB";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
            }
        });


    }

}





