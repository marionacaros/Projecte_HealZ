package pae.healz.Activities;

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

import pae.healz.Json.User;
import pae.healz.R;
import pae.healz.SQLite.SQLiteHelper;
import pae.healz.SQLite.SQLiteHelper;

public class Perfil extends AppCompatActivity {

    private static final String TAG = "Error in writing info";
    private EditText name, secondName, sex, age, weight, height;
    private String nombre, apellido, sexo, edad, peso, altura;
    private Button button;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        name = (EditText) findViewById(R.id.Name);
        secondName = (EditText) findViewById(R.id.Second_Name);
        age = (EditText) findViewById(R.id.Age);
        sex = (EditText) findViewById(R.id.Sex);
        height = (EditText) findViewById(R.id.Height);
        weight = (EditText) findViewById(R.id.Weight);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre = name.getText().toString();
                apellido = secondName.getText().toString();
                edad = age.getText().toString();
                sexo = sex.getText().toString();
                altura = height.getText().toString();
                peso = weight.getText().toString();
            }
        });

        user = new User(nombre, apellido, edad, sexo, altura, peso);


    }








    private void button_save(){
        button = (Button) findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
    }

}
