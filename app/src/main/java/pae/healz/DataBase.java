package pae.healz;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Marc on 04/03/2016.
 */
public class DataBase {

    private final static String FILE_USER = "InfoUser.txt";
    private final static String FILE_DATA = "Data.txt";
    Context ctx;


    public DataBase (Context ctx){
        this.ctx = ctx;
    }

    public boolean escribirlinea(String contenido) {
        try {
            File tarjeta = Environment.getExternalStorageDirectory();
            Log.d("test", tarjeta.getAbsolutePath());
            File file = new File(tarjeta.getAbsolutePath() + File.separator, FILE_USER);
            OutputStreamWriter osw = new OutputStreamWriter(
                    new FileOutputStream(file));
            osw.write(contenido+"\n");
            osw.flush();
            //osw.close();
            return true;
        } catch (IOException ioe) {
            return false;
        }
    }

    public String leerlinea(int Nlinea, EditText editText) {
        String nomarchivo = editText.getText().toString();
        File tarjeta = Environment.getExternalStorageDirectory();
        File file = new File(tarjeta.getAbsolutePath(), nomarchivo);//PONER FILE DATA!!/FILE_USER
        try {
            FileInputStream fIn = new FileInputStream(file);
            InputStreamReader archivo = new InputStreamReader(fIn);
            BufferedReader br = new BufferedReader(archivo);
            String linea = br.readLine();

            for (int aux=0; aux<=Nlinea; aux++){
                linea = br.readLine();
            }
            br.close();
            archivo.close();
            return linea;

        } catch (IOException e) {
            return null;
        }
    }

    //Eliminar no es necesario
    public void crearTXT(boolean type) {
        //type true para archivo de user info, sino datosheartrate
        try {
            // La ruta en la que se creará el archivo será /ruta de nuestro programa/data/data/
            if(type) {
                OutputStreamWriter outSWMensaje = new OutputStreamWriter(
                        ctx.openFileOutput(FILE_USER, Context.MODE_PRIVATE));
                outSWMensaje.write("\n\n\n\n\n\n");
                outSWMensaje.close();
            }
            else{
                OutputStreamWriter outSWMensaje = new OutputStreamWriter(
                        ctx.openFileOutput(FILE_DATA, Context.MODE_PRIVATE));
                outSWMensaje.write("\n\n\n\n\n\n");
                outSWMensaje.close();

            }
        } catch (Exception e) {
        }
    }


}
