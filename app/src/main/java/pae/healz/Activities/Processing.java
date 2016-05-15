package pae.healz.Activities;

import android.content.Context;
import android.provider.ContactsContract;

import java.lang.reflect.Array;
import java.sql.SQLException;

import pae.healz.SQLite.DataSourceDAO;
import pae.healz.SQLite.ModelClassSQL;
import pae.healz.UserData.User;

/**
 * Created by Marc on 05/05/2016.
 */
public class Processing { //Correcte declaracio?
    //Aquesta classe conté totes les formules i calculs
    //Recull de bluetooth, calcula i escriu sobre la base de dades
    private int type =0; //TBW=1, FFM=2, FM=3
    private double altura=0, parteReal=0, peso=0, parteImaginaria=0, edad=0, genero =0; //hombre=1, mujer=0
    private double fM=0, fFM=0, tBW=0;
    private double modulo=0, fase=0;

    private User user;
    Context ctx;
    private ModelClassSQL modelTBW, modelFM, modelFFM, modelW;
    private DataSourceDAO DB;


    private Array[] infoNew = new Array[150];
    private Array[] infoBluetooth = new Array[150]; //Viene de bluetooth, no hay que inicializar aqui



    public Processing(Context cont){
        ctx=cont;
        user = new User(ctx);
        //dataFromBLE = new GestorBLE(ctx.getApplicationContext(), GestorBLE.IBLEDATA);
        inicializacionDatosUser();
        DB= new DataSourceDAO(ctx);

    }


    public float median(float[] vector){
        float sum=0;
        for(int i=0; i<vector.length; i++) {
            sum = sum + vector[i];
        }
        float median = sum/vector.length;
        return median;
    }

    public void parteRealImaginaria(){
        //Aqui leemos lo que nos llega del bluetooth

    }


    public void inicializacionDatosUser(){
        altura = Double.parseDouble(user.getheight());
        peso = Double.parseDouble(user.getweight());
        edad = Double.parseDouble(user.getage());
        if (user.getsex().equalsIgnoreCase("man"))genero = 1;
        else genero=0;

    }

    public double calcula_datos(int type, double real, double imag){
        tBW=0.7493*(((altura)*(altura))/real)+0.1362*peso-0.0778*edad+2.8223*genero+5.6436;
        fFM=0.664*(((altura)*(altura))/real)+0.0967*peso+0.0940*imag-0.2429;
        fM=peso-fFM;
        if(type == 1)return tBW*100;
        else if(type == 2)return fFM*100;
        else if(type == 3)return fM*100;
        else return 1.0;
    }

    public void guardarInfoEnBD(){
        //De moment no impementem Heart Rate
        //Guardem tota la informacio a la vegada
        long date = System.currentTimeMillis();
        modelTBW = new ModelClassSQL(2, 0, (float)(tBW), date);
        modelFM = new ModelClassSQL(4, 0, (float)(fM), date); //revisar el parseo
        modelW = new ModelClassSQL(3, 0, (float)peso, date);
        modelFFM = new ModelClassSQL(1, 0, (float)(fFM), date);

        try {
            DB.addparameters(modelFFM);
            DB.addparameters(modelTBW);
            DB.addparameters(modelFM);
            DB.addparameters(modelW);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Funcion para el calculo de la Frecuencia Respiratoria
    public int calcularFR(){
        //Coeficientes filtro frecuencia respiratoria
        double b1= 0.0015,b2=0,b3=-0.0029,b4=0,b5= 0.0015,a2=-3.8862,a3=5.6673,a4=-3.6761,a5=0.8949;
        //El vector que tiene que llegar de bluetooth tiene que ser de modulos
        for(int i=0; i<((infoBluetooth.length)/5); i++){ //OJO QUE NO SE SI ESTA BE
            //infoNew[i]=b1*Float.parseFloat(infoBluetooth[i]);


        }


        return 0;
    }

    /*
    Calculo de la frecuencia respiratoria

    Me pasan: Vector de modulos (desde bluetooth) - A bluetooth hem de guardar un vector amb els moduls

    (n) = b(1)*x(n) + b(2)*x(n-1) + b(3)*x(n-2)+b(4)*x(n-3)+b(5)*x(-4)- a(2)*y(n-1) -a(3)*y(n-2)-a(4)*y(n-3)-a(5)*y(n-4)0.0015
    //b(1)= 0.0015 b(2)=0b(3)=-0.0029 b(4)=0b(5)= 0.0015 a(2)=-3.8862 a(3)=5.6673 a(4)=-3.6761 a(5)=0.8949




    */

}
