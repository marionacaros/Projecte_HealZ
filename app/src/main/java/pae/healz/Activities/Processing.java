package pae.healz.Activities;

import android.content.Context;
import android.provider.ContactsContract;

import pae.healz.SQLite.DataSourceDAO;
import pae.healz.SQLite.ModelClassSQL;
import pae.healz.UserData.User;

/**
 * Created by Marc on 05/05/2016.
 */
public class Processing {
    //Aquesta classe conté totes les formules i calculs
    //Recull de bluetooth, calcula i escriu sobre la base de dades
    private int type =0; //TBW=1, FFM=2, FM=3
    private double altura=0, parteReal=0, peso=0, parteImaginaria=0, edad=0, genero =0; //hombre=1, mujer=0
    private double fM=0, fFM=0, tBW=0;
    private double modulo=0, fase=0;

    private User user;
    Context ctx;
    private ModelClassSQL modelTBW;
    private DataSourceDAO DB;


    public Processing(Context cont){
        ctx=cont;
        user = new User(ctx);
        inicializacionDatosUser();
        DB= new DataSourceDAO(ctx);

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

    public double calcula_datos(int type){
        tBW=0.7493*(((altura)*(altura))/parteReal)+0.1362*peso-0.0778*edad+2.8223*genero+5.6436;
        fFM=0.664*(((altura)*(altura))/parteReal)+0.0967*peso+0.0940*parteImaginaria-0.2429;
        fM=peso-fFM;
        if(type == 1){
            return tBW*100;
        }
        else if(type == 2){
            return fFM*100;
        }
        else if(type == 3){
            return fM*100;
        }
        else
            return 1.0;
    }

    public void guardarInfoEnBD(){
        long date = System.currentTimeMillis();
        modelTBW = new ModelClassSQL(2, 0, (float)(tBW), date); //revisar el parseo

    }


    /*
    Calculo de la frecuencia respiratoria

    Me pasan: Vector de modulos (desde bluetooth) - A bluetooth hem de guardar un vector amb els moduls

    (n) = b(1)*x(n) + b(2)*x(n-1) + b(3)*x(n-2)+b(4)*x(n-3)+b(5)*x(-4)- a(2)*y(n-1) -a(3)*y(n-2)-a(4)*y(n-3)-a(5)*y(n-4)0.0015  b(1)= 0.0015 b(2)=0b(3)=-0.0029 b(4)=0b(5)= 0.0015 a(2)=-3.8862 a(3)=5.6673 a(4)=-3.6761 a(5)=0.8949




    */

}
