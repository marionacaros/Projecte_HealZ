package pae.healz.UserData;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Marc on 28/04/2016.
 */
public class User {
    Context ctx;
    SharedPreferences prefsuser;

    public User (Context ctx){
        this.ctx=ctx;
        prefsuser =  ctx.getSharedPreferences("Datos Usuario", Context.MODE_PRIVATE);

    }

    public  void saveUserData( String name, String secondName, String age, String sex, String weight, String height){
        SharedPreferences.Editor editor = prefsuser.edit();
        editor.putString("Nombre", name);
        editor.putString("Apellido", secondName);
        editor.putString("Edad", age);
        editor.putString("Sexo", sex);
        editor.putString("Peso", weight);
        editor.putString("Altura", height);
        editor.commit();
    }

    public String getname(){
        String name = prefsuser.getString("Nombre", null);
        return name;
    }
    public String getsecondname(){
        String secondname = prefsuser.getString("Apellido", null);
        return secondname;
    }
    public String getsex(){
        String sex = prefsuser.getString("Sexo", null);
        return sex;
    }
    public String getage(){
        String age = prefsuser.getString("Edad", null);
        return age;
    }
    public String getweight(){
        String weight = prefsuser.getString("Peso", null);
        return weight;
    }
    public String getheight(){
        String height = prefsuser.getString("Altura", null);
        return height;
    }


}
