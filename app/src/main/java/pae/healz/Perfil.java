package pae.healz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class Perfil extends AppCompatActivity {

    private static final String TAG = "Error in writing info";
    //De momento, Name y SecondName no se guarda (para un futuro)
    private EditText Name, Name2, SecondName, Sex, Age, Weight, Height;
    private final static String FILE_USER = "InfoUser.txt";
    private ArrayList<Integer> Information;
    private Button button;
    private DataManager data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        data = new DataManager(getApplicationContext());
        data.crearTXT();
        data.Inicializacion_Information();
        data.establecerTiempoInicial();
        inicializacion();
        button_save();
    }

    private void inicializacion(){

        Name = (EditText) findViewById(R.id.Name);
        SecondName = (EditText) findViewById(R.id.Second_Name);
        Sex = (EditText) findViewById(R.id.Sex);
        Age = (EditText) findViewById(R.id.Age);
        Weight = (EditText) findViewById(R.id.Weight);
        Height =(EditText) findViewById(R.id.Height);


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





                        OutputStreamWriter outSWMensaje = new OutputStreamWriter(
                                openFileOutput(FILE_USER, Context.MODE_PRIVATE));

                        outSWMensaje.write(Name.getText().toString() + "\n");
                        outSWMensaje.write(SecondName.getText().toString() + "\n");
                        outSWMensaje.write(Sex.getText().toString() + "\n");
                        outSWMensaje.write(Age.getText().toString() + "\n");
                        outSWMensaje.write(Height.getText().toString() + "\n");
                        outSWMensaje.write(Weight.getText().toString() + "\n");
                        outSWMensaje.close();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        // Toast.makeText(getApplicationContext(), "The file TXT is impossible to be created",
                        //  Toast.LENGTH_LONG).show();
                    }
                    //   actualizarTXT();

                } else {
                    //Toast.makeText(Perfil.this,
                    //   "Please, introduce all the information",
                    //  Toast.LENGTH_LONG).show();
                }
            }
        });
    }



}
