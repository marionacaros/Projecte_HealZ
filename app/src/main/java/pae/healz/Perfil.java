package pae.healz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import pae.healz.SQLite.UserSQLiteHelper;

public class Perfil extends AppCompatActivity {

    private static final String TAG = "Error in writing info";

    private DataBase dataBase;
    private int NumeroLinea=0;
    private EditText Name, SecondName, Sex, Age, Weight, Height;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        //Declaramos base de datos y accedemos en modo escritura
        UserSQLiteHelper dbHelp = new UserSQLiteHelper(getBaseContext());
        SQLiteDatabase db = dbHelp.getWritableDatabase();
        Toast.makeText(getBaseContext(), "Base de datos preparada", Toast.LENGTH_LONG).show();
    }







    private void button_save(){
        button = (Button) findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (Name.getText().length() != 0
                        && SecondName.getText().length() != 0
                        && Sex.getText().length() != 0
                        && Age.getText().length() != 0
                        && Height.getText().length() != 0
                        && Weight.getText().length() != 0) {
                    try {

                        dataBase.escribirlinea(Name.getText().toString());
                        dataBase.escribirlinea(SecondName.getText().toString());
                        dataBase.escribirlinea(Sex.getText().toString());
                        dataBase.escribirlinea(Age.getText().toString());
                        dataBase.escribirlinea(Height.getText().toString());
                        dataBase.escribirlinea(Weight.getText().toString());


                        //OutputStreamWriter outSWMensaje = new OutputStreamWriter(
                        //        openFileOutput(FILE_USER, Context.MODE_PRIVATE));

                        //outSWMensaje.write(Name.getText().toString() + "\n");
                        //outSWMensaje.write(SecondName.getText().toString() + "\n");
                        //outSWMensaje.write(Sex.getText().toString() + "\n");
                        //outSWMensaje.write(Age.getText().toString() + "\n");
                       // outSWMensaje.write(Height.getText().toString() + "\n");
                        //outSWMensaje.write(Weight.getText().toString() + "\n");
                        //outSWMensaje.close();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());

                    }

                } else {
                    Toast.makeText(Perfil.this, "Please, introduce all the information", Toast.LENGTH_LONG).show();
                }
            }
        });
    }



}
