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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Perfil extends AppCompatActivity {

    private static final String TAG = "Error in writing info";
    //De momento, Name y SecondName no se guarda (para un futuro)
    private EditText Name, SecondName, Sex, Age, Weight, Height;
    private final static String FILE = "InfoUser.txt";
    private ArrayList<Integer> Information;
    private String NameS, SecondNameS;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        button_save();
        inicializacion();
        Inicializacion_Information();
        establecerTiempoInicial();
    }

    private void Inicializacion_Information() {
        Information = new ArrayList<Integer>();
    }

    private void inicializacion(){

        Name = (EditText) findViewById(R.id.Name);
        SecondName = (EditText) findViewById(R.id.Second_Name);
        Sex = (EditText) findViewById(R.id.Sex);
        Age = (EditText) findViewById(R.id.Age);
        Weight = (EditText) findViewById(R.id.Weight);
        Height =(EditText) findViewById(R.id.Height);


    }

    private void establecerTiempoInicial() {
        //Funcionamiento: Gestionamos la entrada y salida de informacion sobre el .txt
        // Comprobamos si existe el archivo
        if (existsFile(FILE)) {
            // En el caso de que exista, intentamos rellenar los EditText, si no
            // se rellenan de forma correcta, el archivo.txt estaba corrupto.
            if (!SetText()) {
                // Avisamos al usuario de que el archivo era corrupto.
                Toast.makeText(Perfil.this,
                        "Please fill in the parameters",
                        Toast.LENGTH_LONG).show();
                // Creamos de nuevo el archivo.
                crearTXT();
                // Rellenamos los EditText con los valores asignados por defecto
                // en el archvio txt.
                SetText();
            }

        } else {
            // En el caso de que no existiera el archivo txt lo creamos y rellenamos
            // los EditText.
            crearTXT();
            SetText();
        }
    }

    private void crearTXT() {
        //Funcionamiento: Creamos el .txt y lo inicializamos
        try {
            // Creamos un objeto OutputStreamWriter, que será el que nos permita
            // escribir en el archivo de texto. Si el archivo no existía se creará
            // automáticamente.

            // La ruta en la que se creará el archivo será /ruta de nuestro programa/data/data/

            OutputStreamWriter outSWMensaje = new OutputStreamWriter(
                    openFileOutput(FILE, Context.MODE_PRIVATE));

            // Escribimos los 5 tiempos iniciales en el archivo.
            outSWMensaje.write("Name\nSecond Name\nSex\nAge\nWeight\nHeight\n");

            // Cerramos el flujo de escritura del archivo, este paso es obligatorio,
            // de no hacerlo no se podrá acceder posteriormente al archivo.
            outSWMensaje.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private boolean SetText() {
        try {
            //Funcionamiento: Leemos el archivo de texto y ponemos, en cada edittext lo que toca

            // Creamos un objeto InputStreamReader, que será el que nos permita
            // leer el contenido del archivo de texto.
            InputStreamReader archivo = new InputStreamReader(
                    openFileInput(FILE));
            // Creamos un objeto buffer, en el que iremos almacenando el contenido
            // del archivo.
            BufferedReader bufferInfo = new BufferedReader(archivo);
            // Por cada EditText leemos una línea y escribimos el contenido en el
            // EditText.
            String texto = bufferInfo.readLine();
            Name.setText(texto);

            texto = bufferInfo.readLine();
            SecondName.setText(texto);

            texto = bufferInfo.readLine();
            Sex.setText(texto);

            texto = bufferInfo.readLine();
            Age.setText(texto);

            texto = bufferInfo.readLine();
            Weight.setText(texto);

            texto = bufferInfo.readLine();
            Height.setText(texto);

            // Cerramos el flujo de lectura del archivo.
            bufferInfo.close();
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean existsFile(String fileName) {
        for (String tmp : fileList()) {
            if (tmp.equals(fileName))
                return true;
        }
        return false;
    }

    private void button_save(){
        button = (Button) findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                if (Name.getText().length() != 0
                        && SecondName.getText().length() != 0
                        && Sex.getText().length() != 0
                        && Age.getText().length() != 0
                        && Height.getText().length() != 0
                        && Weight.getText().length() != 0) {
                    try {    OutputStreamWriter outSWMensaje = new OutputStreamWriter(
                            openFileOutput(FILE, Context.MODE_PRIVATE));

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

                }
                else{
                    //Toast.makeText(Perfil.this,
                         //   "Please, introduce all the information",
                          //  Toast.LENGTH_LONG).show();
                }
            }
        });
    }








}
