package pae.healz;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by Marc on 21/04/2016.
 */
public class DataManager {

    private static final String TAG = "Error in writing info";
    private EditText Name, Name2, SecondName, Sex, Age, Weight, Height;
    private final static String FILE_USER = "InfoUser.txt";
    private ArrayList<Integer> Information;
    private Button button;
    Context ctx;

    public void Inicializacion_Information() {
        Information = new ArrayList<Integer>();
    }

    public DataManager (Context ctx){
        this.ctx = ctx;
    }




    public void establecerTiempoInicial() {
        //Funcionamiento: Gestionamos la entrada y salida de informacion sobre el .txt
        // Comprobamos si existe el archivo
        if (existsFile(FILE_USER)) {
            // En el caso de que exista, intentamos rellenar los EditText, si no
            // se rellenan de forma correcta, el archivo.txt estaba corrupto.
            if (!SetText()) {
                // Avisamos al usuario de que el archivo era corrupto.
                //Toast.makeText(,
                       // "Please fill in all the parameters",
                       // Toast.LENGTH_LONG).show();
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

    public void crearTXT() {
        //Le paso un int, si es 0 es archivo de user, sino es archivo de datos
        //Funcionamiento: Creamos el .txt y lo inicializamos
        try {
            // Creamos un objeto OutputStreamWriter, que será el que nos permita
            // escribir en el archivo de texto. Si el archivo no existía se creará
            // automáticamente.

            // La ruta en la que se creará el archivo será /ruta de nuestro programa/data/data/
                OutputStreamWriter outSWMensaje = new OutputStreamWriter(
                        ctx.openFileOutput(FILE_USER, Context.MODE_PRIVATE));


            // Escribimos los 5 tiempos iniciales en el archivo.
            outSWMensaje.write("\n\n\n\n\n\n");

            // Cerramos el flujo de escritura del archivo, este paso es obligatorio,
            // de no hacerlo no se podrá acceder posteriormente al archivo.
            outSWMensaje.close();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


    //Funció declarada per llegir el Nom del ficher de text
    public EditText getName(){
        try {
            InputStreamReader archi = new InputStreamReader(
                    ctx.openFileInput(FILE_USER));
            BufferedReader bufferInfo = new BufferedReader(archi);
            String nombre = bufferInfo.readLine();
            Name2.setText(nombre);

            bufferInfo.close();
            return Name2;

        } catch (Exception e) {
            return null;
        }
    }


    public  boolean SetText() {
        try {
            //Funcionamiento: Leemos el archivo de texto y ponemos, en cada edittext lo que toca

            // Creamos un objeto InputStreamReader, que será el que nos permita
            // leer el contenido del archivo de texto.
            InputStreamReader archivo = new InputStreamReader(
                    ctx.openFileInput(FILE_USER));
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
        for (String tmp : ctx.fileList()) {
            if (tmp.equals(fileName))
                return true;
        }
        return false;
    }









}
